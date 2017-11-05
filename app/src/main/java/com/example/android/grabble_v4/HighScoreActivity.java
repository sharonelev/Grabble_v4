package com.example.android.grabble_v4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;

import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.android.grabble_v4.Utilities.OnSwipeTouchListener;
import com.example.android.grabble_v4.Utilities.PreferenceUtilities;

public class HighScoreActivity extends Activity implements View.OnClickListener {
    RecyclerView scoresRecyclerView;
    HighScoreAdapter scoreAdapter;
    TextView mDate;
    TextView mScore;
    TextView mTitle;
    View activityView;
    int countDownInd;
    ImageView rightDot;
    ImageView middleDot;
    ImageView leftDot;
    OnSwipeTouchListener onSwipeTouchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_high_score);
        activityView = (View) findViewById(R.id.high_score_activity_layout);
        mTitle = (TextView) findViewById(R.id.high_score_title);
        scoresRecyclerView = (RecyclerView) findViewById(R.id.score_list_view);
        rightDot = (ImageView) findViewById(R.id.right_dot);
        leftDot = (ImageView) findViewById(R.id.left_dot);
        middleDot = (ImageView) findViewById(R.id.middle_dot);

        countDownInd = getIntent().getIntExtra("gameType", 0);
        setContent(countDownInd);



    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.new_game_in_high_score) {
            Intent homeIntent = new Intent(HighScoreActivity.this, MainActivity.class);
            homeIntent.putExtra("Button_tapped", R.string.new_game);
            setResult(MainActivity.RESULT_CODE, homeIntent);
            //startActivity(homeIntent);

            finish();
        }

        if (view.getId() == R.id.back_to_game_in_high_score) {

            finish();
        }
    }

    public void setContent(int gameType){
        switch (gameType) {
            case 0:
                mTitle.setText(getString(R.string.classic_high_score));
                leftDot.setColorFilter(Color.parseColor("#aba7a7"));
                middleDot.setColorFilter(Color.parseColor("#000000"));
                rightDot.setColorFilter(Color.parseColor("#000000"));
                break;
            case 1:
                mTitle.setText(getString(R.string.moderate_high_score));
                leftDot.setColorFilter(Color.parseColor("#000000"));
                middleDot.setColorFilter(Color.parseColor("#aba7a7"));
                rightDot.setColorFilter(Color.parseColor("#000000"));
                break;
            case 2:
                mTitle.setText(getString(R.string.speedy_high_score));
                leftDot.setColorFilter(Color.parseColor("#000000"));
                middleDot.setColorFilter(Color.parseColor("#000000"));
                rightDot.setColorFilter(Color.parseColor("#aba7a7"));
                break;
        }

        scoreAdapter = new HighScoreAdapter(PreferenceUtilities.getTopFive(getBaseContext(), gameType)); //0= classic mode
        LinearLayoutManager scoreLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        scoreLayoutManager.setAutoMeasureEnabled(true);
        scoresRecyclerView.setLayoutManager(scoreLayoutManager);
        scoresRecyclerView.setAdapter(scoreAdapter);
        scoresRecyclerView.setLayoutFrozen(true);
        if (scoreAdapter.highScoreList == null) {
            mDate = (TextView) findViewById(R.id.Date_title);
            mDate.setText("No High Scores");
            mScore = (TextView) findViewById(R.id.score_title);
            mScore.setVisibility(View.GONE);
        }
        onSwipe(activityView);


    }

    public void onSwipe(View view) {
        onSwipeTouchListener = new OnSwipeTouchListener(HighScoreActivity.this) {


            public void onSwipeLeft() {
            Log.i("swipe", "left");
                if(countDownInd<2) {
                    setContent(countDownInd + 1);
                    countDownInd=countDownInd+1;
                }
            }

            public void onSwipeRight() {
                Log.i("swipe", "right");

               if(countDownInd>0){
                   setContent(countDownInd-1);
                   countDownInd=countDownInd-1;
               }


            }

        };
        view.setOnTouchListener(onSwipeTouchListener);
    }


}
