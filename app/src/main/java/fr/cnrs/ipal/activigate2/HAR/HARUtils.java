package fr.cnrs.ipal.activigate2.HAR;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.cnrs.ipal.activigate2.MyApplication;

/**
 * Created by adria on 16/3/18.
 */

public class HARUtils {

    private static final String HAR_PREFERENCES = "HAR_Preferences";

    public static ArrayList<String> json2Send = new ArrayList<>();
    public static ArrayList<String> lastJson = new ArrayList<>();

    public static Boolean isSensing = false;
    public static Boolean canSend = true;
    public static Boolean threadRunning = false;
    public static String lastSensedActivity = "";
    public static String lastIncludedActivity = "";
    public static long lastTimeStamp = 0;

    public static final int SECONDS_PER_MINUTE = 60;
    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static int DETECTION_INTERVAL_SECONDS = 0;
    public static final int DETECTION_INTERVAL_MILLISECONDS = MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;

    public static String getLastSensedActivity() { return lastSensedActivity; }

    public static void setLastSensedActivity(String activity) {
        HARUtils.lastSensedActivity = activity;
    }

    public static void setLastJson(ArrayList<String> lastJson) {
        HARUtils.lastJson = lastJson;
    }

    public static void setIsSensing(Boolean newValue) {
        HARUtils.isSensing = newValue;
    }

    public static boolean getIsSensing() {
        return isSensing;
    }

    public static Boolean getThreadRunning() {
        return threadRunning;
    }

    public static void setThreadRunning(Boolean threadRunning) {
        HARUtils.threadRunning = threadRunning;
    }

    public static void setJson2Send(ArrayList<String> json2Save) { HARUtils.json2Send = json2Save; }

    public static ArrayList<String> getJson2Send() {
        return json2Send;
    }

    public static Boolean getCanSend() {
        return canSend;
    }

    public static void setCanSend(Boolean canSend) {
        HARUtils.canSend = canSend;
    }

    private void save() {
        SharedPreferences sharedPreferences = MyApplication.instance.getSharedPreferences(HAR_PREFERENCES, MyApplication.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("json2Send", array2String(json2Send));
        editor.commit();
    }

    public void onSending(Boolean sent) {

        Long lastJsonTimeStamp = Long.valueOf(lastJson.get(0));
        if(sent) {
            lastTimeStamp = lastJsonTimeStamp;
        }
        else {
            if(lastSensedActivity.equals(lastIncludedActivity)) {
                if (lastJsonTimeStamp > lastTimeStamp + SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND) {
                    json2Send.add(lastJson.get(1));
                    save();
                    lastTimeStamp = lastJsonTimeStamp;
                    Log.d("Added to array", array2String(json2Send));
                }
            }
            else {
                json2Send.add(lastJson.get(1));
                save();
                lastIncludedActivity = lastSensedActivity;
                lastTimeStamp = lastJsonTimeStamp;
                Log.d("Added to array", array2String(json2Send));
            }
        }
    }

    private String array2String(ArrayList<String> json2Send) {
        String jsonRecord = "";
        for (String temp : json2Send)
        {
            jsonRecord = jsonRecord + temp + "&";
        }
        return jsonRecord;
    }

    public ArrayList<String> string2Array(String jsonRecord) {
        ArrayList<String> jsonArray = new ArrayList<>();
        if(jsonRecord != null) {
            String[] str = jsonRecord.split("&");
            for (String tmp : str) {
                jsonArray.add(tmp);
            }
        }
        return jsonArray;
    }

}
