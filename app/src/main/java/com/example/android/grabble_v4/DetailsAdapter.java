package com.example.android.grabble_v4;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by user on 18/12/2017.
 */

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailViewHolder> {
    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder {

        public DetailViewHolder(View itemView) {
            super(itemView);
        }
    }
}