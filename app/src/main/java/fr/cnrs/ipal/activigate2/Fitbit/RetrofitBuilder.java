package fr.cnrs.ipal.activigate2.Fitbit;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;

import fr.cnrs.ipal.activigate2.Fitbit.interceptors.OAuthInterceptor;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by adria on 20/3/18.
 */

public class RetrofitBuilder {
    /***********************************************************
     *  Constants
     **********************************************************/
    /**
     * Root URL
     * (always ends with a /)
     */
    public static final String BASE_URL = "https://api.fitbit.com/";

    /***********************************************************
     * Getting OAuthServerIntf instance using Retrofit creation
     **********************************************************/

    /**
     * An autenticated client to make authenticated calls
     * The token is automaticly added in the Header of the request
     * @param ctx
     * @return OAuthServerIntf instance
     */
    public static OAuthServerIntf getOAuthClient(Context ctx) {
        // now it's using the cach
        // Using my HttpClient
        Retrofit raCustom = new Retrofit.Builder()
                .client(getOAuthOkHttpClient(ctx))
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
        OAuthServerIntf webServer = raCustom.create(OAuthServerIntf.class);
        return webServer;
    }

    /***********************************************************
     * OkHttp Client
     **********************************************************/

    /**
     * Return a OAuth OkHttpClient v:
     * have a cache
     * have a HttpLogger
     * add automaticly the token in the header of each request because of the oAuthInterceptor
     * @param ctx
     * @return
     */
    @NonNull
    public static OkHttpClient getOAuthOkHttpClient(Context ctx) {
        // Define the OkHttp Client with its cache!
        // Assigning a CacheDirectory
        File myCacheDir=new File(ctx.getCacheDir(),"OkHttpCache");
        // You should create it...
        int cacheSize=1024*1024;
        Cache cacheDir=new Cache(myCacheDir,cacheSize);
        Interceptor oAuthInterceptor=new OAuthInterceptor();
        HttpLoggingInterceptor httpLogInterceptor=new HttpLoggingInterceptor();
        httpLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .cache(cacheDir)
                .addInterceptor(oAuthInterceptor)
                .addInterceptor(httpLogInterceptor)
                .build();
    }
}
