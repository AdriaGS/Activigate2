package fr.cnrs.ipal.activigate2.Fitbit.API.Activities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Activities {

    @SerializedName("activities")
    @Expose
    private List<Activity> activities = null;
    @SerializedName("goals")
    @Expose
    private Goals goals;
    @SerializedName("summary")
    @Expose
    private Summary summary;

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public Goals getGoals() {
        return goals;
    }

    public void setGoals(Goals goals) {
        this.goals = goals;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

}
