package fr.cnrs.ipal.activigate2.Fitbit.API.HeartRate;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivitiesHeart {

    @SerializedName("dateTime")
    @Expose
    public String dateTime;
    @SerializedName("value")
    @Expose
    public Value value;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}

