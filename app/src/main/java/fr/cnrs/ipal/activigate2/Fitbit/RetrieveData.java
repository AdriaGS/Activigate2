package fr.cnrs.ipal.activigate2.Fitbit;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.cnrs.ipal.activigate2.Fitbit.API.HeartRate.ActivitiesHeart;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrieveData {

    String heartRateData = "";
    String activityData = "";
    String sleepData = "";

    private String access_token;
    private String token_type;
    private String user_id;
    private String scope;
    private String authorization;
    private long expires_in;

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

    private void getHeartRate() {
        String myUri = "https://api.fitbit.com/1/user/-/activities/heart/date/today/1d.json";
        heartRateData = "";
        new GetDatafromAPI().execute(myUri, authorization, "0");
    }

    private void getActivity() {
        String myUri = "https://api.fitbit.com/1/user/-/activities/date/today.json";
        activityData = "";
        new GetDatafromAPI().execute(myUri, authorization, "1");
    }

    private void getSleep() {
        String myUri = "https://api.fitbit.com/1/user/-/sleep/date/today.json";
        sleepData = "";
        new GetDatafromAPI().execute(myUri, authorization, "2");
    }

    /*public void processResult() {

        if (!heartRateData.equals("") && !activityData.equals("") && !sleepData.equals("")) {
            //pd.dismiss();

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

    }*/

}
