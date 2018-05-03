package fr.cnrs.ipal.activigate2.View;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import fr.cnrs.ipal.activigate2.Fitbit.FitbitUtils;
import fr.cnrs.ipal.activigate2.Fitbit.RetrieveData;
import fr.cnrs.ipal.activigate2.HAR.HARService;
import fr.cnrs.ipal.activigate2.R;
import fr.cnrs.ipal.activigate2.View.Secondary.CircleFillView;

public class FitbitActivity extends AppCompatActivity {

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
    EditText datePicker;

    SwipeRefreshLayout mSwipeRefreshLayout;
    DatePickerDialog.OnDateSetListener datePickerDialog;

    FitbitUtils fbUtils = new FitbitUtils();
    RetrieveData retrieveData = new RetrieveData();

    Calendar myCalendar = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date date = new Date();

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
        datePicker = findViewById(R.id.datePickerET);

        mSwipeRefreshLayout = findViewById(R.id.fitbit_swipe_refresh_layout);

        circleFill = (CircleFillView) findViewById(R.id.stepsProgress);

        // Logic
        datePicker.setText(dateFormat.format(date));
        retrieveData.updateData(FitbitActivity.this);

        datePickerDialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                retrieveData.updateData(FitbitActivity.this);
            }

        };

        datePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(FitbitActivity.this, R.style.TimePickerTheme, datePickerDialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(datePicker.getWindowToken(), 0);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("Date", datePicker.getText().toString());
                //fbUtils.setRequestDate();
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

    private void updateLabel() {

        Calendar today = Calendar.getInstance();
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        String queryFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf2 = new SimpleDateFormat(queryFormat);

        if(myCalendar.after(today)){
            //TODO: Implement a dialog box that says Can't select date grater than today
            datePicker.setText(sdf.format(today.getTime()));
        }
        else if(myCalendar.equals(today)) {
            fbUtils.setRequestDate("today");
        }
        else {
            datePicker.setText(sdf.format(myCalendar.getTime()));
            fbUtils.setRequestDate(sdf2.format(myCalendar.getTime()));
        }
    }



    private void updateView() {

        int steps_percent = 0;
        try {
             steps_percent = fbUtils.getSteps() * 100 / fbUtils.getStepsGoal();
        }
        catch (Exception e) {
            Log.e("StepsGoal", "Steps Goal returned 0");
        }

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

        int totalMinutes = 1;
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