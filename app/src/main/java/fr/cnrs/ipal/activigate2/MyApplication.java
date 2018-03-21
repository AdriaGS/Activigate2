package fr.cnrs.ipal.activigate2;

import android.app.Application;

/**
 * Created by adria on 20/3/18.
 */

public class MyApplication extends Application {
    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
