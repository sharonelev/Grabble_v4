package com.example.android.grabble_v4;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.grabble_v4.data.SingleLetter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);

        new Handler().postDelayed(new Runnable() {
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
        },LETTER_TIME_OUT);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mList.set(0,letterL);
                mList.set(1,letterA);
                mList.set(2,letterR);
                mList.set(3,letterG);
                mList.add(4,letterE);
                mWelcomeAdapter.notifyDataSetChanged();
            }
        },LETTER_TIME_OUT*2);
        new Handler().postDelayed(new Runnable() {
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
        },LETTER_TIME_OUT*3);
        new Handler().postDelayed(new Runnable() {
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
        },LETTER_TIME_OUT*4);
    }
}
