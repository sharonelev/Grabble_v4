package com.example.android.grabble_v4;

import android.content.Context;
import android.net.sip.SipAudioCall;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.grabble_v4.data.SingleLetter;

import java.util.List;

/**
 * Created by user on 9/19/2017.
 */



public class BoardAdapter  extends RecyclerView.Adapter<BoardAdapter.LetterViewHolder>{

    List<SingleLetter> mBoard;


    public BoardAdapter(Context context, List<SingleLetter> list){
        mBoard = list;
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

    @Override
    public void onBindViewHolder(LetterViewHolder holder, int position) {


        //     if (!mCursor.moveToPosition(position))
        //       return; // bail if returned null

        if(mBoard.get(position)==null){
            return;
        }

        // Update the view holder with the information needed to display
        String name = mBoard.get(position).letter_name;
        int value = mBoard.get(position).letter_value;


        holder.mLetter.setText(name);
        holder.mLetterValue.setText(String.valueOf(value));
    }

    @Override
    public int getItemCount() {
        return mBoard.size();
    }



    public class LetterViewHolder extends RecyclerView.ViewHolder {

        TextView mLetter;
        TextView mLetterValue;

        public LetterViewHolder(View itemView) {

            super(itemView);
            mLetter = (TextView) itemView.findViewById(R.id.scrabble_letter_name);
            mLetterValue = (TextView) itemView.findViewById(R.id.scrabble_letter_value);

        }
    }
}
