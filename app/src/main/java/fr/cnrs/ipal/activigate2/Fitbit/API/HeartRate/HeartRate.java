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
        final StringBuffer sb = new StringBuffer("{\"ActivitiesHeart\":[\n")
                .append("\t{\n\t\t\"dateTime\": ").append(activitiesHeart.get(0).getDateTime())
                .append("\n\t\t\"value\": {");
        return sb.toString();
    }

}
