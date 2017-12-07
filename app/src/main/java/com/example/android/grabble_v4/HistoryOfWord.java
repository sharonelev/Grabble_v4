package com.example.android.grabble_v4;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

import com.example.android.grabble_v4.data.Word;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;


public class HistoryOfWord extends DialogFragment {

    RecyclerView mWordsLevel;
    HistoryWordAdapter historyWordAdapter;
    Word finalWord;
    List<List<Word>> wordLevel;
    List<Word> tempLevel;
    LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.activity_history_of_word,container,true);

        mWordsLevel= (RecyclerView)rootview.findViewById(R.id.word_history_full_rv);
        wordLevel = new ArrayList<>();
        historyWordAdapter = new HistoryWordAdapter(getContext(), wordLevel);
        mWordsLevel.setAdapter(historyWordAdapter);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        mWordsLevel.setLayoutManager(linearLayoutManager);

        if(Hawk.contains(MainActivity.WORD)) {
            finalWord = Hawk.get(MainActivity.WORD);
            findWord(finalWord);
            historyWordAdapter.notifyDataSetChanged();

        }
            else
                Log.i("Error", "no word sent");




        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        int screenHeightDP = Hawk.get(MainActivity.DEVICE_HEIGHT); //
        int screenWidthDP = Hawk.get(MainActivity.DEVICE_WIDTH); //
        int tileHeight= Hawk.get(MainActivity.TILE_HEIGHT);
        int tileWidth= Hawk.get(MainActivity.TILE_WIDTH);
        int screenHeightPX = MainActivity.dpToPx(getContext(),screenHeightDP); //
        int screenWidthPX = MainActivity.dpToPx(getContext(),screenWidthDP); //
        int levels= finalWord.getNodeLevel()+1;
        int longestWord=finalWord.getTheWord().length();
        longestWord=Math.max(longestWord,4);
        int fragmentHeight;
        int fragmentWidth;
        fragmentWidth = (int) Math.min(tileWidth * (longestWord+3), screenWidthPX*0.99);
        fragmentHeight= (int) Math.min(tileHeight * (levels*1.3+3), screenHeightPX*0.95);
                ///getView().getLayoutParams().WRAP_CONTENT;
        Window window = getDialog().getWindow();

        if(levels>4)
        {

        }else {
            fragmentHeight = (int) (screenHeightPX / 1.5);
        }

        window.setLayout(fragmentWidth,fragmentHeight);
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
