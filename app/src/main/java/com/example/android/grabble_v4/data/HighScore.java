package com.example.android.grabble_v4.data;


import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 25/10/2017.
 */

public class HighScore{
    String scoreDate;
    int highScore;

    public HighScore(int score){
        highScore=score;
        scoreDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }
    public int getScore(){
        return highScore;
    }
    public String getScoreDate() {return scoreDate;}


}
