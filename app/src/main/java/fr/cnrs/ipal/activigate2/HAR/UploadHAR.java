package fr.cnrs.ipal.activigate2.HAR;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.HashMap;
import java.io.InputStream;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import fr.cnrs.ipal.activigate2.MyApplication;

/**
 * Created by adriagil on 6/3/18.
 */

public class UploadHAR {

    Utils utils = new Utils();

    static String serverURL = "https://icost.ubismart.org/mobility/store";
    String houseID   = utils.getHouseID();

    public void export2Server(String str) {

        ArrayList<String> json = createJSON(str, houseID);
        utils.setLastJson(json);
        utils.add2History(json.get(0), str);

        if(isConnected()) {
            try {
                new SendCurrent().execute(json.get(1), serverURL);
            }
            catch (Exception e){
                Toast.makeText(MyApplication.instance, "Error on sending Data", Toast.LENGTH_SHORT).show();
            }

            final Thread thread = new Thread() {
                @Override
                public void run() {
                    utils.setThreadRunning(true);
                    ArrayList<String> jsonRecord = utils.getJson2Send();
                    while (utils.getJson2Send().size() > 0) {
                        if (utils.getCanSend()) {
                            try {
                                new SendBuffer().execute(jsonRecord.get(utils.getJson2Send().size() - 1), serverURL);
                                utils.setCanSend(false);
                            }
                            catch (Exception e){
                                Toast.makeText(MyApplication.instance, "Error on sending Data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    utils.setThreadRunning(false);
                    return;
                }
            };
            if(!utils.getThreadRunning()) {
                thread.start();
            }
        }
        else {
            utils.onSending(false);
        }
    }

    private ArrayList<String> createJSON(String activity, String houseId) {

        ArrayList<String> returningArray = new ArrayList<>();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String date = String.valueOf(timestamp.getTime());

        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("house", houseId);
        dataMap.put("date", date);
        dataMap.put("mobility", activity);

        ArrayList<HashMap> list = new ArrayList<>();
        list.add(dataMap);

        HashMap<String, ArrayList> listMap = new HashMap<>();
        listMap.put("list", list);

        HashMap<String, HashMap> payload = new HashMap<>();
        payload.put("data", listMap);

        Gson gson = new Gson();
        String json = gson.toJson(payload);

        returningArray.add(date);
        returningArray.add(json);

        return returningArray;
    }

    public static String upload(String json, String serverUrl) {

        InputStream inputStream = null;
        String result = "";

        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(serverUrl);

            // 3. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 4. set httpPost Entity
            httpPost.setEntity(se);

            // 5. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 6. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 7. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            //Log.d("JSON Upload Result", convertInputStreamToString(inputStream));
            return convertInputStreamToString(inputStream);

        } catch (Exception e) {
            Log.d("InputStream", e.toString());
            return "Null";
        }
    }

    private boolean isConnected() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) MyApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}