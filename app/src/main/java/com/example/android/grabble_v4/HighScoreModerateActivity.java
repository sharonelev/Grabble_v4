package com.example.android.grabble_v4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.grabble_v4.Utilities.OnSwipeTouchListener;
import com.example.android.grabble_v4.Utilities.PreferenceUtilities;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;

import org.w3c.dom.Text;

public class HighScoreModerateActivity extends FragmentActivity implements View.OnClickListener{
    RecyclerView scoresRecyclerView;
    HighScoreAdapter scoreAdapter;

    TextView mDate;
    TextView mScore;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_moderate);


        view = (View) findViewById(R.id.high_score_moderate_activity_layout);
        OnSwipeTouchListener onSwipeTouchListener = new OnSwipeTouchListener(HighScoreModerateActivity.this) {
            public void onSwipeTop() {
                Log.i("swipe", "up");
            }

            public void onSwipeRight() {
                Log.i("swipe", "right");
            }

            public void onSwipeLeft() {
                Log.i("swipe", "left");
            }

            public void onSwipeBottom() {
                Log.i("swipe", "bottom");
            }
        };

        view.setOnTouchListener(onSwipeTouchListener);

        scoresRecyclerView = (RecyclerView)findViewById(R.id.score_moderate_list_view);
        scoreAdapter = new HighScoreAdapter(PreferenceUtilities.getTopFive(getBaseContext(),1));
        LinearLayoutManager scoreLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        scoreLayoutManager.setAutoMeasureEnabled(true);
        scoresRecyclerView.setLayoutManager(scoreLayoutManager);
        scoresRecyclerView.setAdapter(scoreAdapter);

        scoresRecyclerView.setLayoutFrozen(true);

        if(scoreAdapter.highScoreList==null){
                mDate = (TextView) findViewById(R.id.Date_title);
                mDate.setText("No High Scores");
                mScore = (TextView) findViewById(R.id.score_title);
                mScore.setVisibility(View.GONE);
            }
        }

    @Override
    public void onClick(View view) {
         if(view.getId()==R.id.new_game_in_high_score){
             Intent homeIntent = new Intent(HighScoreModerateActivity.this, MainActivity.class);
             homeIntent.putExtra("Button_tapped", R.string.new_game);
             setResult(MainActivity.RESULT_CODE,homeIntent);
             //startActivity(homeIntent);

        finish();
}

        if(view.getId()==R.id.back_to_game_in_high_score){

             finish();
        }
    }


}
