package com.example.android.grabble_v4;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.android.grabble_v4.data.Word;
import com.orhanobut.hawk.Hawk;


public class HistoryOfWord extends AppCompatActivity {

    RecyclerView mWordsLevel;
    Word finalWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_of_word);
        mWordsLevel= (RecyclerView)findViewById(R.id.word_history_full_rv);

        if(Hawk.contains(MainActivity.WORD)) {
            finalWord = Hawk.get(MainActivity.WORD);
            findWord(finalWord);

        }
            else
                Log.i("Error", "no word sent");


        ActionBar actionBar = this.getSupportActionBar();
        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    public void findWord(Word word){ //RECURSION
        if(word.getNodeLevel()>0){ //has child
            for(Word child:word.getWordHistory())
                findWord(child); //send all his children
        }

            printWord(word);
            return;

    }

    public void printWord(Word word){
        Log.i(String.valueOf(word.getNodeLevel()),word.getTheWord());
    }


}
