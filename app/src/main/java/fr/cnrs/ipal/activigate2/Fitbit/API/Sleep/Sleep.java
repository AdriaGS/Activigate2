package fr.cnrs.ipal.activigate2.Fitbit.API.Sleep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sleep {

    @SerializedName("dateOfSleep")
    @Expose
    private String dateOfSleep = "";
    @SerializedName("duration")
    @Expose
    private Long duration = 0L;
    @SerializedName("efficiency")
    @Expose
    private Integer efficiency = 0;
    @SerializedName("isMainSleep")
    @Expose
    private Boolean isMainSleep = false;
    @SerializedName("levels")
    @Expose
    private Levels levels = null;
    @SerializedName("logId")
    @Expose
    private Long logId = 0L;
    @SerializedName("minutesAfterWakeup")
    @Expose
    private Integer minutesAfterWakeup = 0;
    @SerializedName("minutesAsleep")
    @Expose
    private Integer minutesAsleep = 0;
    @SerializedName("minutesAwake")
    @Expose
    private Integer minutesAwake = 0;
    @SerializedName("minutesToFallAsleep")
    @Expose
    private Integer minutesToFallAsleep = 0;
    @SerializedName("startTime")
    @Expose
    private String startTime = "";
    @SerializedName("timeInBed")
    @Expose
    private Integer timeInBed = 0;
    @SerializedName("type")
    @Expose
    private String type = "";
    @SerializedName("awakeCount")
    @Expose
    private Integer awakeCount = 0;

    public String getDateOfSleep() {
        return dateOfSleep;
    }

    public void setDateOfSleep(String dateOfSleep) {
        this.dateOfSleep = dateOfSleep;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(Integer efficiency) {
        this.efficiency = efficiency;
    }

    public Boolean getIsMainSleep() {
        return isMainSleep;
    }

    public void setIsMainSleep(Boolean isMainSleep) {
        this.isMainSleep = isMainSleep;
    }

    public Levels getLevels() {
        return levels;
    }

    public void setLevels(Levels levels) {
        this.levels = levels;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Integer getMinutesAfterWakeup() {
        return minutesAfterWakeup;
    }

    public void setMinutesAfterWakeup(Integer minutesAfterWakeup) {
        this.minutesAfterWakeup = minutesAfterWakeup;
    }

    public Integer getMinutesAsleep() {
        return minutesAsleep;
    }

    public void setMinutesAsleep(Integer minutesAsleep) {
        this.minutesAsleep = minutesAsleep;
    }

    public Integer getMinutesAwake() {
        return minutesAwake;
    }

    public void setMinutesAwake(Integer minutesAwake) {
        this.minutesAwake = minutesAwake;
    }

    public Integer getMinutesToFallAsleep() {
        return minutesToFallAsleep;
    }

    public void setMinutesToFallAsleep(Integer minutesToFallAsleep) {
        this.minutesToFallAsleep = minutesToFallAsleep;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getTimeInBed() {
        return timeInBed;
    }

    public void setTimeInBed(Integer timeInBed) {
        this.timeInBed = timeInBed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAwakeCount() { return awakeCount; }

    public void setAwakeCount(Integer awakeCount) { this.awakeCount = awakeCount; }

}
