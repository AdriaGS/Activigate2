package fr.cnrs.ipal.activigate2.Fitbit;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import fr.cnrs.ipal.activigate2.MyApplication;
import com.squareup.moshi.Json;

/**
 * Created by adria on 20/3/18.
 */

public class OAuthToken {
    private static final String TAG = "OAuthToken";
    /***********************************************************
     * Constants
     **********************************************************/
    private static final String OAUTH_SHARED_PREFERENCE_NAME = "OAuthPrefs";
    private static final String SP_TOKEN_KEY = "token";
    private static final String SP_USER_ID = "user_id";
    private static final String SP_SCOPE = "scope";
    private static final String SP_TOKEN_TYPE_KEY = "token_type";
    private static final String SP_TOKEN_EXPIRED_AFTER_KEY = "expired_after";

    /***********************************************************
     * Attributes
     **********************************************************/
    @Json(name = "access_token")
    private String accessToken;
    @Json(name = "token_type")
    private String tokenType;
    @Json(name = "expires_in")
    private long expiresIn;
    @Json(name = "user_id")
    private String user_id;
    @Json(name = "scope")
    private String scope;
    private long expiredAfterMilli = 0;

    /***********************************************************
     * Managing Persistence
     **********************************************************/
    public void save() {
        Log.e(TAG, "Savng the following element " + this);
        //update expired_after
        expiredAfterMilli = System.currentTimeMillis() + expiresIn * 1000;
        Log.e(TAG, "Savng the following element and expiredAfterMilli =" + expiredAfterMilli+" where now="+System.currentTimeMillis()+" and expired in ="+ expiresIn);
        SharedPreferences sp = MyApplication.instance.getSharedPreferences(OAUTH_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(SP_TOKEN_KEY, accessToken);
        ed.putString(SP_TOKEN_TYPE_KEY, tokenType);
        ed.putString(SP_USER_ID, user_id);
        ed.putString(SP_SCOPE, scope);
        ed.putLong(SP_TOKEN_EXPIRED_AFTER_KEY, expiredAfterMilli);
        ed.commit();
    }

    /***********************************************************
     * Getters and Setters
     **********************************************************/
    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getUser_id() { return user_id; }

    public String getScope() { return scope; }

   public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setExpiredAfterMilli(long expiredAfterMilli) {
        this.expiredAfterMilli = expiredAfterMilli;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setUser_id(String user_id) { this.user_id = user_id; }

    public void setScope(String scope) { this.scope = scope; }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OAuthToken{");
        sb.append("accessToken='").append(accessToken).append('\'');
        sb.append(", user_id='").append(user_id).append('\'');
        sb.append(", tokenType='").append(tokenType).append('\'');
        sb.append(", scope='").append(scope).append('\'');
        sb.append(", expires_in=").append(expiresIn);
        sb.append(", expiredAfterMilli=").append(expiredAfterMilli);
        sb.append('}');
        return sb.toString();
    }

    /***********************************************************
     * Factory Pattern
     **********************************************************/

    public static class Factory {
        private static final String TAG = "OAuthToken.Factory";

        public static OAuthToken create() {
            long expiredAfter = 0;
            SharedPreferences sp = MyApplication.instance.getSharedPreferences(OAUTH_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
            if (sp.contains(SP_TOKEN_EXPIRED_AFTER_KEY)) {
                Log.e(TAG, "sp.contains(SP_TOKEN_EXPIRED_AFTER)");
                expiredAfter = sp.getLong(SP_TOKEN_EXPIRED_AFTER_KEY, 0);
                long now = System.currentTimeMillis();

                Log.e(TAG, "Delta : " + (now - expiredAfter));
                if (expiredAfter == 0 || now > expiredAfter) {
                    Log.e(TAG, "expiredAfter==0||now>expiredAfter, token has expired");
                    //flush token in the SP
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString(SP_TOKEN_KEY, null);
                    ed.commit();
                    //rebuild the object according to the SP
                    OAuthToken oauthToken = new OAuthToken();
                    oauthToken.setAccessToken(null);
                    oauthToken.setTokenType(sp.getString(SP_TOKEN_TYPE_KEY, null));
                    oauthToken.setExpiredAfterMilli(sp.getLong(SP_TOKEN_EXPIRED_AFTER_KEY, 0));
                    return oauthToken;
                } else {
                    Log.e(TAG, "NOT (expiredAfter==0||now<expiredAfter) current case, token is valid");
                    //rebuild the object according to the SP
                    OAuthToken oauthToken = new OAuthToken();
                    oauthToken.setAccessToken(sp.getString(SP_TOKEN_KEY, null));
                    oauthToken.setUser_id(sp.getString(SP_USER_ID, null));
                    oauthToken.setScope(sp.getString(SP_SCOPE, null));
                    oauthToken.setTokenType(sp.getString(SP_TOKEN_TYPE_KEY, null));
                    oauthToken.setExpiredAfterMilli(sp.getLong(SP_TOKEN_EXPIRED_AFTER_KEY, 0));
                    return oauthToken;
                }
            } else {
                return null;
            }
        }
    }

}