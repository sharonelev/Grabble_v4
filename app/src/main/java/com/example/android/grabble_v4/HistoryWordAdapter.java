package com.example.android.grabble_v4;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.grabble_v4.data.LetterBag;
import com.example.android.grabble_v4.data.SingleLetter;
import com.example.android.grabble_v4.data.Word;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 07/12/2017.
 */

public class HistoryWordAdapter  extends RecyclerView.Adapter<HistoryWordAdapter.WordViewHolder> {

    List<List<Word>> mWordList;
    Context mContext;
    List<SingleLetter> letterBag= new ArrayList<SingleLetter>();;

    public HistoryWordAdapter(Context context, List<List<Word>> wordList) {

        mWordList = wordList;
        mContext = context;


    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.word_level;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        HistoryWordAdapter.WordViewHolder viewHolder = new HistoryWordAdapter.WordViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {


        if (mWordList.get(position) == null) {
            return;
        }

        breakWordToSingleLetters(holder, position);
        holder.wordsAdapter.notifyDataSetChanged();
        holder.setDivider();

    }



    public void createLetterBag() {
        LetterBag.createScrabbleSet(letterBag);
    }

    public void breakWordToSingleLetters(WordViewHolder holder, int position) {
        createLetterBag();
       // for (List<Word> wordList : mWordList) {  //for level at position

            for (Word word : mWordList.get(position)) {  //for every word in level at position
                List<SingleLetter> oneWord = new ArrayList<>();
                for (int i = 0; i < word.getTheWord().length(); i++)  //for every letter
                {
                    String letter = String.valueOf(word.getTheWord().charAt(i));
                    for (SingleLetter bagLetter : letterBag) //for every letter in the bag
                    {
                        if (bagLetter.getLetter_name().equals(letter)) {
                            SingleLetter newLetter = bagLetter;
                            oneWord.add(newLetter);
                            break;
                        }
                    }

                }//iterated through the word
                holder.mLetterWordList.add(oneWord);
            }
       // }
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder {

        RecyclerView wordInLevel;
        myWordsAdapter wordsAdapter;
        LinearLayoutManager linearLayoutManager;
        List<List<SingleLetter>> mLetterWordList;


        public WordViewHolder(View itemView) {
            super(itemView);
            mLetterWordList= new ArrayList<>();
            wordInLevel = (RecyclerView) itemView.findViewById(R.id.word_in_level_rv);
            wordsAdapter = new myWordsAdapter(mContext, mLetterWordList, null, null, true);
            linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            wordInLevel.setAdapter(wordsAdapter);
            wordInLevel.setLayoutManager(linearLayoutManager);

        }


        public void setDivider(){

            //int deviceWidthPX = MainActivity.dpToPx(mContext,deviceWidthDP);
            DividerItemDecoration divider;
            divider= new DividerItemDecoration(wordInLevel.getContext(),DividerItemDecoration.VERTICAL);
                 divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.line_divider_s));
            wordInLevel.addItemDecoration(divider);

        }

    }

}