package com.brainupco.geotimeclock;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

/**
 * Created by jsantos on 29/mar/2016.
 */
public class Utility {

    // Status
    public static String getSavedAssetStatus(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_Asset_Status_key),
                context.getString(R.string.status_available));
    }

    public static void setAssetStatus(Context context, String newValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_Asset_Status_key), newValue);
        editor.commit();
    }

    // IMEI
    public static String getSavedAssetIMEI(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_Asset_IMEI_key),
                "");
    }

    public static void setAssetIMEI(Context context, String newValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_Asset_IMEI_key), newValue);
        editor.commit();
    }

    // Starting Starting Time
    public static String getSavedAssetStartingTime(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_Asset_Starting_Time_key),
                "");
    }

    public static void setAssetStartingTime(Context context, String newValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_Asset_Starting_Time_key), newValue);
        editor.commit();
    }

    // Id Task
    public static String getSavedAssetIdTask(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_Asset_Id_Task_key),
                "");
    }

    public static void setAssetIdTask(Context context, String newValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_Asset_Id_Task_key), newValue);
        editor.commit();
    }

    // Task
    public static String getSavedAssetTask(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_Asset_Task_key),
                "");
    }

    public static void setAssetTask(Context context, String newValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_Asset_Task_key), newValue);
        editor.commit();
    }

    // Address
    public static String getSavedAssetAddress(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_Asset_Address_key),
                "");
    }

    public static void setAssetAddress(Context context, String newValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_Asset_Address_key), newValue);
        editor.commit();
    }
    // Work Duration
    public static String getSavedAssetWorkDuration(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_Asset_Work_Duration_key),
                "");
    }

    public static void setAssetWorkDuration(Context context, String newValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_Asset_Work_Duration_key), newValue);
        editor.commit();
    }
    // IMEI
    public static String getDeviceIMEI(Context context) {
        // (testphone: 868442014378892)
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String devID = telephonyManager.getDeviceId();
        //String andID = System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        //return telephonyManager.getDeviceId();
        return devID;
    }

    public static ProgressDialog createPD(Context context, String title, String message){
        ProgressDialog pg = new ProgressDialog(context);

        // Set Values
        pg.setIcon(R.mipmap.ic_launcher);
        pg.setTitle(title);
        pg.setMessage(message);
        pg.setIndeterminate(true);
        pg.setCancelable(false);

        return pg;
    }
    public static void showDialog(Context context, String title, String message){
        boolean status = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
            .setTitle(title)
            .setIcon(R.mipmap.ic_launcher)
            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // FIRE ZE MISSILES!

                }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
