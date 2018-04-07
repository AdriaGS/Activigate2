package fr.cnrs.ipal.activigate2.Fitbit;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fr.cnrs.ipal.activigate2.HAR.Result;
import fr.cnrs.ipal.activigate2.MyApplication;
import fr.cnrs.ipal.activigate2.View.FitbitActivity;

public class FitbitUtils {

    static int restingHeartRate = 0;
    static int sedentaryMin = 0;
    static int lightlyActiveMin = 0;
    static int veryActiveMin = 0;
    static int steps = 0;
    static int stepsGoal = 0;
    static long sleepDuration = 0;
    static int sleepEfficiency = 0;
    static int awakeningsCount = 0;
    static ArrayList<Integer> minutesZones;

    //GETTERS

    public static int getRestingHeartRate() {
        return restingHeartRate;
    }

    public static int getSedentaryMin() {
        return sedentaryMin;
    }

    public static int getLightlyActiveMin() {
        return lightlyActiveMin;
    }

    public static int getVeryActiveMin() {
        return veryActiveMin;
    }

    public static int getSteps() {
        return steps;
    }

    public static int getStepsGoal() {
        return stepsGoal;
    }

    public static long getSleepDuration() {
        return sleepDuration;
    }

    public static int getSleepEfficiency() {
        return sleepEfficiency;
    }

    public static int getAwakeningsCount() {
        return awakeningsCount;
    }

    public static ArrayList<Integer> getMinutesZones() {
        return minutesZones;
    }

    //SETTERS

    public static void setRestingHeartRate(int restingHeartRate) {
        FitbitUtils.restingHeartRate = restingHeartRate;
    }

    public static void setSedentaryMin(int sedentaryMin) {
        FitbitUtils.sedentaryMin = sedentaryMin;
    }

    public static void setLightlyActiveMin(int lightlyActiveMin) {
        FitbitUtils.lightlyActiveMin = lightlyActiveMin;
    }

    public static void setVeryActiveMin(int veryActiveMin) {
        FitbitUtils.veryActiveMin = veryActiveMin;
    }

    public static void setSteps(int steps) {
        FitbitUtils.steps = steps;
    }

    public static void setStepsGoal(int stepsGoal) {
        FitbitUtils.stepsGoal = stepsGoal;
    }

    public static void setSleepDuration(long sleepDuration) {
        FitbitUtils.sleepDuration = sleepDuration;
    }

    public static void setSleepEfficiency(int sleepEfficiency) {
        FitbitUtils.sleepEfficiency = sleepEfficiency;
    }

    public static void setAwakeningsCount(int awakeningsCount) {
        FitbitUtils.awakeningsCount = awakeningsCount;
    }

    public static void setMinutesZones(ArrayList<Integer> minutesZones) {
        FitbitUtils.minutesZones = minutesZones;
    }

}
