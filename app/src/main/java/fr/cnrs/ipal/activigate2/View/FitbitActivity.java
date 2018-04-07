package fr.cnrs.ipal.activigate2.View;

import android.app.ProgressDialog;
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

import fr.cnrs.ipal.activigate2.Fitbit.API.HeartRate.ActivitiesHeart;
import fr.cnrs.ipal.activigate2.Fitbit.API.HeartRate.HeartRate;
import fr.cnrs.ipal.activigate2.Fitbit.OAuthServerIntf;
import fr.cnrs.ipal.activigate2.Fitbit.OAuthToken;
import fr.cnrs.ipal.activigate2.Fitbit.RetrofitBuilder;
import fr.cnrs.ipal.activigate2.R;
import fr.cnrs.ipal.activigate2.View.Secondary.CircleFillView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FitbitActivity extends AppCompatActivity {

    private String access_token;
    private String token_type;
    private String user_id;
    private String scope;
    private String authorization;
    private long expires_in;

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
                updateData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private boolean getParameters() {

        OAuthToken oauthToken = OAuthToken.Factory.create();
        access_token = oauthToken.getAccessToken();
        token_type = oauthToken.getTokenType();
        user_id = oauthToken.getUser_id();
        scope = oauthToken.getScope();
        expires_in = oauthToken.getExpiresIn();

        if (user_id != null && access_token != null) {
            authorization = token_type + " " + access_token;
            return true;
        } else {
            return false;
        }

    }

    public void updateData() {

        if (getParameters()) {

            OAuthServerIntf server= RetrofitBuilder.getOAuthClient(this);
            Call<HeartRate> heartRateDataCall = server.getHeartRatedata("-", "today");
            heartRateDataCall.enqueue(new Callback<HeartRate>() {
                @Override
                public void onResponse(Call<HeartRate> call, Response<HeartRate> response) {
                    Log.e("TAG","The call listFilesCall succeed with [code="+response.code()+" and has body = "+response.body()+" and message = "+response.message()+" ]");
                    if(response.isSuccessful()) {
                        HeartRate hR = response.body();
                        Log.d("Response General", hR.getActivitiesHeart().toString());
                        //Log.d("Response Value", hR.getValue() == null ? "no value": hR.getValue().toString());
                    }
                }
                @Override
                public void onFailure(Call<HeartRate> call, Throwable t) {
                    Log.e("TAG","The call listFilesCall failed",t);
                }
            });

        }
        else {
            Log.e("Error", "Some parameters returned null");
        }

    }

    private void updateView() {

        int steps_percent = steps_val*100/stepsGoal;

        steps.setText("Steps\n" + String.valueOf(steps_val));
        circleFill.setValue(steps_percent);

        lightlyActive.setText(String.valueOf(String.format("%02d:%02d H",
                TimeUnit.MINUTES.toHours(lightlyActiveMin_val),
                lightlyActiveMin_val - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(lightlyActiveMin_val)))));
        sedentaryMin.setText(String.valueOf(String.format("%02d:%02d H",
                TimeUnit.MINUTES.toHours(sedentaryMin_val),
                sedentaryMin_val - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(sedentaryMin_val)))));
        veryActive.setText(String.valueOf(String.format("%02d:%02d H",
                TimeUnit.MINUTES.toHours(veryActiveMin_val),
                veryActiveMin_val - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(veryActiveMin_val)))));

        int totalMinutes = 0;
        for(int i = 0; i < minutesZones.size(); i++) {
            totalMinutes += minutesZones.get(i);
        }

        restingHeartRate.setText(String.valueOf(restingHeartRate_val) + " bpm");
        outOfRangeZone.setText(String.valueOf(minutesZones.get(0)*100/totalMinutes) + "%");
        fatBurnZone.setText(String.valueOf(minutesZones.get(1)*100/totalMinutes) + "%");
        cardioZone.setText(String.valueOf(minutesZones.get(2)*100/totalMinutes) + "%");
        peakZone.setText(String.valueOf(minutesZones.get(3)*100/totalMinutes) + "%");

        sleepDuration.setText(String.format("%02d:%02d hours",
                TimeUnit.MILLISECONDS.toHours(sleepDuration_val),
                TimeUnit.MILLISECONDS.toMinutes(sleepDuration_val) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(sleepDuration_val))
        ));
        sleepEfficiency.setText(String.valueOf(sleepEfficiency_val) + "%");
        awakeningsCount.setText(String.valueOf(awakeningsCount_val));

        mSwipeRefreshLayout.setRefreshing(false);

    }
}