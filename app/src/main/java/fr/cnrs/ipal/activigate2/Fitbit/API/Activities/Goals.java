package fr.cnrs.ipal.activigate2.Fitbit.API.Activities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Goals {

    @SerializedName("caloriesOut")
    @Expose
    private Integer caloriesOut;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("floors")
    @Expose
    private Integer floors;
    @SerializedName("steps")
    @Expose
    private Integer steps;

    public Integer getCaloriesOut() {
        return caloriesOut;
    }

    public void setCaloriesOut(Integer caloriesOut) {
        this.caloriesOut = caloriesOut;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getFloors() {
        return floors;
    }

    public void setFloors(Integer floors) {
        this.floors = floors;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

}
