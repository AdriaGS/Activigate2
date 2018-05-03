package fr.cnrs.ipal.activigate2.Fitbit.API.Sleep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Deep {

    @SerializedName("count")
    @Expose
    private Integer count = 0;
    @SerializedName("minutes")
    @Expose
    private Integer minutes = 0;
    @SerializedName("thirtyDayAvgMinutes")
    @Expose
    private Integer thirtyDayAvgMinutes = 0;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getThirtyDayAvgMinutes() {
        return thirtyDayAvgMinutes;
    }

    public void setThirtyDayAvgMinutes(Integer thirtyDayAvgMinutes) {
        this.thirtyDayAvgMinutes = thirtyDayAvgMinutes;
    }

}
