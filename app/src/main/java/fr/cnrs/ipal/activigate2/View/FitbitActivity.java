package fr.cnrs.ipal.activigate2.View;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fr.cnrs.ipal.activigate2.Fitbit.OAuthToken;
import fr.cnrs.ipal.activigate2.Fitbit.getHeartRateTest;
import fr.cnrs.ipal.activigate2.HAR.HttpAsyncTask;
import fr.cnrs.ipal.activigate2.R;

public class FitbitActivity extends AppCompatActivity {

    private String access_token;
    private String token_type;
    private String user_id;
    private String scope;
    private long expires_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitbit);
    }

    public void getHeartRate(View view) {

        OAuthToken oauthToken = OAuthToken.Factory.create();
        access_token = oauthToken.getAccessToken();
        token_type = oauthToken.getTokenType();
        user_id = oauthToken.getUser_id();
        scope = oauthToken.getScope();
        expires_in = oauthToken.getExpiresIn();

        String myUri = "https://api.fitbit.com/1/user/" + user_id + "/activities/heart/date/today/1d.json";
        String authorization = token_type + " " + access_token;

        new getHeartRateTest().execute(myUri, authorization);

    }
}
