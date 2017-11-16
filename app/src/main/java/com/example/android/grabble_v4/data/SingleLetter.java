package com.example.android.grabble_v4.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.style.ParagraphStyle;
import android.widget.TextView;

/**
 * Created by user on 9/19/2017.
 */

public class SingleLetter{

    public String letter_name;
    public int letter_value;
    public int letter_probability;

    public SingleLetter(String name, int num, int prob){
        letter_name=name;
        letter_value=num;
        letter_probability=prob;
    }

    public SingleLetter(String name){
        letter_name=name;

    }
    public String getLetter_name(){
        return letter_name;
    }

    public int getLetter_value(){
        return letter_value;
    }

    public int getLetter_probability() {
        return letter_probability;
    }

    public void setLetter_name(String let){
        this.letter_name=let;
    }

    public void setLetter_value(int letter_value) {
        this.letter_value = letter_value;
    }

    public void setLetter_probability(int letter_probability) {
        this.letter_probability = letter_probability;
    }

    public void reduce_letter_probability(){
        this.letter_probability=letter_probability-1;
    }



}
