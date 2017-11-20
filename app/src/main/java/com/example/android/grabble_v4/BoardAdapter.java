package com.example.android.grabble_v4;

import android.content.Context;
import android.graphics.Color;
import android.net.sip.SipAudioCall;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.grabble_v4.data.SingleLetter;
import com.orhanobut.hawk.Hawk;

import java.util.List;

/**
 * Created by user on 9/19/2017.
 */



public class BoardAdapter  extends RecyclerView.Adapter<BoardAdapter.LetterViewHolder> {

    List<SingleLetter> mBoard;
    LetterClickListener mOnClickListener;
    int recyclerViewId;
    Context mContext;
    int mTileWidth;
    int mTileHeight;
    TileDimensions tileDimensions;


    public interface LetterClickListener {
        void onLetterClick(int view_id, int clickedItemIndex);
    }

    public interface TileDimensions{
        void getTileDimensions(int tileWidth, int tileHeight);
    }

    public BoardAdapter(Context context, List<SingleLetter> list, LetterClickListener listener, int recycler_id, TileDimensions td){
        mBoard = list;
        mOnClickListener =listener;
        recyclerViewId=recycler_id;
        mContext=context;
        tileDimensions=td;
    };


    @Override
    public LetterViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        Hawk.init(mContext).build();
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.letter_on_board_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        final View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        LetterViewHolder viewHolder = new LetterViewHolder(view);
        if(tileDimensions!=null)
            if(!Hawk.contains(MainActivity.TILE_HEIGHT) || !Hawk.contains(MainActivity.TILE_WIDTH))
                    getTileDimensionsInit(view);
        return viewHolder;
    }

    private void getTileDimensionsInit(final View view) {
         {
            ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        } else {
                            view.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                        mTileHeight = view.getMeasuredHeight();
                        mTileWidth = view.getMeasuredWidth();
                        Hawk.put(MainActivity.TILE_HEIGHT,mTileHeight);
                        Hawk.put(MainActivity.TILE_WIDTH,mTileWidth);
                        tileDimensions.getTileDimensions(mTileWidth, mTileHeight);

                    }
                });
            }
        }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.itemView.setElevation((float) 25);
        }
        int numOfTiles = mBoard.size();

        //shrink tiles for long words
        if(recyclerViewId==R.id.word_builder_list || recyclerViewId==R.id.myWordsRecyclerView) {
            if (numOfTiles <= 10 ){
                holder.mLetter.setTextSize (28);
                holder.mLetterValue.setTextSize(10);
                holder.itemView.getLayoutParams().width=MainActivity.boardTileWidth;
                holder.itemView.getLayoutParams().height=MainActivity.boardTileHeight;
            }
            else if (numOfTiles == 11) {
                holder.mLetter.setTextSize(25);
                holder.mLetterValue.setTextSize(8);
                holder.itemView.getLayoutParams().width=holder.itemView.getLayoutParams().WRAP_CONTENT;
                holder.itemView.getLayoutParams().height=holder.itemView.getLayoutParams().WRAP_CONTENT;
            }
            else if ( numOfTiles >= 12) {
                holder.mLetter.setTextSize(22);
                holder.mLetterValue.setTextSize(7);
                holder.itemView.getLayoutParams().width=holder.itemView.getLayoutParams().WRAP_CONTENT;
                holder.itemView.getLayoutParams().height=holder.itemView.getLayoutParams().WRAP_CONTENT;
            }
        }

        //handle blank placers
            if(name.equals("") ){
                holder.mLetterValue.setVisibility(View.INVISIBLE);
                holder.clickable(holder.itemView,0);
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);

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
                if(recyclerViewId==R.id.welcomeRecyclerView){
                    return;
                }

                int clickedPosition = getAdapterPosition();
                if(clickedPosition<0)
                    return;
                Log.i("Board adpater", "onClick "+clickedPosition);
                mOnClickListener.onLetterClick(recyclerViewId, clickedPosition);
            }
        }
    }
}
