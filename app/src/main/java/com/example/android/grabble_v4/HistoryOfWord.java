package com.example.android.grabble_v4;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.android.grabble_v4.data.Word;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;


public class HistoryOfWord extends AppCompatActivity {

    RecyclerView mWordsLevel;
    HistoryWordAdapter historyWordAdapter;
    Word finalWord;
    List<List<Word>> wordLevel;
    List<Word> tempLevel;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_of_word);
        mWordsLevel= (RecyclerView)findViewById(R.id.word_history_full_rv);
        wordLevel = new ArrayList<>();
        historyWordAdapter = new HistoryWordAdapter(this, wordLevel);
        mWordsLevel.setAdapter(historyWordAdapter);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        mWordsLevel.setLayoutManager(linearLayoutManager);

        if(Hawk.contains(MainActivity.WORD)) {
            finalWord = Hawk.get(MainActivity.WORD);
            findWord(finalWord);
            historyWordAdapter.notifyDataSetChanged();

        }
            else
                Log.i("Error", "no word sent");


        ActionBar actionBar = this.getSupportActionBar();
        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }



    //RECURSION
    public void findWord(Word word){
        if(word.getNodeLevel()>0){ //has child
            for(Word child:word.getWordHistory())
                findWord(child); //send all his children
        }
            printWord(word);

            return;

    }
    public void printWord(Word word){
        int nodeLevel= word.getNodeLevel();
        Log.i(String.valueOf(nodeLevel),word.getTheWord());
        int level_size = wordLevel.size();
        if(level_size>nodeLevel){
            wordLevel.get(nodeLevel).add(word);
        } else
        {
            tempLevel = new ArrayList<>();
            tempLevel.add(word);
            wordLevel.add(nodeLevel,tempLevel);
            //historyWordAdapter.notifyDataSetChanged();
        }
    }
}
