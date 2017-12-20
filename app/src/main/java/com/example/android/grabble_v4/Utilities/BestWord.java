package com.example.android.grabble_v4.Utilities;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.example.android.grabble_v4.data.SingleLetter;
import com.example.android.grabble_v4.data.Word;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 20/12/2017.
 */

public class BestWord {

    public static final String TAG = "BestWord";
    DictionaryDbHelper dbHelper;



    List<SingleLetter> mBoard;
    List<List<SingleLetter>> mMyWords;
    List<Word> mWordsList;
    List<SingleLetter> bestWord;
    Context mContext;

    public BestWord(Context context, List<SingleLetter> boardLetters, List<List<SingleLetter>> myWords, List<Word> words,String theBestWord ){
        mBoard=boardLetters;
        mMyWords=myWords;
        mContext=context;
        mWordsList=words;
        attachDB();
        int[] A = new int[mBoard.size()];
        findBestWord(A, 0);
    }


    public void findBestWord(int A[], int x) {
        //for every board letters combination
        if(x == A.length-1){
            A[x]=0;
            boardCombination(A);
            A[x]=1;
            boardCombination(A);
            return;
        }
        A[x]=0;
        findBestWord(A,x+1);
        A[x]=1;
        findBestWord(A,x+1);

    }
    public void boardCombination(int A[]){
        String combination= "";
        for(int i=0; i<A.length; i++){
            if(A[i]==1)
                {
                    combination= combination+mBoard.get(i).getLetter_name();
                }
        }
        if(combination=="")
        {return;
        }
        //check without attaching to word
        if(combination.length()>2)
        {
            char[] arrCh = combination.toCharArray();
            permutation(arrCh, 0, arrCh.length);
        }
        for(Word word:mWordsList) {
            combination =combination+word.getTheWord();
            char[] arrCh = combination.toCharArray();
            permutation(arrCh, 0, arrCh.length);

        }

    }
    public void permutation(char[] arrA, int left, int size) {
        int x;
        if (left == size) {
            String word="";
            for (int i = 0; i < arrA.length; i++) {
                word=word+arrA[i];
            }
            checkWord(word);
            //System.out.print(" ");
        } else {
            for (x = left; x < size; x++) {
                swap(arrA, left, x);
                permutation(arrA, left + 1, size);
                swap(arrA, left, x);
            }
        }
    }

    public void swap(char[] arrA, int i, int j) {
        char temp = arrA[i];
        arrA[i] = arrA[j];
        arrA[j] = temp;
    }


    public boolean checkWord(String combination){
        boolean valid = dbHelper.check_word(combination);
        if(valid)
            Log.i(TAG, String.valueOf(combination));
        return valid;
    }
    //for every word in mywords
    //for every combination of words in mywords + board letters

    public void attachDB(){
        dbHelper = new DictionaryDbHelper(mContext);
        try {
            dbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");
        }

        try {
            dbHelper.openDataBase();
            // dbHelper.get_table();

        }catch(SQLException sqle){

            throw sqle;
        }

    }
}
