package com.example.android.grabble_v4.data;



import java.util.List;

/**
 * Created by user on 9/19/2017.
 */

public class LetterBag {



    public static List<SingleLetter> createScrabbleSet(List<SingleLetter> list) {


        list.add(new SingleLetter("A",1,9));//9
        list.add(new SingleLetter("B",3,2));
        list.add(new SingleLetter("C",3,2));
        list.add(new SingleLetter("D",2,4));
        list.add(new SingleLetter("E",1,12));//12
        list.add(new SingleLetter("F",4,2));
        list.add(new SingleLetter("G",2,3));
        list.add(new SingleLetter("H",4,2));
        list.add(new SingleLetter("I",1,9));//9
        list.add(new SingleLetter("J",8,1));
        list.add(new SingleLetter("K",5,1));
        list.add(new SingleLetter("L",1,4));
        list.add(new SingleLetter("M",3,2));
        list.add(new SingleLetter("N",1,6));
        list.add(new SingleLetter("O",1,8));//8
        list.add(new SingleLetter("P",3,2));
        list.add(new SingleLetter("Q",10,1));
        list.add(new SingleLetter("R",1,6));
        list.add(new SingleLetter("S",1,4));
        list.add(new SingleLetter("T",1,6));
        list.add(new SingleLetter("U",1,4));
        list.add(new SingleLetter("V",4,2));
        list.add(new SingleLetter("W",4,2));
        list.add(new SingleLetter("X",8,1));
        list.add(new SingleLetter("Y",4,2));
        list.add(new SingleLetter("Z",10,1));

        return list;
    }
}
