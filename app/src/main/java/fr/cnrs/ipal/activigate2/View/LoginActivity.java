package fr.cnrs.ipal.activigate2.View;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import fr.cnrs.ipal.activigate2.Fitbit.OAuthToken;
import fr.cnrs.ipal.activigate2.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private static final String CLIENT_ID = "22CVHY";
    private static final String REDIRECT_URI = "activigate://callback";
    private static final String REDIRECT_URI_ROOT = "activigate";

    private String access_token;
    private String token_type;
    private String user_id;
    private String scope;
    private String expires_in;

    private OAuthToken oAuthToken = new OAuthToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Manage the callback case:
        Uri data = getIntent().getData();
        if (data != null && !TextUtils.isEmpty(data.getScheme())) {
            Log.d("URI", data.toString());
            if (REDIRECT_URI_ROOT.equals(data.getScheme())) {
                    access_token = getAccessToken(data);
                    String[] dataList = data.toString().split("&");
                    user_id = dataList[1].substring(dataList[1].indexOf("=") + 1);
                    scope = dataList[2].substring(dataList[2].indexOf("=") + 1);
                    scope = scope.replace("+", " ");
                    token_type = dataList[3].substring(dataList[3].indexOf("=") + 1);
                    expires_in = dataList[4].substring(dataList[4].indexOf("=") + 1);
                    Log.d("Token", access_token);
                if(data.toString().contains("error")) {
                    //a problem occurs, the user reject our granting request or something like that
                    Toast.makeText(this, R.string.loginactivity_grantsfails_quit,Toast.LENGTH_LONG).show();
                    Log.e(TAG, "onCreate: handle result of authorization with error");
                    //then die
                    finish();
                }
                Log.e("Expires in", expires_in);
                oAuthToken.setTokenType(token_type);
                oAuthToken.setAccessToken(access_token);
                oAuthToken.setScope(scope);
                oAuthToken.setExpiredAfterMilli(Integer.parseInt(expires_in) * 1000);
                oAuthToken.setUser_id(user_id);
                oAuthToken.save();
                startFitbitActivity(false);
            }
        } else {
            //Manage the start application case:
            //If you don't have a token yet or if your token has expired , ask for it
            OAuthToken oauthToken = OAuthToken.Factory.create();
            if (oauthToken == null || oauthToken.getAccessToken() == null) {
                Log.e(TAG, "onCreate: Launching authorization (first step)");
                //first step of OAUth: the authorization step
                makeAuthorizationRequest();
            }
            //else just launch your FitbitActivity
            else {
                Log.e(TAG, "onCreate: Token available, just launch FitbitActivity");
                startFitbitActivity(false);
            }
        }
    }

    public void makeAuthorizationRequest() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.fitbit.com/oauth2/authorize?response_type=token" +
                "&client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&scope=activity%20heartrate%20sleep&expires_in=604800"));
        startActivity(intent);
        finish();

    }

    private String getAccessToken(Uri data) {
        String str = data.toString();
        int indexOfHash = str.indexOf("#");
        String subStr = str.substring(indexOfHash+1, str.length());
        String sStr = subStr.substring(0,subStr.indexOf("&"));
        String ssStr = subStr.substring(sStr.indexOf("=")+1, sStr.length());
        return ssStr;
    }

    private void startFitbitActivity(boolean newtask) {
        Intent i = new Intent(this, FitbitActivity.class);
        if(newtask){
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        startActivity(i);
        //you can die so
        finish();
    }
}
