package fr.cnrs.ipal.activigate2.View;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import fr.cnrs.ipal.activigate2.Fitbit.OAuthToken;
import fr.cnrs.ipal.activigate2.R;
import fr.cnrs.ipal.activigate2.View.ViewUtils.CircleFillView;

public class FitbitActivity extends AppCompatActivity {

    private String access_token;
    private String token_type;
    private String user_id;
    private String scope;
    private String authorization;
    private long expires_in;

    String heartRateData = "";
    String activityData = "";
    String sleepData = "";

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

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        day.setText("TODAY - " + dateFormat.format(date));

        circleFill = (CircleFillView) findViewById(R.id.stepsProgress);

        updateData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.updateData:
                updateData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getHeartRate() {
        String myUri = "https://api.fitbit.com/1/user/-/activities/heart/date/today/1d.json";
        heartRateData = "";
        new getDatafromAPI().execute(myUri, authorization, "0");
    }

    private void getActivity() {
        String myUri = "https://api.fitbit.com/1/user/-/activities/date/today.json";
        activityData = "";
        new getDatafromAPI().execute(myUri, authorization, "1");
    }

    private void getSleep() {
        String myUri = "https://api.fitbit.com/1/user/-/sleep/date/today.json";
        sleepData = "";
        new getDatafromAPI().execute(myUri, authorization, "2");
    }

    public void updateData() {

        if (getParameters()) {
            pd = new ProgressDialog(this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();

            getHeartRate();
            getActivity();
            getSleep();
        }
        else {
            Log.e("Error", "Some parameters returned null");
        }

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

    public void processResult() {

        if (!heartRateData.equals("") && !activityData.equals("") && !sleepData.equals("") && pd.isShowing()) {
            pd.dismiss();

            try {
                JSONObject activityJson = new JSONObject(activityData).getJSONObject("summary");
                JSONArray heartZones = activityJson.getJSONArray("heartRateZones");

                minutesZones = new ArrayList<>();
                for(int i = 0; i < heartZones.length(); i++) {
                    String zone = heartZones.getString(i);
                    minutesZones.add(new JSONObject(zone).getInt("minutes"));
                }

                try {
                    restingHeartRate_val = activityJson.getInt("restingHeartRate");
                }
                catch (JSONException e) {
                    Log.e("No Resting HR", "No Resting Heart Rate");
                }
                sedentaryMin_val = activityJson.getInt("sedentaryMinutes");
                lightlyActiveMin_val = activityJson.getInt("lightlyActiveMinutes");
                veryActiveMin_val = activityJson.getInt("veryActiveMinutes");
                steps_val = activityJson.getInt("steps");
                stepsGoal = new JSONObject(activityData).getJSONObject("goals").getInt("steps");


                JSONObject sleepJson = new JSONObject(sleepData);
                String sleep = sleepJson.getJSONArray("sleep").getString(0);
                sleepJson = new JSONObject(sleep);
                sleepDuration_val = sleepJson.getInt("duration");
                sleepEfficiency_val = sleepJson.getInt("efficiency");
                awakeningsCount_val = sleepJson.getInt("awakeningsCount");

                updateView();


            } catch (JSONException e) {
                Log.e("JSON Exception", e.toString());
            }

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

    }


    private class getDatafromAPI extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", urls[1]);
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.d("Response: ", "> " + line);

                }
                switch (Integer.parseInt(urls[2])) {
                    case 0:
                        heartRateData = buffer.toString();
                        return buffer.toString();
                    case 1:
                        activityData = buffer.toString();
                        return buffer.toString();
                    case 2:
                        sleepData = buffer.toString();
                        return buffer.toString();
                    default:
                        return null;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // this is executed on the main thread after the process is over
            // update your UI here
            if(result == null) {
                Log.e("Error", "Error on data reception");
            }
            processResult();
        }

    }
}