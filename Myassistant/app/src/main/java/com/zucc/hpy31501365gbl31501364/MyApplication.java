package com.zucc.hpy31501365gbl31501364;

import android.app.Application;
import android.content.Context;

/**
 * Created by L-Jere on 2018/7/16.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
