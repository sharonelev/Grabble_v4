package com.example.android.grabble_v4;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.grabble_v4.data.LetterBag;
import com.example.android.grabble_v4.data.SingleLetter;

import java.util.List;

/**
 * Created by user on 18/12/2017.
 */

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailViewHolder> {

    List<SingleLetter> letterBag;

    public DetailsAdapter(List<SingleLetter> letters){
    letterBag=letters;
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.letter_details_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        DetailsAdapter.DetailViewHolder viewHolder = new DetailsAdapter.DetailViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {

        if(letterBag.get(position)==null)
            return;

        holder.letter.setText(letterBag.get(position).getLetter_name());
        holder.value.setText(String.valueOf(letterBag.get(position).getLetter_value()));
        holder.freq.setText(String.valueOf(letterBag.get(position).getLetter_probability()));

    }


    @Override
    public int getItemCount() {
        return letterBag.size();
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder {

        TextView letter;
        TextView value;
        TextView freq;


        public DetailViewHolder(View itemView) {
            super(itemView);
            letter=(TextView)itemView.findViewById(R.id.details_letter_name);
            value=(TextView)itemView.findViewById(R.id.details_letter_value);
            freq=(TextView)itemView.findViewById(R.id.details_letter_freq);
        }
    }
}