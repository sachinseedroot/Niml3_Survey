package com.example.neha.myapplication.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPrefSettings {

    public static void setuserTrackingId(Context context, String msisdn) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userTrackingId", msisdn);
        editor.commit();
    }

    public static String getuserTrackingId(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getString("userTrackingId", "");
    }


    public static void setFirstInstall(Context context, boolean IsUserVerified) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("FirstInstall", IsUserVerified);
        editor.commit();
    }

    public static boolean getFirstInstall(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getBoolean("FirstInstall", false);
    }

    public static void setIsUserCountryCode(Context context, String IsUserCountryCode) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("IsUserCountryCode", IsUserCountryCode);
        editor.commit();
    }

    public static String getIsUserCountryCode(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getString("IsUserCountryCode", "");
    }


    public static void setuserDetailsJSON(Context context, String userDetailsJSON) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userDetailsJSON", userDetailsJSON);
        editor.commit();
    }

    public static String getuserDetailsJSON(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getString("userDetailsJSON", "");
    }

    public static void setTrackingUuid(Context context, String TrackingUuid) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("TrackingUuid", TrackingUuid);
        editor.commit();
    }

    public static String getTrackingUuid(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getString("TrackingUuid", "");
    }

    public static void setANDROID_ID(Context context, String ANDROID_ID) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ANDROID_ID", ANDROID_ID);
        editor.commit();
    }

    public static String getANDROID_ID(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getString("ANDROID_ID", "");
    }

    public static void setAppsflyerReferrer(Context context, String AppsflyerReferrer) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("AppsflyerReferrer", AppsflyerReferrer);
        editor.commit();
    }

    public static String getAppsflyerReferrer(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getString("AppsflyerReferrer", "");
    }
}
