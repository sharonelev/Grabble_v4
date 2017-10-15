package com.example.android.grabble_v4;

import android.content.Context;
import android.graphics.Color;
import android.net.sip.SipAudioCall;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.grabble_v4.data.SingleLetter;

import java.util.List;

/**
 * Created by user on 9/19/2017.
 */



public class BoardAdapter  extends RecyclerView.Adapter<BoardAdapter.LetterViewHolder> {

    List<SingleLetter> mBoard;
    LetterClickListener mOnClickListener;
    int recyclerViewId;
    Context mContext;




    public interface LetterClickListener {
        // YOSSI explanation

        void onLetterClick(int view_id, int clickedItemIndex);
    }

    public BoardAdapter(Context context, List<SingleLetter> list, LetterClickListener listener, int recycler_id){
        mBoard = list;
      mOnClickListener =listener;
        recyclerViewId=recycler_id;
        mContext=context;
    };
    @Override
    public LetterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.letter_on_board_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        LetterViewHolder viewHolder = new LetterViewHolder(view);

        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(LetterViewHolder holder, int position) {

        if(mBoard.get(position)==null){
            return;
        }

        // Update the view holder with the information needed to display
        String name = mBoard.get(position).letter_name;
        int value = mBoard.get(position).letter_value;

        holder.mLetter.setText(name);
        holder.mLetterValue.setText(String.valueOf(value));

        int numOfTiles = mBoard.size();
        if(recyclerViewId==R.id.word_builder_list||recyclerViewId==R.id.myWordsRecyclerView){
           /* if(numOfTiles<=7){
                holder.itemView.getLayoutParams().width = 130;
                holder.itemView.getLayoutParams().height = 140;
                holder.mLetter.setTextSize(35);
                holder.mLetterValue.setTextSize(20);
            }*/
            if(numOfTiles<=8){
                holder.itemView.getLayoutParams().width = 135;//130
                holder.itemView.getLayoutParams().height = 160;//140
                holder.mLetter.setTextSize(30);
                holder.mLetterValue.setTextSize(15);
            }
            if(numOfTiles==9){
                holder.itemView.getLayoutParams().width = 115;//130
                holder.itemView.getLayoutParams().height = 140;//140
                holder.mLetter.setTextSize(25);
                holder.mLetterValue.setTextSize(14);
            }
            if(numOfTiles>=10) {
                holder.itemView.getLayoutParams().width = 100;//130
                holder.itemView.getLayoutParams().height = 120;//140
                holder.mLetter.setTextSize(20);
                holder.mLetterValue.setTextSize(13);
                //make margin thinner, perhaps in mainactivity
            }





        }

        if(name.equals("") ){
            holder.mLetterValue.setVisibility(View.INVISIBLE);
            holder.clickable(holder.itemView,0);
            //if(recyclerViewId==R.id.myWordsRecyclerView){
                 holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            //}
            //else{
             //   holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            //}
        }

        else {
            holder.mLetterValue.setVisibility(View.VISIBLE);
            holder.itemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.border));
            holder.clickable(holder.itemView,1);


        }
    }

    @Override
    public int getItemCount() {
        return mBoard.size();
    }



    public class LetterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mLetter;
        TextView mLetterValue;

        public LetterViewHolder(View itemView) {

            super(itemView);
            mLetter = (TextView) itemView.findViewById(R.id.scrabble_letter_name);
            mLetterValue = (TextView) itemView.findViewById(R.id.scrabble_letter_value);
            itemView.setOnClickListener(this);


        }

        public void clickable(View view, int trigger){
            switch (trigger){
                case 0:
                view.setOnClickListener(null);
                    break;
                case 1:
                view.setOnClickListener(this);
                    break;
            }
        }

        @Override
        public void onClick(View view) {
            {
                int clickedPosition = getAdapterPosition();
                Log.i("letter clicked",String.valueOf(clickedPosition));
                mOnClickListener.onLetterClick(recyclerViewId, clickedPosition);
            }

        }


    }

}
