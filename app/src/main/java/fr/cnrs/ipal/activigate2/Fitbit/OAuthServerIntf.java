package fr.cnrs.ipal.activigate2.Fitbit;

import fr.cnrs.ipal.activigate2.Fitbit.API.Activities;
import fr.cnrs.ipal.activigate2.Fitbit.API.HeartRate;
import fr.cnrs.ipal.activigate2.Fitbit.API.Sleep;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by adria on 20/3/18.
 */

public interface OAuthServerIntf {

    @GET("/1/user/{user-id}/activities/heart/date/{date}/1d.json")
    Call<HeartRate> getHeartRatedata(@Path("user-id") String userId, @Path("date") String date);

    @GET("/1/user/-/activities/date/today.json")
    Call<Activities> getActivitiesData();

    @GET("/1/user/-/sleep/date/today.json")
    Call<Sleep> getSleepData();

}
