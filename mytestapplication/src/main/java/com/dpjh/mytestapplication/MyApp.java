package com.dpjh.mytestapplication;

import android.app.Application;
import android.content.Context;

/**
 * xxxx
 * Date: 2024/4/18 11:00
 * Author: liangdp
 */
public class MyApp extends Application {

    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
    }
}
