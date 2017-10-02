package com.example.android.grabble_v4;

import com.example.android.grabble_v4.data.SingleLetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by user on 9/19/2017.
 */

public class RandomSelector {

    Random rand = new Random();
    int totalSum = 0;
    List<SingleLetter> items = new ArrayList<SingleLetter>();

    RandomSelector(List<SingleLetter> list) {
        items=list;
        for(SingleLetter item : items) {
            totalSum = totalSum + item.letter_probability;
        }
    }

    public SingleLetter getRandom() {

        int index = rand.nextInt(totalSum);
        int sum = 0;
        int i=0;
        while(sum < index ) {
            sum = sum + items.get(i++).letter_probability;
        }
        return items.get(Math.max(0,i-1));
    }

}
