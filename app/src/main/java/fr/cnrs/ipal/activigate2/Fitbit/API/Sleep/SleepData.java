package fr.cnrs.ipal.activigate2.Fitbit.API.Sleep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SleepData {

    @SerializedName("sleep")
    @Expose
    private List<SleepData> sleep = null;
    @SerializedName("summary")
    @Expose
    private Summary_ summary;

    public List<SleepData> getSleep() {
        return sleep;
    }

    public void setSleep(List<SleepData> sleep) {
        this.sleep = sleep;
    }

    public Summary_ getSummary() {
        return summary;
    }

    public void setSummary(Summary_ summary) {
        this.summary = summary;
    }

}
