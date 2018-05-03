package fr.cnrs.ipal.activigate2.Fitbit.API.HeartRate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeartRateZone {

    @SerializedName("caloriesOut")
    @Expose
    private Float caloriesOut;
    @SerializedName("max")
    @Expose
    private Integer max = 0;
    @SerializedName("min")
    @Expose
    private Integer min = 0;
    @SerializedName("minutes")
    @Expose
    private Integer minutes = 0;
    @SerializedName("name")
    @Expose
    private String name = "";

    public Float getCaloriesOut() {
        return caloriesOut;
    }

    public void setCaloriesOut(Float caloriesOut) {
        this.caloriesOut = caloriesOut;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
