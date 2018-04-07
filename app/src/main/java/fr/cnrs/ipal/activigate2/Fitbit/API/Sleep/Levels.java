package fr.cnrs.ipal.activigate2.Fitbit.API.Sleep;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Levels {

    @SerializedName("summary")
    @Expose
    private Summary summary;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("shortData")
    @Expose
    private List<ShortDatum> shortData = null;

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public List<ShortDatum> getShortData() {
        return shortData;
    }

    public void setShortData(List<ShortDatum> shortData) {
        this.shortData = shortData;
    }

}
