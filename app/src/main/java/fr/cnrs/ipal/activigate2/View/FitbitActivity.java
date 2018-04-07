package fr.cnrs.ipal.activigate2.View;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import fr.cnrs.ipal.activigate2.Fitbit.API.Activities.Activities;
import fr.cnrs.ipal.activigate2.Fitbit.API.HeartRate.ActivitiesHeart;
import fr.cnrs.ipal.activigate2.Fitbit.API.HeartRate.HeartRate;
import fr.cnrs.ipal.activigate2.Fitbit.API.Sleep.Sleep;
import fr.cnrs.ipal.activigate2.Fitbit.API.Sleep.SleepData;
import fr.cnrs.ipal.activigate2.Fitbit.FitbitUtils;
import fr.cnrs.ipal.activigate2.Fitbit.OAuthServerIntf;
import fr.cnrs.ipal.activigate2.Fitbit.OAuthToken;
import fr.cnrs.ipal.activigate2.Fitbit.RetrieveData;
import fr.cnrs.ipal.activigate2.Fitbit.RetrofitBuilder;
import fr.cnrs.ipal.activigate2.HAR.HARService;
import fr.cnrs.ipal.activigate2.R;
import fr.cnrs.ipal.activigate2.View.Secondary.CircleFillView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FitbitActivity extends AppCompatActivity {

    ProgressDialog pd;
    CircleFillView circleFill;

    TextView lightlyActive;
    TextView sedentaryMin;
    TextView veryActive;
    TextView steps;
    TextView restingHeartRate;
    TextView sleepDuration;
    TextView sleepEfficiency;
    TextView awakeningsCount;

    TextView outOfRangeZone;
    TextView fatBurnZone;
    TextView cardioZone;
    TextView peakZone;

    TextView day;

    SwipeRefreshLayout mSwipeRefreshLayout;

    int restingHeartRate_val = 0;
    int sedentaryMin_val;
    int lightlyActiveMin_val;
    int veryActiveMin_val;
    int steps_val;
    int stepsGoal;
    int sleepDuration_val;
    int sleepEfficiency_val;
    int awakeningsCount_val;
    ArrayList<Integer> minutesZones;

    FitbitUtils fbUtils = new FitbitUtils();
    RetrieveData retrieveData = new RetrieveData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitbit);

        lightlyActive = findViewById(R.id.lightlyActiveMinTV);
        sedentaryMin = findViewById(R.id.sedentaryMinTV);
        veryActive = findViewById(R.id.veryActiveMinTV);
        steps = findViewById(R.id.steps);
        restingHeartRate = findViewById(R.id.restingHRTV);
        sleepDuration = findViewById(R.id.sleepDurationTV);
        sleepEfficiency = findViewById(R.id.efficiencyTV);
        awakeningsCount = findViewById(R.id.awakeningCountTV);

        outOfRangeZone = findViewById(R.id.outOfRangeTV);
        fatBurnZone = findViewById(R.id.fatBurnTV);
        cardioZone = findViewById(R.id.cardioTV);
        peakZone = findViewById(R.id.peakTV);

        day = findViewById(R.id.dayTV);

        mSwipeRefreshLayout = findViewById(R.id.fitbit_swipe_refresh_layout);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        day.setText("TODAY - " + dateFormat.format(date));

        circleFill = (CircleFillView) findViewById(R.id.stepsProgress);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveData.updateData(FitbitActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, new IntentFilter(HARService.LOCAL_BROADCAST_NAME));
        super.onResume();

    }

    // Broadcast Receiver to receive last activity that was sensed
    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateView();
        }
    };

    private void updateView() {

        int steps_percent = fbUtils.getSteps()*100/fbUtils.getStepsGoal();

        steps.setText("Steps\n" + String.valueOf(fbUtils.getSteps()));
        circleFill.setValue(steps_percent);

        lightlyActive.setText(String.valueOf(String.format("%02d:%02d H",
                TimeUnit.MINUTES.toHours(fbUtils.getLightlyActiveMin()),
                fbUtils.getLightlyActiveMin() - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(fbUtils.getLightlyActiveMin())))));
        sedentaryMin.setText(String.valueOf(String.format("%02d:%02d H",
                TimeUnit.MINUTES.toHours(fbUtils.getSedentaryMin()),
                fbUtils.getSedentaryMin() - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(fbUtils.getSedentaryMin())))));
        veryActive.setText(String.valueOf(String.format("%02d:%02d H",
                TimeUnit.MINUTES.toHours(fbUtils.getVeryActiveMin()),
                fbUtils.getVeryActiveMin() - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(fbUtils.getVeryActiveMin())))));

        int totalMinutes = 0;
        for(int i = 0; i < fbUtils.getMinutesZones().size(); i++) {
            totalMinutes += fbUtils.getMinutesZones().get(i);
        }

        restingHeartRate.setText(String.valueOf(fbUtils.getRestingHeartRate()) + " bpm");
        outOfRangeZone.setText(String.valueOf(fbUtils.getMinutesZones().get(0)*100/totalMinutes) + "%");
        fatBurnZone.setText(String.valueOf(fbUtils.getMinutesZones().get(1)*100/totalMinutes) + "%");
        cardioZone.setText(String.valueOf(fbUtils.getMinutesZones().get(2)*100/totalMinutes) + "%");
        peakZone.setText(String.valueOf(fbUtils.getMinutesZones().get(3)*100/totalMinutes) + "%");

        sleepDuration.setText(String.format("%02d:%02d hours",
                TimeUnit.MILLISECONDS.toHours(fbUtils.getSleepDuration()),
                TimeUnit.MILLISECONDS.toMinutes(fbUtils.getSleepDuration()) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(fbUtils.getSleepDuration()))
        ));
        sleepEfficiency.setText(String.valueOf(fbUtils.getSleepEfficiency()) + "%");
        awakeningsCount.setText(String.valueOf(fbUtils.getAwakeningsCount()));

        mSwipeRefreshLayout.setRefreshing(false);

    }
}