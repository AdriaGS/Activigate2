package fr.cnrs.ipal.activigate2.Fitbit.API.Sleep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SleepData {

    @SerializedName("sleep")
    @Expose
    private List<Sleep> sleep = null;
    @SerializedName("summary")
    @Expose
    private Summary_ summary = null;

    public List<Sleep> getSleep() {
        return sleep;
    }

    public void setSleep(List<Sleep> sleep) {
        this.sleep = sleep;
    }

    public Summary_ getSummary() {
        return summary;
    }

    public void setSummary(Summary_ summary) {
        this.summary = summary;
    }

    public Sleep getMainSleep() {
        for(Sleep item: sleep) {
            if (item.getIsMainSleep()) {
                return item;
            }
        }
        return null;
    }

}
