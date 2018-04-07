package fr.cnrs.ipal.activigate2.Fitbit;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetDatafromAPI extends AsyncTask<String, String, String> {

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
            /*switch (Integer.parseInt(urls[2])) {
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
            }*/

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
        //processResult();
    }

}
