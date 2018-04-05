package fr.cnrs.ipal.activigate2.View;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import fr.cnrs.ipal.activigate2.HAR.Utils;
import fr.cnrs.ipal.activigate2.R;

public class HistoricActivity extends AppCompatActivity {

    Utils utils = new Utils();

    // TODO: define global variables for activities

    private static final String walking = "Walking";
    private static final String running = "Running";
    private static final String bike = "OnBicycle";
    private static final String vehicle = "InVehicle";
    private static final String still = "Still";
    private static final String tilting = "Tilting";
    private static final String unknown = "Unknown";

    ArrayList<String> historic = new ArrayList<>();
    HashMap<String, Long> hoursXActivity;

    TextView walkingTV;
    TextView runningTV;
    TextView bikeTV;
    TextView vehicleTV;
    TextView stillTV;
    TextView tiltingTV;
    TextView unknownTV;
    TextView lastUpdate;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);

        walkingTV = findViewById(R.id.walkingTV);
        runningTV = findViewById(R.id.runningTV);
        bikeTV = findViewById(R.id.bikeTV);
        vehicleTV = findViewById(R.id.vehicleTV);
        stillTV = findViewById(R.id.stillTV);
        tiltingTV = findViewById(R.id.tiltingTV);
        unknownTV = findViewById(R.id.unknownTV);
        lastUpdate = findViewById(R.id.lastUpdated);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.historic_swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                 computeValues();
             }
        });

        historic = utils.getActivitiesHistory();
        computeValues();

    }

    private void updateView(long currentDate){

        walkingTV.setText(String.format("%02d:%02d hours",
                TimeUnit.MILLISECONDS.toHours(hoursXActivity.get(walking)),
                TimeUnit.MILLISECONDS.toMinutes(hoursXActivity.get(walking)) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hoursXActivity.get(walking)))
        ));

        runningTV.setText(String.format("%02d:%02d hours",
                TimeUnit.MILLISECONDS.toHours(hoursXActivity.get(running)),
                TimeUnit.MILLISECONDS.toMinutes(hoursXActivity.get(running)) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hoursXActivity.get(running)))
        ));

        bikeTV.setText(String.format("%02d:%02d hours",
                TimeUnit.MILLISECONDS.toHours(hoursXActivity.get(bike)),
                TimeUnit.MILLISECONDS.toMinutes(hoursXActivity.get(bike)) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hoursXActivity.get(bike)))
        ));

        vehicleTV.setText(String.format("%02d:%02d hours",
                TimeUnit.MILLISECONDS.toHours(hoursXActivity.get(vehicle)),
                TimeUnit.MILLISECONDS.toMinutes(hoursXActivity.get(vehicle)) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hoursXActivity.get(vehicle)))
        ));

        stillTV.setText(String.format("%02d:%02d hours",
                TimeUnit.MILLISECONDS.toHours(hoursXActivity.get(still)),
                TimeUnit.MILLISECONDS.toMinutes(hoursXActivity.get(still)) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hoursXActivity.get(still)))
        ));

        tiltingTV.setText(String.format("%02d:%02d hours",
                TimeUnit.MILLISECONDS.toHours(hoursXActivity.get(tilting)),
                TimeUnit.MILLISECONDS.toMinutes(hoursXActivity.get(tilting)) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hoursXActivity.get(tilting)))
        ));

        unknownTV.setText(String.format("%02d:%02d hours",
                TimeUnit.MILLISECONDS.toHours(hoursXActivity.get(unknown)),
                TimeUnit.MILLISECONDS.toMinutes(hoursXActivity.get(unknown)) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hoursXActivity.get(unknown)))
        ));

        lastUpdate.setText("Last Updated: " + getDate(currentDate, "dd/MM/yyyy hh:mm:ss"));

    }

    private void computeValues() {

        initializeMap();
        long accumulatedTime = 0;

        String[] tmp = historic.get(0).split(":");
        String lastActivity = tmp[1];
        long lastActivityTime = Long.valueOf(tmp[0]);

        Log.d("First Activity", lastActivity);
        Log.d("First Time", getDate(lastActivityTime,"dd/MM/yyyy hh:mm:ss"));

        for(int i = 1; i < historic.size(); i++) {
            tmp = historic.get(i).split(":");
            accumulatedTime += (Long.valueOf(tmp[0]) - lastActivityTime);
            if(lastActivity.equals("InVehicle") && tmp[1].equals("Still")){
                if(i < historic.size()-1) {
                    String[] nextTmp = historic.get(i + 1).split(":");
                    if(nextTmp[1].equals("InVehicle")) {
                        tmp[1] = "InVehicle";
                    }
                    else if(nextTmp[1].equals("Still")){
                        tmp[1] = "InVehicle";
                    }
                }
            }
            if(!tmp[1].equals(lastActivity)) {
                accumulatedTime += hoursXActivity.get(lastActivity);
                hoursXActivity.put(lastActivity, accumulatedTime);
                lastActivity = tmp[1];
                accumulatedTime = 0;
            }
            lastActivityTime = Long.valueOf(tmp[0]);

        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long currentDate = timestamp.getTime();
        updateView(currentDate);

        mSwipeRefreshLayout.setRefreshing(false);

    }

    private void initializeMap() {
        hoursXActivity = new HashMap<String, Long>(){{
            put(walking, Long.valueOf("0"));
            put(running, Long.valueOf("0"));
            put(bike, Long.valueOf("0"));
            put(vehicle, Long.valueOf("0"));
            put(still, Long.valueOf("0"));
            put(tilting, Long.valueOf("0"));
            put(unknown, Long.valueOf("0"));

        }};
    }

    private static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
