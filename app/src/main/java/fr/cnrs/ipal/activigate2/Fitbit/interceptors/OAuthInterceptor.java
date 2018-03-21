package fr.cnrs.ipal.activigate2.Fitbit.interceptors;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import fr.cnrs.ipal.activigate2.MyApplication;
import fr.cnrs.ipal.activigate2.Fitbit.OAuthToken;

import java.io.IOException;

import fr.cnrs.ipal.activigate2.View.MainActivity;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by adria on 20/3/18.
 */

public class OAuthInterceptor implements Interceptor {

    private static final String TAG = "OAuthInterceptor";
    private String accessToken,accessTokenType;

    @Override
    public Response intercept(Chain chain) throws IOException {
        //find the token
        OAuthToken oauthToken = OAuthToken.Factory.create();
        accessToken = oauthToken.getAccessToken();
        accessTokenType = oauthToken.getTokenType();
        //add it to the request
        Request.Builder builder = chain.request().newBuilder();
        if (!TextUtils.isEmpty(accessToken)&&!TextUtils.isEmpty(accessTokenType)) {
            Log.e(TAG,"In the interceptor adding the header authorization with : " + accessTokenType + " " + accessToken);
            builder.header("Authorization",accessTokenType + " " + accessToken);
        }else{
            Log.e(TAG,"In the interceptor there is a fuck with : " + accessTokenType + " " + accessToken);
            //you should launch the loginActivity to fix that:
            Intent i = new Intent(MyApplication.instance, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            MyApplication.instance.startActivity(i);
        }
        //proceed to the call
        return chain.proceed(builder.build());
    }
}