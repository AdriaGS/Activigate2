package fr.cnrs.ipal.activigate2.Fitbit;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by adria on 20/3/18.
 */

public class getHeartRateTest extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", urls[1]);
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String content = "", line;
            while ((line = rd.readLine()) != null) {
                content += line + "\n";
            }
            return content;
        }
        catch (IOException e) {
            Log.d("Error", e.toString());
            return null;
        }
    }
    @Override
    protected void onPostExecute(String result) {
        // this is executed on the main thread after the process is over
        // update your UI here
        Log.d("Result", result);
    }

}
