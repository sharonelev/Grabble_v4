package com.example.android.grabble_v4;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.grabble_v4.data.SingleLetter;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 25/09/2017.
 */

public class myWordsAdapter extends RecyclerView.Adapter<myWordsAdapter.WordViewHolder> {
    Context mContext;
    List<List<SingleLetter>> myWords;
    ListWordClickListener mOnClickListener;



   public interface ListWordClickListener {
        void onWordItemClick(int clickedWord, int clickedLetter);
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
        holder.isEnabled=true;
        int screenWidthDP=Hawk.get(MainActivity.DEVICE_WIDTH);
        int screenWidthPX = MainActivity.dpToPx(mContext,screenWidthDP);
        int tileSize;
        //tile size incluudes spaces
        if(screenWidthDP<400)
            tileSize=(int) screenWidthPX/10;
        else
            tileSize=(int) screenWidthPX/11;
        holder.itemView.getLayoutParams().width=(holder.mList.size())*(tileSize)+tileSize;

    }

    @Override
    public int getItemCount() {
        return myWords.size();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder implements BoardAdapter.LetterClickListener {//,View.OnClickListener{

        RecyclerView eachWordRecView;
        BoardAdapter mBoardAdapter;
        List<SingleLetter> mList = new ArrayList<>();
        boolean isEnabled= true;


        public void mySetEnabled(boolean state){
            isEnabled= state;
        }


        public WordViewHolder(View itemView) {

            super(itemView);

            eachWordRecView = (RecyclerView) itemView.findViewById(R.id.each_word);
            setDivider();
            LinearLayoutManager wordLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            eachWordRecView.setLayoutManager(wordLayoutManager);
            mBoardAdapter = new BoardAdapter(mContext, mList, this, R.id.myWordsRecyclerView,null);
            eachWordRecView.setAdapter(mBoardAdapter);

        }


        @Override
        public void onLetterClick(int view_id, int clickedItemIndex) { // a letter in a myWords was clicked
            if(isEnabled) {
                Log.i("MyWordsAdapter", "onLetterClick " + clickedItemIndex);
                int position = getAdapterPosition();
                mList.remove(clickedItemIndex);
                mList.add(clickedItemIndex, new SingleLetter("", 0, 0));
                mBoardAdapter.notifyItemRemoved(clickedItemIndex);
                mBoardAdapter.notifyItemInserted(clickedItemIndex);
                mOnClickListener.onWordItemClick(position, clickedItemIndex);
            }
       }

       //add dividers between letters according to screen width
        public void setDivider(){
            int deviceWidthDP =Hawk.get(MainActivity.DEVICE_WIDTH);
           //int deviceWidthPX = MainActivity.dpToPx(mContext,deviceWidthDP);
            DividerItemDecoration divider;
            divider= new DividerItemDecoration(eachWordRecView.getContext(),DividerItemDecoration.HORIZONTAL);
           /* if(deviceWidthDP<400)
                divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.line_divider_s));
            else */
                if(deviceWidthDP<400)
                divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.line_divider_s));
            else
                divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.line_divider_l));
            eachWordRecView.addItemDecoration(divider);

    }


    }
}
