package com.example.android.grabble_v4;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.orhanobut.hawk.Hawk;
import com.pixelcan.inkpageindicator.InkPageIndicator;

/**
 * Created by user on 08/11/2017.
 */

public class HighScoreScreenSlideDialog extends DialogFragment implements OnClickListener {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3 ;
    private static final String GAME_TYPE= "gameType";
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    private int countDownInd;
    Button newGameButton;
    Button backButton;
    Bundle bundle;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

public static HighScoreScreenSlideDialog createInstance(int gameType){

    HighScoreScreenSlideDialog highScoreScreenSlideDialog = new HighScoreScreenSlideDialog();
    Bundle bundle = new Bundle();
    bundle.putInt(GAME_TYPE,gameType);
    highScoreScreenSlideDialog.setArguments(bundle);
    return highScoreScreenSlideDialog;
}
    public void onResume()
    { //set size
        super.onResume();
        int screenHeightDP = Hawk.get(MainActivity.DEVICE_HEIGHT); //
        int screenWidthDP = Hawk.get(MainActivity.DEVICE_WIDTH); //
        int screenHeightPX = MainActivity.dpToPx(getContext(),screenHeightDP); //
        int screenWidthPX = MainActivity.dpToPx(getContext(),screenWidthDP); //
        Window window = getDialog().getWindow();
        if(screenWidthDP<600)
            window.setLayout(ViewPager.LayoutParams.WRAP_CONTENT, (int) (screenHeightPX/1.5));
        else
            window.setLayout( (int) (screenWidthPX/1.5), (int) (screenHeightPX/1.5));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.high_score_view_pager,container,true);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        InkPageIndicator inkPageIndicator = (InkPageIndicator) rootview.findViewById(R.id.ink_indicator);
        mPager = (ViewPager) rootview.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        countDownInd=getArguments().getInt(GAME_TYPE);
        mPager.setAdapter(mPagerAdapter);
        inkPageIndicator.setViewPager(mPager);
        inkPageIndicator.bringToFront();
        mPager.setCurrentItem(countDownInd);
        backButton = (Button) rootview.findViewById(R.id.back_to_game_in_high_score);
        newGameButton = (Button) rootview.findViewById(R.id.new_game_in_high_score);
        backButton.setOnClickListener(this);
        newGameButton.setOnClickListener(this);
        Activity thisActivity = getActivity();
        if(thisActivity instanceof HomeActivity)
        {newGameButton.setVisibility(View.GONE);}
        return rootview;
    }


    @Override
    public void onClick(View view) {

        Log.i("dialog","any btn");
        switch (view.getId()) {
            case R.id.new_game_in_high_score:
                Log.i("dialog", "new_game");
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.newGame();
        }
        getDialog().dismiss();
        }

    public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{

        public ScreenSlidePagerAdapter( FragmentManager fm) {

            super(fm);
        }

        @Override
        public Fragment getItem(int position) { //use position
            HighScoreFragment fragment = new HighScoreFragment();
            bundle=new Bundle();
            bundle.putInt(GAME_TYPE,position);
            fragment.setArguments(bundle);
            return fragment;

                        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }



    }

}



