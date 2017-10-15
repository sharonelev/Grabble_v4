package com.example.android.grabble_v4;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android.grabble_v4.data.SingleLetter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    private static  int SPLASH_TIME_OUT=7000;
    private static  int LETTER_TIME_OUT=1500;
    private List<SingleLetter> mList = new ArrayList<>();
    RecyclerView recyclerView;
    BoardAdapter mWelcomeAdapter;
    private SingleLetter letterG =new SingleLetter("G",1,0);
    private SingleLetter letterR =new SingleLetter("R",1,0);
    private SingleLetter letterA =new SingleLetter("A",1,0);
    private SingleLetter letterB= new SingleLetter("B",1,0);
    private SingleLetter letterL =new SingleLetter("L",1,0);
    private SingleLetter letterE =new SingleLetter("E",1,0);
    Handler myHandler= new Handler();
    Runnable screenRun =new Runnable() {
        @Override
        public void run() {
            endActivity();
        }
    };


    Runnable addE = new Runnable() {
        @Override
        public void run() {
            //     mList.clear();
            //    mWelcomeAdapter.notifyDataSetChanged();
            mList.set(0,letterG);
            mList.set(1,letterE);
            mList.set(2,letterA);
            mList.add(3,letterR);


            mWelcomeAdapter.notifyDataSetChanged();
        }
    };

    Runnable addL = new Runnable() {
        @Override
        public void run() {
            mList.set(0,letterL);
            mList.set(1,letterA);
            mList.set(2,letterR);
            mList.set(3,letterG);
            mList.add(4,letterE);
            mWelcomeAdapter.notifyDataSetChanged();
        }
    };

    Runnable addB = new Runnable() {
        @Override
        public void run() {
            mList.set(0,letterG);
            mList.set(1,letterA);
            mList.set(2,letterR);
            mList.set(3,letterB);
            mList.set(4,letterL);
            mList.add(5,letterE);


            mWelcomeAdapter.notifyDataSetChanged();
        }
    };

    Runnable addB2 = new Runnable() {
        @Override
        public void run() {
            mList.set(0,letterG);
            mList.set(1,letterR);
            mList.set(2,letterA);
            mList.set(3,letterB);
            mList.set(4,letterB);
            mList.set(5,letterL);
            mList.add(6,letterE);

            mWelcomeAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView =(RecyclerView) findViewById(R.id.welcomeRecyclerView);
        LinearLayoutManager WelcomeLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(WelcomeLayoutManager);
        mList.add(letterR);
        mList.add(letterA);
        mList.add(letterG);

        mWelcomeAdapter = new BoardAdapter(this, mList, null, R.id.welcomeRecyclerView);
        recyclerView.setAdapter(mWelcomeAdapter);
        myHandler.postDelayed(screenRun,SPLASH_TIME_OUT);
        myHandler.postDelayed(addE,LETTER_TIME_OUT);
        myHandler.postDelayed(addL ,LETTER_TIME_OUT*2);
        myHandler.postDelayed(addB,LETTER_TIME_OUT*3);
        myHandler.postDelayed(addB2,LETTER_TIME_OUT*4);
    }

    @Override
    public void onClick(View view) {
        //SKIP tapped
        myHandler.removeCallbacksAndMessages(null);
        endActivity();

    }


    public void endActivity() {
        Intent homeIntent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(homeIntent);
        finish();
    }


}
