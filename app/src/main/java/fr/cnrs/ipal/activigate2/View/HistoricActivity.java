package fr.cnrs.ipal.activigate2.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import fr.cnrs.ipal.activigate2.HAR.HARUtils;
import fr.cnrs.ipal.activigate2.R;

public class HistoricActivity extends AppCompatActivity {

    HARUtils harUtils = new HARUtils();

    // TODO: define global variables for activities

    ArrayList<String> historic = new ArrayList<>();
    HashMap<String, Long> hoursXActivity = new HashMap<String, Long>(){{
        put("Walking", Long.valueOf("0"));
        put("Running", Long.valueOf("0"));
        put("OnBicycle", Long.valueOf("0"));
        put("InVehicle", Long.valueOf("0"));
        put("Still", Long.valueOf("0"));
        put("Tilting", Long.valueOf("0"));
        put("Unknown", Long.valueOf("0"));

    }};

    TextView walkingTV;
    TextView runningTV;
    TextView bikeTV;
    TextView vehicleTV;
    TextView stillTV;
    TextView tiltingTV;
    TextView unknownTV;

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


        historic = harUtils.getActivitiesHistory();
        computeValues();

    }

    private void updateView(){

        walkingTV.setText(String.format("%02d:%02d hours",
                TimeUnit.MILLISECONDS.toHours(hoursXActivity.get("Walking")),
                TimeUnit.MILLISECONDS.toMinutes(hoursXActivity.get("Walking")) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hoursXActivity.get("Walking")))
        ));

        runningTV.setText(String.format("%02d:%02d hours",
                TimeUnit.MILLISECONDS.toHours(hoursXActivity.get("Running")),
                TimeUnit.MILLISECONDS.toMinutes(hoursXActivity.get("Running")) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hoursXActivity.get("Running")))
        ));

        bikeTV.setText(String.format("%02d:%02d hours",
                TimeUnit.MILLISECONDS.toHours(hoursXActivity.get("OnBicycle")),
                TimeUnit.MILLISECONDS.toMinutes(hoursXActivity.get("OnBicycle")) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hoursXActivity.get("OnBicycle")))
        ));

        vehicleTV.setText(String.format("%02d:%02d hours",
                TimeUnit.MILLISECONDS.toHours(hoursXActivity.get("InVehicle")),
                TimeUnit.MILLISECONDS.toMinutes(hoursXActivity.get("InVehicle")) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hoursXActivity.get("InVehicle")))
        ));

        stillTV.setText(String.format("%02d:%02d hours",
                TimeUnit.MILLISECONDS.toHours(hoursXActivity.get("Still")),
                TimeUnit.MILLISECONDS.toMinutes(hoursXActivity.get("Still")) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hoursXActivity.get("Still")))
        ));

        tiltingTV.setText(String.format("%02d:%02d hours",
                TimeUnit.MILLISECONDS.toHours(hoursXActivity.get("Tilting")),
                TimeUnit.MILLISECONDS.toMinutes(hoursXActivity.get("Tilting")) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hoursXActivity.get("Tilting")))
        ));

        unknownTV.setText(String.format("%02d:%02d hours",
                TimeUnit.MILLISECONDS.toHours(hoursXActivity.get("Unknown")),
                TimeUnit.MILLISECONDS.toMinutes(hoursXActivity.get("Unknown")) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hoursXActivity.get("Unknown")))
        ));

    }

    private void computeValues() {

        long accumulatedTime = 0;

        String[] tmp = historic.get(0).split(":");
        String lastActivity = tmp[1];
        long lastActivityTime = Long.valueOf(tmp[0]);

        for(int i = 1; i < historic.size(); i++) {
            tmp = historic.get(i).split(":");
            accumulatedTime += (Long.valueOf(tmp[0]) - lastActivityTime);
            if(!tmp[1].equals(lastActivity)) {
                lastActivity = tmp[1];
                accumulatedTime += hoursXActivity.get(tmp[1]);
                hoursXActivity.put(tmp[1], accumulatedTime);
                accumulatedTime = 0;
            }
            lastActivityTime = Long.valueOf(tmp[0]);

        }
        updateView();

    }
}
