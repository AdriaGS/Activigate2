package fr.cnrs.ipal.activigate2.HAR;

import android.content.SharedPreferences;
import android.util.Log;

import java.sql.Timestamp;
import java.util.ArrayList;

import fr.cnrs.ipal.activigate2.Logger.Logger;
import fr.cnrs.ipal.activigate2.MyApplication;

/**
 * Created by adria on 16/3/18.
 */

public class Utils {

    private static final String HAR_PREFERENCES = "HAR_Preferences";
    static Logger logger = new Logger();

    public static ArrayList<String> json2Send = new ArrayList<>();
    public static ArrayList<String> lastJson = new ArrayList<>();
    public static String houseID = "";

    public static ArrayList<String> activitiesHistory = new ArrayList<>();

    public static Boolean isSensing = false;
    public static Boolean canSend = true;
    public static Boolean threadRunning = false;
    public static String lastSensedActivity = "";
    public static String lastIncludedActivity = "";
    public static long lastTimeStamp = 0;

    public static final int HOUR_PER_DAY = 24;
    public static final int MINUTES_PER_HOUR = 60;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static int DETECTION_INTERVAL_SECONDS = 0;
    public static final int DETECTION_INTERVAL_MILLISECONDS = MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;

    public static final int MILIS_PER_DAY = MILLISECONDS_PER_SECOND*SECONDS_PER_MINUTE*MINUTES_PER_HOUR*HOUR_PER_DAY;

    public static String getLastSensedActivity() { return lastSensedActivity; }

    public static void setLastSensedActivity(String activity) {
        Utils.lastSensedActivity = activity;
    }

    public static void setHouseID(String houseID) {
        Utils.houseID = houseID;
    }

    public static String getHouseID() {
        return houseID;
    }

    public static void setLastJson(ArrayList<String> lastJson) {
        Utils.lastJson = lastJson;
    }

    public static void setIsSensing(Boolean newValue) {
        Utils.isSensing = newValue;
    }

    public static boolean getIsSensing() {
        return isSensing;
    }

    public static Boolean getThreadRunning() {
        return threadRunning;
    }

    public static void setThreadRunning(Boolean threadRunning) {
        Utils.threadRunning = threadRunning;
    }

    public static void setJson2Send(ArrayList<String> json2Save) { Utils.json2Send = json2Save; }

    public static ArrayList<String> getJson2Send() {
        return json2Send;
    }

    public static void setActivitiesHistory(ArrayList<String> activitiesHistory) {
        Utils.activitiesHistory = activitiesHistory;
    }

    public static ArrayList<String> getActivitiesHistory() {
        return activitiesHistory;
    }

    public static Boolean getCanSend() {
        return canSend;
    }

    public static void setCanSend(Boolean canSend) {
        Utils.canSend = canSend;
    }

    private void save() {
        SharedPreferences sharedPreferences = MyApplication.instance.getSharedPreferences(HAR_PREFERENCES, MyApplication.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("json2Send", array2String(json2Send));
        editor.putString("activitiesHistory", array2String(activitiesHistory));
        editor.commit();
        logger.appendLog("Saved variables in SharedPreferences\n");
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

    public void add2History(String date, String activity) {

        String toSave = date + ":" + activity;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long currentDate = timestamp.getTime();

        for(int i = 0; i < activitiesHistory.size(); i++) {
            String[] tmp = activitiesHistory.get(i).split(":");
            if(Long.valueOf(tmp[0]) + MILIS_PER_DAY < currentDate) {
                activitiesHistory.remove(i);
            }
            else {
                break;
            }
        }
        activitiesHistory.add(toSave);

    }

    public String array2String(ArrayList<String> json2Send) {
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
