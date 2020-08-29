package com.lamine.realestatemanager.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import com.lamine.realestatemanager.RealEstateManagerApplication;
import com.lamine.realestatemanager.models.Property;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    private static final String TEXT_DATE = "dd/MM/yyyy" ;

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param dollars <-- to convert dollar to euro
     */
    public static double convertDollarToEuro(double dollars) { return Math.round(dollars * 0.812); }

    // Euros to dollars conversion
    public static double convertEuroToDollar(double euros) {
        return Math.round(euros / 0.812);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     */
    // return today date
    @NotNull
    public static String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat(TEXT_DATE, Locale.FRANCE);
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     */

    // return true if network is connected
    @NotNull
    public static Boolean isInternetAvailable(@NotNull Context context) {
        boolean isInternet = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            network = Objects.requireNonNull(connectivityManager).getActiveNetwork();
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            if (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)){
                isInternet = true;
            }
        }else{
            NetworkInfo activeNetwork = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()){
                isInternet = true;
            }
        }
        return isInternet;
    }

    //To check if location is enable
    public static boolean isLocationEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            assert lm != null;
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gps_enabled;
    }

    // Convert date to string
    @NotNull
    public static String getStringDate(int year, int dayOfMonth, int monthOfYear) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, monthOfYear, dayOfMonth);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(TEXT_DATE, Locale.getDefault());
        return sdf.format(date);
    }

    // To define last item clicked id
    public static Long getPropertyId(List<Property> propertiesList) {
        int propId;
        int id = RealEstateManagerApplication.getLastItemClicked();
        if (id == -2) {
            propId = propertiesList.size();
        } else {
            propId = id + 1;
        }
        return (long) propId;
    }

    // To define last item clicked position
    public static int getPropertyPosition(List<Property> propertiesList) {
        int propPosition;
        int position = RealEstateManagerApplication.getLastItemClicked();
        if (position == -2) {
            propPosition = propertiesList.size() - 1;
            RealEstateManagerApplication.setLastItemClicked(propPosition);
        } else if (position == -1) {
            propPosition = 0;
        } else {
            propPosition = position;
        }
        return propPosition;
    }

}
