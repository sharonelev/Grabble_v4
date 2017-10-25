package com.example.android.grabble_v4.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.grabble_v4.data.HighScore;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by user on 25/10/2017.
 */


public class PreferenceUtilities {

    public static final String NEW_HIGH_SCORE = "new-high-score";
    public static List<HighScore> highScoreList = new ArrayList<>();

    synchronized private static void setHighScore(Context context, int score) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(NEW_HIGH_SCORE, score);
        editor.apply();
    }

    public static int getTopFive(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

      //  int glassesOfWater = prefs.getStringSet(KEY_WATER_COUNT, DEFAULT_COUNT);

        return 0;
    }

}
