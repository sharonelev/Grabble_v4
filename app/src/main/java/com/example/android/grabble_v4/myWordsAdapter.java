package com.example.android.grabble_v4;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.android.grabble_v4.data.SingleLetter;

import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.StaggeredGridLayoutManager;
/**
 * Created by user on 25/09/2017.
 */

public class myWordsAdapter extends RecyclerView.Adapter<myWordsAdapter.WordViewHolder> {
    Context mContext;
    List<List<SingleLetter>> myWords;
    ListWordClickListener mOnClickListener;



   public interface ListWordClickListener {
        // YOSSI explanation
        void onWordItemClick(int clickedWord, int clickedLetter);
        ;
    }

    public  myWordsAdapter(Context context,  List<List<SingleLetter>> aWords, ListWordClickListener listener){
        mContext=context;
        myWords=aWords;
        mOnClickListener=listener;

    }
    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.my_words_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        myWordsAdapter.WordViewHolder viewHolder = new myWordsAdapter.WordViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position)

    {
        if(myWords.get(position)==null){

            return;}

       holder.mList.clear();
        holder.mBoardAdapter.notifyDataSetChanged();
       holder.mList.addAll(myWords.get(position));
        holder.mBoardAdapter.notifyDataSetChanged();
        //mBoardAdapter notify change is on all letters in the word

      //  holder.itemView.setLayoutParams(new StaggeredGridLayoutManager.LayoutParams(StaggeredGridLayoutManager.LayoutParams.FILL_PARENT,
        //        StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT));
        holder.itemView.getLayoutParams().width=holder.mList.size()*100+(holder.mList.size()-1)*20+100;
    }

    @Override
    public int getItemCount() {
        return myWords.size();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder implements BoardAdapter.LetterClickListener {//,View.OnClickListener{

        RecyclerView eachWordRecView;
        BoardAdapter mBoardAdapter;
        List<SingleLetter> mList = new ArrayList<>();
        BoardAdapter.LetterClickListener listener;
        GridLayoutManager gridLayoutManager;


        public WordViewHolder(View itemView) {

            super(itemView);

            eachWordRecView = (RecyclerView) itemView.findViewById(R.id.each_word);

            //      listener= new MainActivity();
            LinearLayoutManager wordLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

            eachWordRecView.setLayoutManager(wordLayoutManager); //this prevents onClick to work!!
            mBoardAdapter = new BoardAdapter(mContext, mList, this, R.id.myWordsRecyclerView);

            //try sending word in list to the adapter and erase the onclick methods here. first erase and see if mywords still gets emptied
            eachWordRecView.setAdapter(mBoardAdapter);


            //       eachWordRecView.setLayoutFrozen(true);
            // itemView.setOnClickListener(this);
        }

        @Override
        public void onLetterClick(int view_id, int clickedItemIndex) { // a letter in a myWords was clicked
            Log.i("MyWordsAdapter", "onLetterClick " +clickedItemIndex);
          //  Log.i("click in word adapter", "do u get here?");
            int position = getAdapterPosition();
           // Log.i("adapter position", String.valueOf(position)); //word number
            //Log.i("letter position", String.valueOf(clickedItemIndex));

            //Log.i("letter position", "after word click listener");
            mList.remove(clickedItemIndex);
            mList.add(clickedItemIndex, new SingleLetter("", 0, 0));
            mBoardAdapter.notifyItemRemoved(clickedItemIndex);
            mBoardAdapter.notifyItemInserted(clickedItemIndex);

            mOnClickListener.onWordItemClick(position, clickedItemIndex);
       }
    }

}
