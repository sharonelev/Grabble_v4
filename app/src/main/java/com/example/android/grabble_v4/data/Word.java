package com.example.android.grabble_v4.data;

import java.util.List;

/**
 * Created by user on 07/11/2017.
 */

public class Word {
    int order_num;
    String theWord;
    List<Word> wordHistory;

    public Word(String w, List<Word> prevWord){
        theWord=w;
        if (prevWord!=null)
        {
            for(Word word:prevWord)
            wordHistory.add(word);
        }


    }
    public Word(String w){

        theWord=w;
    }

    public int getOrder_num() {

        return order_num;
    }

    public String getTheWord() {
        return theWord;
    }

    public void setOrder_num(int order_num) {

        this.order_num = order_num;
    }

    public void setTheWord(String theWord) {

        this.theWord = theWord;
    }

    public List<Word> getWordHistory() {
        return wordHistory;
    }
}
