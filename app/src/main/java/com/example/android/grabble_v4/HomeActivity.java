package com.example.android.grabble_v4;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.android.grabble_v4.data.SingleLetter;
import com.example.android.grabble_v4.Utilities.LocaleHelper;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, BoardAdapter.TileDimensions{

    private static  int LETTER_TIME_OUT=1000;
    private List<SingleLetter> mList = new ArrayList<>();
    RecyclerView recyclerView;
    BoardAdapter mWelcomeAdapter;
    private SingleLetter letterT =new SingleLetter("T",2,0);
    private SingleLetter letterI =new SingleLetter("I",1,0);
    private SingleLetter letterL =new SingleLetter("L",2,0);
    private SingleLetter letterE= new SingleLetter("E",1,0);
    private SingleLetter letterS =new SingleLetter("S",1,0);
    Handler myHandler= new Handler();

    //runnable per tile
    Runnable addI = new Runnable() {
        @Override
        public void run() {
            mList.set(0,letterI);
            mWelcomeAdapter.notifyDataSetChanged();
        }
    };

    Runnable addS = new Runnable() {
        @Override
        public void run() {
            mList.set(0,letterI);
            mList.add(1,letterS);
            mWelcomeAdapter.notifyDataSetChanged();
        }
    };

    Runnable addT = new Runnable() {
        @Override
        public void run() {
            mList.set(0,letterS);
            mList.set(1,letterI);
            mList.add(2,letterT);


            mWelcomeAdapter.notifyDataSetChanged();
        }
    };

    Runnable addL = new Runnable() {
        @Override
        public void run() {
            mList.set(0,letterL);
            mList.set(1,letterI);
            mList.set(2,letterS);
            mList.add(3,letterT);
            mWelcomeAdapter.notifyDataSetChanged();
        }
    };
    Runnable addE = new Runnable() {
        @Override
        public void run() {
            mList.set(0,letterT);
            mList.set(1,letterI);
            mList.set(2,letterL);
            mList.set(3,letterE);
            mList.add(4,letterS);
            mWelcomeAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LocaleHelper.setLocale(this,"en");
        recyclerView =(RecyclerView) findViewById(R.id.welcomeRecyclerView);
        LinearLayoutManager WelcomeLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(WelcomeLayoutManager);
        mList.add(letterI);
        mWelcomeAdapter = new BoardAdapter(this, mList, null, R.id.welcomeRecyclerView, this);
        recyclerView.setAdapter(mWelcomeAdapter);
        recyclerView.setEnabled(false);
        myHandler.postDelayed(addI,LETTER_TIME_OUT);
        myHandler.postDelayed(addS,LETTER_TIME_OUT*2);
        myHandler.postDelayed(addT ,LETTER_TIME_OUT*3);
        myHandler.postDelayed(addL,LETTER_TIME_OUT*4);
        myHandler.postDelayed(addE,LETTER_TIME_OUT*5);
    }

    @Override
    public void onClick(View view) {
        //game choice
        switch (view.getId()) {
            case R.id.button_classic_game:
                myHandler.removeCallbacksAndMessages(null);
                endActivity(view.getId());
                break;
            case R.id.button_moderate_game:
                myHandler.removeCallbacksAndMessages(null);
                endActivity(view.getId());
                break;
            case R.id.button_speedy_game:
                myHandler.removeCallbacksAndMessages(null);
                endActivity(view.getId());
                break;
        }

    }

    public void endActivity(int gameType) {
        Intent homeIntent = new Intent(HomeActivity.this, MainActivity.class);
        homeIntent.putExtra(MainActivity.GAME_TYPE,gameType);
        startActivity(homeIntent);
        finish();
    }

    @Override
    public void getTileDimensions(int tileWidth, int tileHeight) {
        Log.i("height", String.valueOf(MainActivity.pxToDp(this,tileHeight)));
        Log.i("width", String.valueOf(MainActivity.pxToDp(this,tileWidth)));
    }


    //MENU METHODS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_game, menu);
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context context = HomeActivity.this;
        Class destinationActivity;
        Intent intent;
        switch (item.getItemId()) {

            case  R.id.instructions_menu:
                destinationActivity = Instructions.class;
                intent = new Intent(context, destinationActivity);
                intent.putExtra(MainActivity.PREV_ACTIVITY,"home");
                startActivity(intent);
                return true;
            case R.id.high_score_in_menu:
                openHighScoreDialog();
                return true;
            case R.id.send_feedback:
                destinationActivity = SendFeedback.class;
                intent = new Intent(context, destinationActivity);
                startActivity(intent);
                return true;
            case R.id.settings:
                destinationActivity = SettingsActivity.class;
                intent= new Intent(context, destinationActivity);
                startActivity(intent);
                return true;
        } //switch
        return super.onOptionsItemSelected(item); //if not action_search
    }
    public void openHighScoreDialog(){

        HighScoreScreenSlideDialog.createInstance(0).show(getSupportFragmentManager(),"Dialog Fragment");
    }
}
