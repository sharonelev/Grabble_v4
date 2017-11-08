package com.example.android.grabble_v4.data;

/**
 * Created by user on 07/11/2017.
 */

public class Word {
    int order_num;
    String theWord;

    public Word(int i, String w){
        order_num=i;
        theWord=w;
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
}
