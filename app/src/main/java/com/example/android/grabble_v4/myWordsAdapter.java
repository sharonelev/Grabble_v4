package com.example.android.grabble_v4;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.android.grabble_v4.data.SingleLetter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 25/09/2017.
 */

public class myWordsAdapter extends RecyclerView.Adapter<myWordsAdapter.WordViewHolder> {
    Context mContext;
    List<List<SingleLetter>> myWords;
 private   ListWordClickListener mOnClickListener;



   public interface ListWordClickListener {
        // YOSSI explanation
        void onWordItemClick(int clickedItemIndex);
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

//        Log.i("on bind", myWords.get(position).get(1).getLetter_name());

        if(myWords.get(position)==null){

            return;}

      holder.mList.addAll(myWords.get(position));
      // holder.wordInList=position;
    //   holder.mBoardAdapter.notifyDataSetChanged();//causes mywords to duplicate!!!


       /* holder.eachWordRecView.setLayoutManager(holder.gridLayoutManager);
        holder.mBoardAdapter = new BoardAdapter(mContext, holder.mList ,holder.listener, R.id.myWordsRecyclerView, position);
        holder.eachWordRecView.setAdapter(holder.mBoardAdapter);
*/

    }

    @Override
    public int getItemCount() {
        return myWords.size();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            //, BoardAdapter.ListItemClickListener{
        RecyclerView eachWordRecView;
        BoardAdapter mBoardAdapter;
        List<SingleLetter> mList = new ArrayList<>();
       BoardAdapter.ListItemClickListener listener;
        GridLayoutManager gridLayoutManager;
      //  int wordInList;

        public WordViewHolder(View itemView) {

            super(itemView);

            eachWordRecView = (RecyclerView)itemView.findViewById(R.id.each_word);

            listener=new MainActivity();
            gridLayoutManager= new GridLayoutManager(mContext,7);

            eachWordRecView.setLayoutManager(gridLayoutManager); //this prevents onClick to work!!
            mBoardAdapter = new BoardAdapter(mContext, mList ,listener, R.id.myWordsRecyclerView );
            eachWordRecView.setAdapter(mBoardAdapter);

            eachWordRecView.setLayoutFrozen(true);

            itemView.setOnClickListener(this);
        }

    @Override
        public void onClick(View view) {

            //never gets here.....

            Log.i("WordAdapter","word clicked");
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onWordItemClick(clickedPosition);
        }


    }

}
