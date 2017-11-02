package com.example.android.grabble_v4.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.android.grabble_v4.R;
import com.example.android.grabble_v4.data.HighScore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Created by user on 25/10/2017.
 */


public class PreferenceUtilities {

    // public static final String NEW_HIGH_SCORE = "new-high-score";
    public static final String HIGH_SCORE_LIST = "high-score-list";
    public static final int NUM_OF_SCORES = 5;


    public static int newScoreSend(Context context, int score, int countDownInd) {
        List<HighScore> highScoreList = getTopFive(context);

        if (highScoreList == null) {
            highScoreList = new ArrayList<>();
            newScoreInsert(highScoreList, context, score, false);
            return 1;
        }

        int list_size = highScoreList.size();

        if (list_size < NUM_OF_SCORES) {
            //the top 5 aren't full yet
            newScoreInsert(highScoreList, context, score, false);
            return 1; //made it to the wall
        }

        if (highScoreList.get(list_size - 1).getScore() < score) {
            //lowest score is lower than current score
            newScoreInsert(highScoreList, context, score, true);
            return 1;//made it to the wall
        } else return 0; //didn't make it to the wall
    }

    public static void newScoreInsert(List<HighScore> list, Context context, int score, boolean removeLast) {
        HighScore newHighScore = new HighScore(score);
        list.add(newHighScore);
        Collections.sort(list, new Comparator<HighScore>() {
            public int compare(HighScore h1, HighScore h2) {
                return h1.getScore() - h2.getScore();
            }
        });
        Collections.reverse(list);

        if (removeLast) {
            list.remove(list.size() - 1); //remove last score
        }
        setHighScore(context, list);

    }


    synchronized private static void setHighScore(Context context, List<HighScore> highScoreList) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(highScoreList);
        editor.putString(HIGH_SCORE_LIST, json);
        editor.apply();
    }

    public static List<HighScore> getTopFive(Context context) {

        List<HighScore> highScoreList;
        Gson gson = new Gson();
        Type type = new TypeToken<List<HighScore>>() {
        }.getType();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        highScoreList = gson.fromJson(prefs.getString(HIGH_SCORE_LIST, ""), type);

        return highScoreList;
    }



}
