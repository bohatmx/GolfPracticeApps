package com.boha.golfpractice.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.boha.golfpractice.library.dto.CoachDTO;
import com.boha.golfpractice.library.dto.GcmDeviceDTO;
import com.boha.golfpractice.library.dto.PlayerDTO;
import com.google.gson.Gson;

/**
 * Created by aubreyM on 2014/10/12.
 */
public class SharedUtil {
    static final Gson gson = new Gson();
    public static final String
            COACH_JSON = "coach",
            GCMDEVICE = "gcmd",
            PLAYER_JSON = "player",
            LAST_PLAYER_ID = "lastMonID",
            LAST_COACH_ID = "lastStaffID",
            GCM_REGISTRATION_ID = "gcm",
            THEME = "theme",
            AUTH_TOKEN = "token",
            LOG = "SharedUtil",
            APP_VERSION = "appVersion";

    public static void setThemeSelection(Context ctx, int theme) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(THEME, theme);
        ed.commit();

        Log.w(LOG, "#### theme saved: " + theme);

    }

    public static int getThemeSelection(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        int j = sp.getInt(THEME, -1);
        Log.i(LOG, "#### theme retrieved: " + j);
        return j;
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's monApp.
     * @param regId   registration ID
     */
    public static void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(LOG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(GCM_REGISTRATION_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
        Log.e(LOG, "GCM registrationId saved in prefs! Yebo!!!");
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    public static String getRegistrationId(Context context) {
        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String registrationId = prefs.getString(GCM_REGISTRATION_ID, null);
        if (registrationId == null) {
            Log.i(LOG, "GCM Registration ID not found on device.");
            return null;
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = SharedUtil.getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(LOG, "App version changed.");
            return null;
        }
        return registrationId;
    }

     public static void savePlayer(Context ctx, PlayerDTO dto) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String x = gson.toJson(dto);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(PLAYER_JSON, x);
        ed.commit();
        Log.e("SharedUtil", "%%%%% Player: " + dto.getFirstName() + " " + dto.getLastName() + " saved in SharedPreferences");
    }


    public static PlayerDTO getPlayer(Context ctx) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String mon = sp.getString(PLAYER_JSON, null);
        PlayerDTO player = null;
        if (mon != null) {
            player = gson.fromJson(mon, PlayerDTO.class);

        }
        return player;
    }

    public static void saveCoach(Context ctx, CoachDTO dto) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String x = gson.toJson(dto);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(COACH_JSON, x);
        ed.commit();
        Log.e("SharedUtil", "%%%%% Coach: " + dto.getFirstName() + " " + dto.getLastName() + " saved in SharedPreferences");
    }


    public static CoachDTO getCoach(Context ctx) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String adm = sp.getString(COACH_JSON, null);
        CoachDTO ciach = null;
        if (adm != null) {
            ciach = gson.fromJson(adm, CoachDTO.class);

        }
        return ciach;
    }


    public static void saveGCMDevice(Context ctx, GcmDeviceDTO dto) {


        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String x = gson.toJson(dto);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(GCMDEVICE, x);
        ed.commit();
        System.out.println("%%%%% Device saved in SharedPreferences");
    }


    public static GcmDeviceDTO getGCMDevice(Context ctx) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String adm = sp.getString(GCMDEVICE, null);
        GcmDeviceDTO co = null;
        if (adm != null) {
            co = gson.fromJson(adm, GcmDeviceDTO.class);

        }
        if (co != null)
            Log.e("SharedUtil", "%%%%% Device found in SharedPreferences: " + co.getModel());
        return co;
    }



    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    public static void saveLastPlayerID(Context ctx, Integer playerID) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(LAST_PLAYER_ID, playerID);
        ed.commit();
        Log.e("SharedUtil", "%%%%% PlayerID: " + playerID + " saved in SharedPreferences");
    }

    public static Integer getLastPlayerID(Context ctx) {
        if (ctx == null) {
            return 0;
        }
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        int id = sp.getInt(LAST_PLAYER_ID, 0);
        return id;
    }

    public static void saveLastCoachID(Context ctx, Integer coachID) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(LAST_COACH_ID, coachID);
        ed.commit();
        Log.e("SharedUtil", "%%%%% coachID: " + coachID + " saved in SharedPreferences");
    }

    public static Integer getLastStaffID(Context ctx) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        int id = sp.getInt(LAST_COACH_ID, 0);
        return id;
    }


    public static void saveAuthToken(Context ctx, String token) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(AUTH_TOKEN, token);
        ed.commit();
        Log.e("SharedUtil", "%%%%% auth-token: " + token + " saved in SharedPreferences");
    }

    public static String getAuthToken(Context ctx) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String id = sp.getString(AUTH_TOKEN, null);
        return id;
    }
}
