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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import fr.cnrs.ipal.activigate2.HAR.HARManager;
import fr.cnrs.ipal.activigate2.HAR.HARService;
import fr.cnrs.ipal.activigate2.HAR.Utils;
import fr.cnrs.ipal.activigate2.Logger.Logger;
import fr.cnrs.ipal.activigate2.MyApplication;
import fr.cnrs.ipal.activigate2.R;
import fr.cnrs.ipal.activigate2.View.Secondary.AboutActivity;
import fr.cnrs.ipal.activigate2.View.Secondary.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    private static final String HAR_PREFERENCES = "HAR_Preferences";
    private static final String HOUSE_ID = "houseId";
    private static final String IS_SENSING = "isSensing";
    private static final String JSON_SEND = "json2Send";
    private static final String LAST_ACTIVITY = "lastSensedActivity";
    private static final String ACTIVITY_HISTORY = "activitesHistoric";

    TextView sensingTV;
    TextView noHouseIDTV;
    ImageButton fitbitButton;
    ImageButton sensingButton;
    EditText houseIDET;

    HARManager harManager = new HARManager();
    Utils utils = new Utils();
    //private ActivityMainBinding binding;

    SharedPreferences sharedPreferences;

    Boolean isSensing;
    String houseId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assigning TextView and ImageViews to variables
        sensingTV = (TextView) findViewById(R.id.sensingTextView);
        fitbitButton = (ImageButton) findViewById(R.id.fitbitDataButton);
        sensingButton = (ImageButton) findViewById(R.id.sensingButton);

        load();

        if(isSensing) {
            sensingButton.setBackground(getResources().getDrawable(R.drawable.stop_default));
            harManager.init(this, 1);
            harManager.start(this);
        }

        if(houseId.equals("")) {
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
                            }
                        }
                    });
            // Create the AlertDialog object and return it
            houseIdBox.create();
            houseIdBox.show();
        }
        else {
            Log.e("House ID", houseId);
            utils.setHouseID(houseId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
          
        try {
            // Initialize the Human Activity Recognizer Manager
            harManager.init(this, 10);
            // Start the Sensing
            harManager.start(this);
        }
        catch (Exception e) {
            Toast.makeText(this, "Error on Starting Activity Recognition", Toast.LENGTH_SHORT).show();
        }
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
        sharedPreferences = MyApplication.instance.getSharedPreferences(HAR_PREFERENCES, MyApplication.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(JSON_SEND, utils.array2String(utils.getJson2Send()));
        editor.putString(ACTIVITY_HISTORY, utils.array2String(utils.getActivitiesHistory()));
        editor.putString(LAST_ACTIVITY, utils.getLastSensedActivity());
        editor.putBoolean(IS_SENSING, utils.getIsSensing());
        editor.putString(HOUSE_ID, utils.getHouseID());
        editor.commit();
    }

    private void load() {
        sharedPreferences = MyApplication.instance.getSharedPreferences(HAR_PREFERENCES, MyApplication.MODE_PRIVATE);
        utils.setJson2Send(utils.string2Array(sharedPreferences.getString(JSON_SEND, null)));
        utils.setLastSensedActivity(sharedPreferences.getString(LAST_ACTIVITY, null));
        utils.setActivitiesHistory(utils.string2Array(sharedPreferences.getString(ACTIVITY_HISTORY, null)));
        isSensing = sharedPreferences.getBoolean(IS_SENSING, false);
        houseId = sharedPreferences.getString(HOUSE_ID, "");
        utils.setIsSensing(isSensing);
    }

}

