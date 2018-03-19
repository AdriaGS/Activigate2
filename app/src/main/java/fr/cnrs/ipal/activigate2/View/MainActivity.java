package fr.cnrs.ipal.activigate2.View;

import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.cnrs.ipal.activigate2.HAR.HARManager;
import fr.cnrs.ipal.activigate2.HAR.HARService;
import fr.cnrs.ipal.activigate2.HAR.HARUtils;
import fr.cnrs.ipal.activigate2.R;
//import ipal.cnrs.fr.activigate_googleapi.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    TextView sensingTV;
    TextView activityTV;
    Button fitbitButton;

    HARManager harManager = new HARManager();
    //private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assigning TextView to variables
        sensingTV = (TextView) findViewById(R.id.sensingTextView);
        activityTV = (TextView) findViewById(R.id.activity_tv);
        fitbitButton = (Button) findViewById(R.id.toHistoricButton);

        fitbitButton.setVisibility(View.INVISIBLE);

        //toSensingHistoric = new Intent(this, SecondaryActivity.class);
        //binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, new IntentFilter(HARService.LOCAL_BROADCAST_NAME));

        updateTextView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onSensing(View view){
        if(!HARUtils.isSensing) {
            // Initialize the Human Activity Recognizer Manager
            harManager.init(this, 0);
            // Start the Sensing
            harManager.start(this);
            //Task task = activityRecognitionClient.requestActivityUpdates(TIMER, pendingIntent);
            HARUtils.isSensing = true;
            moveTaskToBack(true);
        }
        else {
            // Stop the Sensing
            harManager.stop(this);
            //Task task = activityRecognitionClient.removeActivityUpdates(pendingIntent);
            HARUtils.isSensing = false;
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


    }

    private void updateTextView() {

        if(HARUtils.sensingRecord.size() > 0) {
            if (HARUtils.isSensing) {
                sensingTV.setText("SENSING");
                activityTV.setText("Lasted sensed activity: " + HARUtils.getLastSensedValue());
            } else {
                sensingTV.setText("NOT SENSING");
                activityTV.setText("NO ACTIVITY RECOGNIZED YET");
            }
        }
    }
}

