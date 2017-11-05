package com.example.android.grabble_v4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.grabble_v4.Utilities.PreferenceUtilities;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;

public class HighScoreSpeedyActivity extends Activity implements View.OnClickListener{

    RecyclerView scoresRecyclerView;
    HighScoreAdapter scoreAdapter;
    TextView mDate;
    TextView mScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_speedy);
        scoresRecyclerView = (RecyclerView)findViewById(R.id.score_speedy_list_view);
        scoreAdapter = new HighScoreAdapter(PreferenceUtilities.getTopFive(getBaseContext(),2));
        LinearLayoutManager scoreLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        scoreLayoutManager.setAutoMeasureEnabled(true);
        scoresRecyclerView.setLayoutManager(scoreLayoutManager);
        scoresRecyclerView.setAdapter(scoreAdapter);
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
             Intent homeIntent = new Intent(HighScoreSpeedyActivity.this, MainActivity.class);
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
