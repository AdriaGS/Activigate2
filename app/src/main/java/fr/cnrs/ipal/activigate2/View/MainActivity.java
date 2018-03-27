package fr.cnrs.ipal.activigate2.View;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import fr.cnrs.ipal.activigate2.HAR.HARManager;
import fr.cnrs.ipal.activigate2.HAR.HARService;
import fr.cnrs.ipal.activigate2.HAR.HARUtils;
import fr.cnrs.ipal.activigate2.MyApplication;
import fr.cnrs.ipal.activigate2.R;
//import ipal.cnrs.fr.activigate_googleapi.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String HAR_PREFERENCES = "HAR_Preferences";

    TextView sensingTV;
    TextView activityTV;
    Button fitbitButton;

    HARManager harManager = new HARManager();
    HARUtils harUtils = new HARUtils();
    //private ActivityMainBinding binding;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assigning TextView to variables
        sensingTV = (TextView) findViewById(R.id.sensingTextView);
        activityTV = (TextView) findViewById(R.id.activity_tv);
        fitbitButton = (Button) findViewById(R.id.toHistoricButton);

        // TODO: Load Json2Send
        sharedPreferences = MyApplication.instance.getSharedPreferences(HAR_PREFERENCES, this.MODE_PRIVATE);
        harUtils.setJson2Send(harUtils.string2Array(sharedPreferences.getString("json2Send", null)));
        harUtils.setLastSensedActivity(sharedPreferences.getString("lastSensedActivity", null));
        Boolean isSensing = sharedPreferences.getBoolean("isSensing", false);
        harUtils.setIsSensing(isSensing);
        if(isSensing) {
            harManager.init(this, 1);
            harManager.start(this);
        }

    }

    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, new IntentFilter(HARService.LOCAL_BROADCAST_NAME));

        updateTextView();
    }

    public void onSensing(View view){
        if(!harUtils.getIsSensing()) {
            // Initialize the Human Activity Recognizer Manager
            harManager.init(this, 30);
            // Start the Sensing
            harManager.start(this);
            harUtils.setIsSensing(true);
            //moveTaskToBack(true);
        }
        else {
            // Stop the Sensing
            harManager.stop(this);
            harUtils.setIsSensing(false);
        }
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

    private void updateTextView() {

        if (harUtils.getIsSensing()) {
            sensingTV.setText("SENSING");
            activityTV.setText("Lasted sensed activity: " + harUtils.getLastSensedActivity());
        }
        else {
            sensingTV.setText("NOT SENSING");
            activityTV.setText("NO ACTIVITY RECOGNIZED YET");
        }
    }

    private void save() {
        sharedPreferences = MyApplication.instance.getSharedPreferences(HAR_PREFERENCES, this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastSensedActivity", harUtils.getLastSensedActivity());
        editor.putBoolean("isSensing", harUtils.getIsSensing());
        editor.commit();
    }

    public static Object fromJson(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }
}

