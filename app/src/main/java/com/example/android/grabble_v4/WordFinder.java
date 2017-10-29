package com.example.android.grabble_v4;

import android.util.Log;

import com.example.android.grabble_v4.data.SingleLetter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 26/10/2017.
 */

public class WordFinder {
    List<SingleLetter> tempBoard=new ArrayList<SingleLetter>();;
    List<List<SingleLetter>> tempMyWords=new ArrayList<>();

    public WordFinder(List<SingleLetter> board , List<int[]> builderLetterTypes ,List<SingleLetter> builder ,List<List<SingleLetter>> myWords){

        //"clear" builder

        int builder_size = builder.size();
        for (int i = builder_size - 1; i >= 0; i--) {
            int origin = builderLetterTypes.get(i)[0];
            int wordIndex = builderLetterTypes.get(i)[1];
            int letterIndex = builderLetterTypes.get(i)[2];
            switch (origin) {
                case 0: //board
                    board.remove(letterIndex);
                    board.add(letterIndex, builder.get(i));
                    break;
                case 1: //myWords(wordplace.letterplace)
                    if (wordIndex < 0) {
                        Log.i("case word index=-1", "shouldn't get here");
                        break;
                    } //per caution. shouldn't get here
                    myWords.get(wordIndex).remove(letterIndex); //remove space holder
                    myWords.get(wordIndex).add(letterIndex, builder.get(i));
                    break;
            }
            //    board.add(builder.get(i));
            builderLetterTypes.remove(i);
            builder.remove(i);

        }
        tempBoard =board;
        tempMyWords=myWords;
    } //end of constructor


    //for letter in builder
        //for word in mywords
            //attach letter to word
                //swap letters - recursion
                    //test dictionary VERY HEAVY!!!

}
