package fr.cnrs.ipal.activigate2.Fitbit.API.Activities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Distance {

    @SerializedName("activity")
    @Expose
    private String activity;
    @SerializedName("distance")
    @Expose
    private Double distance;

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

}
