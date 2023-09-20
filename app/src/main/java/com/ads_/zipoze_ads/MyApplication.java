package com.ads_.zipoze_ads;

import android.app.Application;
import android.content.Context;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;

public class MyApplication extends Application {

    public static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AudienceNetworkAds.initialize(this);
        MobileAds.initialize(this);
    }

    public static MyApplication getInstance() {
        return mInstance;
    }


    public static synchronized Context getContext() {
        Context applicationContext;
        synchronized (MyApplication.class) {
            applicationContext = getInstance().getApplicationContext();
        }
        return applicationContext;
    }
}
