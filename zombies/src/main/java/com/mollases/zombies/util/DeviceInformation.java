package com.mollases.zombies.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.TimeZone;

/**
 * Created by mollases on 4/19/14.
 */
public class DeviceInformation {
    public static final int DEFAULT_REG_ID = -1;
    private static final String TAG = DeviceInformation.class.getName();
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String CURRENT_GAME_ID = "in_game_id";

    public static int getRegistrationId(Context context) {
        final SharedPreferences prefs = getAppPreferences(context);
        int registrationId = prefs.getInt(PROPERTY_REG_ID, DEFAULT_REG_ID);
        if (registrationId == -1) {
            Log.i(TAG, "Registration not found.");
        }
        return registrationId;
    }

    public static String getRegistrationIdAsString(Context context) {
        return String.valueOf(getRegistrationId(context));
    }

    public static String getInGameIdAsString(Context context) {
        return String.valueOf(getInGameId(context));
    }

    public static int getInGameId(Context context) {
        final SharedPreferences prefs = getAppPreferences(context);
        return prefs.getInt(CURRENT_GAME_ID, -1);
    }

    public static void storeRegistrationId(Context context, int regId) {
        final SharedPreferences prefs = getAppPreferences(context);
        Log.i(TAG, "Saving regId: " + regId);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PROPERTY_REG_ID, regId);
        editor.commit();
    }

    public static void storeCurrentlyActiveGame(Context context, int gameId) {
        final SharedPreferences prefs = getAppPreferences(context);
        Log.i(TAG, "Saving matchid: " + gameId);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(CURRENT_GAME_ID, gameId);
        editor.commit();

    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private static SharedPreferences getAppPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
    }

    public static int getTimeZoneByHourlyOffset() {
        return TimeZone.getDefault().getOffset(System.currentTimeMillis()) / 60 / 60 / 1000;
    }
}
