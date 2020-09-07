package com.lamine.realestatemanager;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import java.nio.channels.MulticastChannel;

/**
 * Created by Lamine MESSACI on 01/08/2020.
 */
public class RealEstateManagerApplication extends Application {


    private static Boolean searchCalls = false;
    private static int lastItemClicked = 0;
    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    public static boolean isSearch() {
        return searchCalls;
    }

    public static void setSearchCalls(Boolean search) {
        searchCalls = search;
    }

    public static int getLastItemClicked() {
        return lastItemClicked;
    }

    public static void setLastItemClicked(int item) {
        lastItemClicked = item;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
