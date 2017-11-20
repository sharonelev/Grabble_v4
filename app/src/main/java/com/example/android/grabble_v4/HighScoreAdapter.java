package com.example.android.grabble_v4;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.grabble_v4.data.HighScore;

import java.util.List;

/**
 * Created by user on 25/10/2017.
 */


public class HighScoreAdapter extends RecyclerView.Adapter<HighScoreAdapter.ScoreViewHolder>{

    List<HighScore> highScoreList;
    public HighScoreAdapter(List<HighScore> list){
        highScoreList=list;

    }

    @Override
    public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
    Context context = parent.getContext();
    int layoutIdForListItem = R.layout.top_score_view;
    LayoutInflater inflater = LayoutInflater.from(context);
    boolean shouldAttachToParentImmediately = false;

    View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        HighScoreAdapter.ScoreViewHolder viewHolder = new HighScoreAdapter.ScoreViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ScoreViewHolder holder, int position) {
        if(highScoreList.get(position)==null){
            return;
        }

        int score = highScoreList.get(position).getScore();
        String dateTime = highScoreList.get(position).getScoreDate();

        holder.mDateTime.setText(dateTime);
        holder.mScore.setText(String.valueOf(score));
        holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        holder.mDateTime.setTextSize(20);
        holder.mScore.setTextSize(20);

    }

    @Override
    public int getItemCount() {
        if(highScoreList!=null){
        return highScoreList.size();}
        else return 0;
    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder  {

        TextView mScore;
        TextView mDateTime;

        public ScoreViewHolder(View itemView) {
            super(itemView);
            mScore = (TextView) itemView.findViewById(R.id.score_record);
            mDateTime = (TextView) itemView.findViewById(R.id.date_time_record);
        }
    }
}



