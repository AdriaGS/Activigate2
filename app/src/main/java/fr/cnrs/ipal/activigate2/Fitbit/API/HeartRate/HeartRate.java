package fr.cnrs.ipal.activigate2.Fitbit.API.HeartRate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HeartRate {

    @SerializedName("activities-heart")
    @Expose
    public List<ActivitiesHeart> activitiesHeart = null;

    public List<ActivitiesHeart> getActivitiesHeart() {
        return activitiesHeart;
    }

    public void setActivitiesHeart(List<ActivitiesHeart> activitiesHeart) {
        this.activitiesHeart = activitiesHeart;
    }

    @Override
    public String toString() {
        String returnString = "";
        return returnString;
    }

}
