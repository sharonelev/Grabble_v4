package com.example.android.grabble_v4.Utilities;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by user on 15/11/2017.
 */

public class myStaggeredGridLayoutManager extends StaggeredGridLayoutManager{

    public myStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public void setGapStrategy(int gapStrategy) {
        super.setGapStrategy(gapStrategy);
    }
}
