package com.example.android.grabble_v4.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 07/11/2017.
 */

public class Word{
    int nodeLevel = 0;
    String theWord;
    List<Word> wordHistory;

    public Word(String w, List<Word> prevWord) {
        theWord = w;
        wordHistory = new ArrayList<>();
        if (prevWord != null) {
            for (Word wordInList : prevWord) {
                wordHistory.add(wordInList);
                nodeLevel = Math.max(nodeLevel, wordInList.getNodeLevel() + 1);
            }
        } else nodeLevel = 0;


    }

    public Word(String w) {

        theWord = w;
    }

    public int getNodeLevel() {

        return nodeLevel;
    }

    public String getTheWord() {
        return theWord;
    }

    public void setNodeLevel(int order_num) {

        this.nodeLevel = order_num;
    }

    public void setTheWord(String theWord) {

        this.theWord = theWord;
    }

    public List<Word> getWordHistory() {
        return wordHistory;
    }
}