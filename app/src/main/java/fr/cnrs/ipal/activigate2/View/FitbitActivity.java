package fr.cnrs.ipal.activigate2.View;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import fr.cnrs.ipal.activigate2.Fitbit.OAuthToken;
import fr.cnrs.ipal.activigate2.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitbit);
    }

    private void getHeartRate() {
        String myUri = "https://api.fitbit.com/1/user/-/activities/heart/date/today/1d.json";
        new getDatafromAPI().execute(myUri, authorization, "0");
    }

    private void getActivity() {
        String myUri = "https://api.fitbit.com/1/user/-/activities/date/today.json";
        new getDatafromAPI().execute(myUri, authorization, "1");
    }

    private void getSleep() {
        String myUri = "https://api.fitbit.com/1/user/-/sleep/date/today.json";
        new getDatafromAPI().execute(myUri, authorization, "2");
    }

    public void updateData(View view) {

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

    public void updateDataView(String data) {

        if (pd.isShowing()){
            pd.dismiss();
        }

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
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

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
            updateDataView(result);
        }

    }
}