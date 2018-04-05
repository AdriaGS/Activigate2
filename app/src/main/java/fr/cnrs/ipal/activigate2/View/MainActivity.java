package fr.cnrs.ipal.activigate2.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import fr.cnrs.ipal.activigate2.HAR.HARManager;
import fr.cnrs.ipal.activigate2.HAR.HARService;
import fr.cnrs.ipal.activigate2.HAR.Utils;
import fr.cnrs.ipal.activigate2.Logger.Logger;
import fr.cnrs.ipal.activigate2.MyApplication;
import fr.cnrs.ipal.activigate2.R;
import fr.cnrs.ipal.activigate2.View.Secondary.AboutActivity;
import fr.cnrs.ipal.activigate2.View.Secondary.SettingsActivity;
//import ipal.cnrs.fr.activigate_googleapi.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String HAR_PREFERENCES = "HAR_Preferences";

    TextView sensingTV;
    TextView noHouseIDTV;
    ImageButton fitbitButton;
    ImageButton sensingButton;
    EditText houseIDET;

    HARManager harManager = new HARManager();
    Utils utils = new Utils();
    //private ActivityMainBinding binding;

    SharedPreferences sharedPreferences;
    Logger logger = new Logger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assigning TextView and ImageViews to variables
        sensingTV = (TextView) findViewById(R.id.sensingTextView);
        fitbitButton = (ImageButton) findViewById(R.id.fitbitDataButton);
        sensingButton = (ImageButton) findViewById(R.id.sensingButton);

        sharedPreferences = MyApplication.instance.getSharedPreferences(HAR_PREFERENCES, this.MODE_PRIVATE);
        utils.setJson2Send(utils.string2Array(sharedPreferences.getString("json2Send", null)));
        utils.setLastSensedActivity(sharedPreferences.getString("lastSensedActivity", null));
        utils.setActivitiesHistory(utils.string2Array(sharedPreferences.getString("activitiesHistory", null)));
        Boolean isSensing = sharedPreferences.getBoolean("isSensing", false);
        String houseId = sharedPreferences.getString("houseId", null);
        utils.setIsSensing(isSensing);
        if(isSensing) {
            sensingButton.setBackground(getResources().getDrawable(R.drawable.stop_default));
            harManager.init(this, 1);
            harManager.start(this);
        }
        logger.appendLog("Loaded variables in SharedPreferences\n");
        logger.appendLog("Json2Send" + sharedPreferences.getString("json2Send", null));
        logger.appendLog("Activities History" + sharedPreferences.getString("activitiesHistory", null));


        if(houseId == null) {
            LayoutInflater li = LayoutInflater.from(this);
            View prompt = li.inflate(R.layout.house_id_popup, null);

            houseIDET = prompt.findViewById(R.id.housID);
            noHouseIDTV = prompt.findViewById(R.id.wrongID);

            AlertDialog.Builder houseIdBox = new AlertDialog.Builder(this);
            houseIdBox.setView(prompt);
            houseIdBox.setCancelable(false);
            houseIdBox.setMessage(R.string.houseIdBox)
                    .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(houseIDET.equals("")){
                                noHouseIDTV.setVisibility(View.VISIBLE);
                            }
                            else {
                                utils.setHouseID(houseIDET.getText().toString());
                                save();
                                return;
                            }
                        }
                    });
            // Create the AlertDialog object and return it
            houseIdBox.create();
            houseIdBox.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, new IntentFilter(HARService.LOCAL_BROADCAST_NAME));

        updateTextView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        save();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.basic_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.activityHistory:
                goHistoric();
                return true;
            case R.id.settings:
                goSettings();
                return true;
            case R.id.about:
                goAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSensing(View view){
        if(!utils.getIsSensing()) {
            startSensing();
        }
        else {
            AlertDialog.Builder cancelSensing = new AlertDialog.Builder(this);
            cancelSensing.setMessage(R.string.cancelAlertBox)
                    .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            stopSensing();
                        }
                    })
                    .setNegativeButton(R.string.notCancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            return;
                        }
                    });
            // Create the AlertDialog object and return it
            cancelSensing.create();
            cancelSensing.show();

        }
    }

    private void startSensing() {
        // Initialize the Human Activity Recognizer Manager
        harManager.init(this, 30);
        // Start the Sensing
        harManager.start(this);
        utils.setIsSensing(true);
        //moveTaskToBack(true);
        updateTextView();
    }

    public void stopSensing() {
        // Stop the Sensing
        harManager.stop(MainActivity.this);
        utils.setIsSensing(false);
        updateTextView();
    }

    // Broadcast Receiver to receive last activity that was sensed
    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTextView();
        }
    };

    public void goFitbit(View view){
        Intent toFitbit = new Intent(this, LoginActivity.class);
        startActivity(toFitbit);

    }

    private void goHistoric() {
        Intent goHistoric = new Intent(this, HistoricActivity.class);
        startActivity(goHistoric);
    }

    private void goSettings() {
        Intent goSettings = new Intent(this, SettingsActivity.class);
        startActivity(goSettings);
    }

    private void goAbout() {
        Intent goAbout = new Intent(this, AboutActivity.class);
        startActivity(goAbout);
    }

    private void updateTextView() {
        if (utils.getIsSensing()) {
            sensingButton.setBackground(getResources().getDrawable(R.drawable.stop_default));
            sensingTV.setText("Stop Recognition");
        }
        else {
            sensingButton.setBackground(getResources().getDrawable(R.drawable.start_default));
            sensingTV.setText("Start Recognition");
        }
    }

    private void save() {
        sharedPreferences = MyApplication.instance.getSharedPreferences(HAR_PREFERENCES, this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("json2Send", utils.array2String(utils.getJson2Send()));
        editor.putString("activitiesHistory", utils.array2String(utils.getActivitiesHistory()));
        editor.putString("lastSensedActivity", utils.getLastSensedActivity());
        editor.putBoolean("isSensing", utils.getIsSensing());
        editor.putString("houseId", utils.getHouseID());
        editor.commit();
        logger.appendLog("Saved variables in SharedPreferences\n");
    }

}

