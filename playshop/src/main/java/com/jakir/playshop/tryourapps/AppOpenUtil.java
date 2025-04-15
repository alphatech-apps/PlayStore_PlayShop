package com.jakir.playshop.tryourapps;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppOpenUtil {

    private static final String PREF_NAME = "AppUsagePrefs";
    private static final String KEY_OPEN_COUNT = "open_count";
    private static final String KEY_LAST_DATE = "last_date";

    public static int countAppOpen(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String lastDate = prefs.getString(KEY_LAST_DATE, "");

        int openCount;

        if (!todayDate.equals(lastDate)) {
            openCount = 1;// First time opening the app today
            editor.putString(KEY_LAST_DATE, todayDate);
        } else {
            openCount = prefs.getInt(KEY_OPEN_COUNT, 0) + 1;
        }

        editor.putInt(KEY_OPEN_COUNT, openCount);
        editor.apply();

        return openCount;
    }

    public static int getCountAppOpen(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return prefs.getInt(KEY_OPEN_COUNT, 0);
    }
}
