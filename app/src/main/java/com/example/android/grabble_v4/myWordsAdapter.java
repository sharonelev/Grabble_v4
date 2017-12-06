package com.example.android.grabble_v4;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.transition.Visibility;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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
    public void onBindViewHolder(final WordViewHolder holder, int position)

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
        //tile size includes spaces
        if(screenWidthDP<400)
            tileSize=(int) screenWidthPX/10;
        else if(screenWidthDP<600)
            tileSize=(int) screenWidthPX/11;
        else
            tileSize=(int) screenWidthPX/12;
        //holder.itemView.setBackgroundColor(Color.RED); //For testing only
        holder.itemView.getLayoutParams().width=(holder.mList.size())*(tileSize)+tileSize;


        holder.wordMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mContext, holder.wordMenu, Gravity.NO_GRAVITY, R.attr.actionOverflowMenuStyle, 0);
                //inflating menu from xml resource
                popup.inflate(R.menu.word_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.word_history:
                                //handle menu1 click
                                break;
                            case R.id.word_dictionary:
                                //handle menu2 click
                                break;
                            case R.id.word_share:
                                //handle menu3 click
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                //popup.show();
                MenuPopupHelper menuHelper = new MenuPopupHelper(mContext, (MenuBuilder) popup.getMenu(), holder.itemView);
                menuHelper.setForceShowIcon(true);
                //menuHelper.setGravity(Gravity.END);
                menuHelper.show();
            }
        });


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
        ImageButton wordMenu;


        public void mySetEnabled(boolean state){
            isEnabled= state;
        }


        public WordViewHolder(View itemView) {

            super(itemView);

            eachWordRecView = (RecyclerView) itemView.findViewById(R.id.each_word);
            wordMenu = (ImageButton) itemView.findViewById(R.id.word_options);
            setDivider();
            LinearLayoutManager wordLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            eachWordRecView.setLayoutManager(wordLayoutManager);
            mBoardAdapter = new BoardAdapter(mContext, mList, this, R.id.myWordsRecyclerView,null);
            eachWordRecView.setAdapter(mBoardAdapter);
            wordMenu.setVisibility(View.VISIBLE);

        }


        @Override
        public void onLetterClick(int view_id, int clickedItemIndex) { // a letter in a myWords was clicked
            if(isEnabled) {
                if(wordMenu.getVisibility()!= View.GONE)
                    {wordMenu.setVisibility(View.GONE);}
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
                if(deviceWidthDP<400)
                divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.line_divider_s));
            else
                divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.line_divider_l));
            eachWordRecView.addItemDecoration(divider);

    }


    }
}
