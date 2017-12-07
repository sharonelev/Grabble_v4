package com.example.android.grabble_v4;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Parcelable;
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
import android.widget.TextView;

import com.example.android.grabble_v4.data.SingleLetter;
import com.example.android.grabble_v4.data.Word;
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
    List<Word> mWordList;
    boolean mHistory;


   public interface ListWordClickListener {
        void onWordItemClick(int clickedWord, int clickedLetter);
    }



    public  myWordsAdapter(Context context,  List<List<SingleLetter>> aWords, ListWordClickListener listener, List<Word> wordList, boolean history){
        mContext=context;
        myWords=aWords;
        mOnClickListener=listener;
        mWordList = wordList;
        mHistory = history;

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
    public void onBindViewHolder(final WordViewHolder holder, final int position)

    {
        if(myWords.get(position)==null){

            return;}

       holder.mList.clear();
        holder.mBoardAdapter.notifyDataSetChanged();
       holder.mList.addAll(myWords.get(position));
        holder.mBoardAdapter.notifyDataSetChanged();
        holder.isEnabled=true;


    //POP UP MENU FOR WORD
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
                                Intent intent =  new Intent(mContext, HistoryOfWord.class);
                                Hawk.put(MainActivity.WORD, mWordList.get(position));
                                mContext.startActivity(intent);
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

        //WORD SIZE
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
        int menuWidth = MainActivity.dpToPx(mContext,20);
        holder.itemView.getLayoutParams().width=(holder.mList.size())*(tileSize)+tileSize+ menuWidth;

        if(mHistory)
        {
            int wordPoints=0;
            holder.wordMenu.setVisibility(View.GONE);
            for(SingleLetter letter:holder.mList){
                wordPoints=letter.getLetter_value()+wordPoints;
            }
            if(holder.mList.size()>=7)
                wordPoints=holder.mList.size()+wordPoints;
            holder.pointsFromWord.setText("+"+String.valueOf(wordPoints));
            holder.pointsFromWord.setVisibility(View.VISIBLE);
            holder.itemView.getLayoutParams().width=(holder.mList.size())*(tileSize)+tileSize*2;  //+MainActivity.spToPx(mContext,mContext.getResources().getDimension(R.dimen.points_in_history));

        } else  // in myWords
        checkWordComplete(position,holder); //word position


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
        TextView pointsFromWord;


        public void mySetEnabled(boolean state){
            isEnabled= state;
        }


        public WordViewHolder(View itemView) {

            super(itemView);

            eachWordRecView = (RecyclerView) itemView.findViewById(R.id.each_word);
            wordMenu = (ImageButton) itemView.findViewById(R.id.word_options);
            pointsFromWord= (TextView) itemView.findViewById(R.id.points_earned);
            setDivider();
            LinearLayoutManager wordLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            eachWordRecView.setLayoutManager(wordLayoutManager);
            mBoardAdapter = new BoardAdapter(mContext, mList, this, R.id.myWordsRecyclerView,null);
            eachWordRecView.setAdapter(mBoardAdapter);
           // wordMenu.setVisibility(View.VISIBLE);

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

    private void checkWordComplete(int wordToCheck, final WordViewHolder holder) {
        for(SingleLetter letter:myWords.get(wordToCheck)){
            if(letter.getLetter_name()=="")
                return;
        } //all letters aren't blank
        holder.wordMenu.setVisibility(View.VISIBLE);

    }
}
