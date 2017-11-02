package com.example.android.grabble_v4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android.grabble_v4.Utilities.PreferenceUtilities;

public class HighScoreSpeedyActivity extends Activity implements View.OnClickListener{

    RecyclerView scoresRecyclerView;
    HighScoreAdapter scoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_speedy);
        scoresRecyclerView = (RecyclerView)findViewById(R.id.score_list_view);
        scoreAdapter = new HighScoreAdapter(PreferenceUtilities.getTopFive(getBaseContext()));
        LinearLayoutManager scoreLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        scoreLayoutManager.setAutoMeasureEnabled(true);
        scoresRecyclerView.setLayoutManager(scoreLayoutManager);
        scoresRecyclerView.setAdapter(scoreAdapter);

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
