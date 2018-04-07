package fr.cnrs.ipal.activigate2.Fitbit;

import fr.cnrs.ipal.activigate2.Fitbit.API.Activities.Activities;
import fr.cnrs.ipal.activigate2.Fitbit.API.HeartRate.HeartRate;
import fr.cnrs.ipal.activigate2.Fitbit.API.Sleep.SleepData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by adria on 20/3/18.
 */

public interface OAuthServerIntf {

    @GET("/1/user/{user-id}/activities/heart/date/{date}/1d.json")
    Call<HeartRate> getHeartRatedata(@Path("user-id") String userId, @Path("date") String date);

    @GET("/1/user/{user-id}/activities/date/{date}.json")
    Call<Activities> getActivitiesData(@Path("user-id") String userId, @Path("date") String date);

    @GET("/1/user/{user-id}/sleep/date/{date}.json")
    Call<SleepData> getSleepData(@Path("user-id") String userId, @Path("date") String date);

}
