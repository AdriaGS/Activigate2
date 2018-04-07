package fr.cnrs.ipal.activigate2.Fitbit.API.Sleep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShortDatum {

    @SerializedName("datetime")
    @Expose
    private String datetime;
    @SerializedName("level")
    @Expose
    private String level;
    @SerializedName("seconds")
    @Expose
    private Integer seconds;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

}
