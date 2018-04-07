package fr.cnrs.ipal.activigate2.Fitbit.API.Activities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Summary {

    @SerializedName("activityCalories")
    @Expose
    private Integer activityCalories;
    @SerializedName("caloriesBMR")
    @Expose
    private Integer caloriesBMR;
    @SerializedName("caloriesOut")
    @Expose
    private Integer caloriesOut;
    @SerializedName("distances")
    @Expose
    private List<Distance> distances = null;
    @SerializedName("elevation")
    @Expose
    private Double elevation;
    @SerializedName("fairlyActiveMinutes")
    @Expose
    private Integer fairlyActiveMinutes;
    @SerializedName("floors")
    @Expose
    private Integer floors;
    @SerializedName("lightlyActiveMinutes")
    @Expose
    private Integer lightlyActiveMinutes;
    @SerializedName("marginalCalories")
    @Expose
    private Integer marginalCalories;
    @SerializedName("sedentaryMinutes")
    @Expose
    private Integer sedentaryMinutes;
    @SerializedName("steps")
    @Expose
    private Integer steps;
    @SerializedName("veryActiveMinutes")
    @Expose
    private Integer veryActiveMinutes;

    public Integer getActivityCalories() {
        return activityCalories;
    }

    public void setActivityCalories(Integer activityCalories) {
        this.activityCalories = activityCalories;
    }

    public Integer getCaloriesBMR() {
        return caloriesBMR;
    }

    public void setCaloriesBMR(Integer caloriesBMR) {
        this.caloriesBMR = caloriesBMR;
    }

    public Integer getCaloriesOut() {
        return caloriesOut;
    }

    public void setCaloriesOut(Integer caloriesOut) {
        this.caloriesOut = caloriesOut;
    }

    public List<Distance> getDistances() {
        return distances;
    }

    public void setDistances(List<Distance> distances) {
        this.distances = distances;
    }

    public Double getElevation() {
        return elevation;
    }

    public void setElevation(Double elevation) {
        this.elevation = elevation;
    }

    public Integer getFairlyActiveMinutes() {
        return fairlyActiveMinutes;
    }

    public void setFairlyActiveMinutes(Integer fairlyActiveMinutes) {
        this.fairlyActiveMinutes = fairlyActiveMinutes;
    }

    public Integer getFloors() {
        return floors;
    }

    public void setFloors(Integer floors) {
        this.floors = floors;
    }

    public Integer getLightlyActiveMinutes() {
        return lightlyActiveMinutes;
    }

    public void setLightlyActiveMinutes(Integer lightlyActiveMinutes) {
        this.lightlyActiveMinutes = lightlyActiveMinutes;
    }

    public Integer getMarginalCalories() {
        return marginalCalories;
    }

    public void setMarginalCalories(Integer marginalCalories) {
        this.marginalCalories = marginalCalories;
    }

    public Integer getSedentaryMinutes() {
        return sedentaryMinutes;
    }

    public void setSedentaryMinutes(Integer sedentaryMinutes) {
        this.sedentaryMinutes = sedentaryMinutes;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public Integer getVeryActiveMinutes() {
        return veryActiveMinutes;
    }

    public void setVeryActiveMinutes(Integer veryActiveMinutes) {
        this.veryActiveMinutes = veryActiveMinutes;
    }

}
