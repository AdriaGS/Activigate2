package fr.cnrs.ipal.activigate2.Fitbit;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.cnrs.ipal.activigate2.Fitbit.API.Activities.Activities;
import fr.cnrs.ipal.activigate2.Fitbit.API.HeartRate.ActivitiesHeart;
import fr.cnrs.ipal.activigate2.Fitbit.API.HeartRate.HeartRate;
import fr.cnrs.ipal.activigate2.Fitbit.API.Sleep.SleepData;
import fr.cnrs.ipal.activigate2.MyApplication;
import fr.cnrs.ipal.activigate2.View.FitbitActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrieveData {

    FitbitUtils fbUtils = new FitbitUtils();

    public static String LOCAL_BROADCAST_NAME = "LOCAL_ACT_RECOGNITION";
    public static String LOCAL_BROADCAST_EXTRA = "RESULT";

    private String access_token;
    private String token_type;
    private String user_id;
    private String scope;
    private String authorization;
    private long expires_in;

    static boolean hrReceived = false;
    static boolean actReceived = false;
    static boolean sleepReceived = false;

    public void updateData(Context ctx) {
        if (getParameters()) {
            // Heart Rate Data
            OAuthServerIntf server= RetrofitBuilder.getOAuthClient(ctx);
            Call<HeartRate> heartRateDataCall = server.getHeartRatedata(fbUtils.getRequestUser(), fbUtils.getRequestDate());
            heartRateDataCall.enqueue(new Callback<HeartRate>() {
                @Override
                public void onResponse(Call<HeartRate> call, Response<HeartRate> response) {
                    Log.e("TAG","The call for heart rate data succeed with [code="+response.code()+" and has body = "+response.body()+" and message = "+response.message()+" ]");
                    if(response.isSuccessful()) {
                        HeartRate hR = response.body();
                        fbUtils.setMinutesZones(hR.getActivitiesHeart().get(0).getValue().getMinutesZones());
                        fbUtils.setRestingHeartRate(hR.getActivitiesHeart().get(0).getValue().getRestingHeartRate());
                        hrReceived = true;
                        canUpdateView();
                    }
                }
                @Override
                public void onFailure(Call<HeartRate> call, Throwable t) {
                    Log.e("TAG","The call for heart rate data failed",t);
                }
            });

            //Activity Data
            Call<Activities> activitiesDataCall = server.getActivitiesData(fbUtils.getRequestUser(), fbUtils.getRequestDate());
            activitiesDataCall.enqueue(new Callback<Activities>() {
                @Override
                public void onResponse(Call<Activities> call, Response<Activities> response) {
                    Log.e("TAG","The call for activity data succeed with [code="+response.code()+" and has body = "+response.body()+" and message = "+response.message()+" ]");
                    if(response.isSuccessful()) {
                        Activities actiData = response.body();
                        fbUtils.setSteps(actiData.getSummary().getSteps());
                        fbUtils.setStepsGoal(actiData.getGoals().getSteps());
                        fbUtils.setSedentaryMin(actiData.getSummary().getSedentaryMinutes());
                        fbUtils.setLightlyActiveMin(actiData.getSummary().getLightlyActiveMinutes());
                        fbUtils.setVeryActiveMin(actiData.getSummary().getVeryActiveMinutes());
                        actReceived = true;
                        canUpdateView();
                    }
                }
                @Override
                public void onFailure(Call<Activities> call, Throwable t) {
                    Log.e("TAG","The call for activity data failed",t);
                }
            });

            //Sleep Data
            Call<SleepData> sleepDataCall = server.getSleepData(fbUtils.getRequestUser(), fbUtils.getRequestDate());
            sleepDataCall.enqueue(new Callback<SleepData>() {
                @Override
                public void onResponse(Call<SleepData> call, Response<SleepData> response) {
                    Log.e("TAG", "The call for sleep data succeed with [code=" + response.code() + " and has body = " + response.body() + " and message = " + response.message() + " ]");
                    if (response.isSuccessful()) {
                        SleepData sleep = response.body();
                        try{
                            fbUtils.setSleepDuration(sleep.getMainSleep().getDuration());
                            fbUtils.setSleepEfficiency(sleep.getMainSleep().getEfficiency());
                            fbUtils.setAwakeningsCount(sleep.getMainSleep().getAwakeCount());
                        }
                        catch (Exception e) {
                            Log.e("getMainSleep", "No main sleep returned");
                            fbUtils.setSleepDuration(0);
                            fbUtils.setSleepEfficiency(0);
                            fbUtils.setAwakeningsCount(0);
                        }
                        sleepReceived = true;
                        canUpdateView();
                    }
                }

                @Override
                public void onFailure(Call<SleepData> call, Throwable t) {
                    Log.e("TAG", "The call for sleep data failed", t);
                }
            });
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

    private static void canUpdateView() {
        if(hrReceived && actReceived && sleepReceived) {
            hrReceived = false;
            actReceived = false;
            sleepReceived = false;
            Intent broadcastIntent = new Intent(LOCAL_BROADCAST_NAME);
            broadcastIntent.putExtra(LOCAL_BROADCAST_EXTRA, FitbitActivity.class);
            LocalBroadcastManager.getInstance(MyApplication.instance).sendBroadcast(broadcastIntent);
        }
    }

}
