package com.lamine.realestatemanager;

import android.app.Application;
import android.content.Context;

/**
 * Created by Lamine MESSACI on 01/08/2020.
 */
public class RealEstateManagerApplication extends Application {

    public static Boolean searchCalls =false;
    private static int lastItemClicked = 0;
    private static  Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static Application getsApplication() {
        return sApplication;
    }

    public static Application getApplication(){return sApplication;}

    public static Context getContext(){return getApplication().getApplicationContext(); }

    public static  boolean isSearchCalls(Boolean search){return searchCalls; }

    public static int getLastItemClicked () { return lastItemClicked; }

    public static void setLastItemClicked(int itemClicked) { lastItemClicked = itemClicked; }

    public static void setSearchCalls(Boolean search) { searchCalls = search; }

}
