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
        //mBoardAdapter notify change is on all letters in the word

      //  holder.itemView.setLayoutParams(new StaggeredGridLayoutManager.LayoutParams(StaggeredGridLayoutManager.LayoutParams.FILL_PARENT,
        //        StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT));
        int screenWidth = Hawk.get(MainActivity.DEVICE_WIDTH);
        int tileSize =(int) screenWidth/11;
        holder.itemView.getLayoutParams().width=(holder.mList.size())*(tileSize)+tileSize;
        //holder.itemView.setBackgroundColor(Color.RED);
                // ((100*1.25)*holder.mList.size()+75);
                //holder.mList.size()*100+(holder.mList.size()-1)*20+100;
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
  //          eachWordRecView.setEnabled(state);
            isEnabled= state;
        }


        public WordViewHolder(View itemView) {

            super(itemView);

            eachWordRecView = (RecyclerView) itemView.findViewById(R.id.each_word);
            setDivider();
            LinearLayoutManager wordLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

            eachWordRecView.setLayoutManager(wordLayoutManager); //this prevents onClick to work!!
            mBoardAdapter = new BoardAdapter(mContext, mList, this, R.id.myWordsRecyclerView);

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

        public void setDivider(){
            int deviceWidth =Hawk.get(MainActivity.DEVICE_WIDTH);
            DividerItemDecoration divider;
            divider= new DividerItemDecoration(eachWordRecView.getContext(),DividerItemDecoration.HORIZONTAL);
            if(deviceWidth<800)
                divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.line_divider_s));
            else if(deviceWidth<1000)
                divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.line_divider_m));
            else
                divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.line_divider_l));
            eachWordRecView.addItemDecoration(divider);

    }


    }
}
