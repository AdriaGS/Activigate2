package fr.cnrs.ipal.activigate2.Fitbit.API.Sleep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Summary_ {

    @SerializedName("totalMinutesAsleep")
    @Expose
    private Integer totalMinutesAsleep = 0;
    @SerializedName("totalSleepRecords")
    @Expose
    private Integer totalSleepRecords = 0;
    @SerializedName("totalTimeInBed")
    @Expose
    private Integer totalTimeInBed = 0;

    public Integer getTotalMinutesAsleep() {
        return totalMinutesAsleep;
    }

    public void setTotalMinutesAsleep(Integer totalMinutesAsleep) {
        this.totalMinutesAsleep = totalMinutesAsleep;
    }

    public Integer getTotalSleepRecords() {
        return totalSleepRecords;
    }

    public void setTotalSleepRecords(Integer totalSleepRecords) {
        this.totalSleepRecords = totalSleepRecords;
    }

    public Integer getTotalTimeInBed() {
        return totalTimeInBed;
    }

    public void setTotalTimeInBed(Integer totalTimeInBed) {
        this.totalTimeInBed = totalTimeInBed;
    }

}