package com.example.android.grabble_v4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.grabble_v4.Utilities.PreferenceUtilities;

public class HighScoreActivity extends Activity implements View.OnClickListener{

    RecyclerView scoresRecyclerView;
    HighScoreAdapter scoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
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
        Intent homeIntent = new Intent(HighScoreActivity.this, MainActivity.class);
        startActivity(homeIntent);
        finish();
}
    }
}
