package com.example.android.grabble_v4;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.grabble_v4.utilities.PreferenceUtilities;

/**
 * Created by user on 08/11/2017.
 */

public class HighScoreFragment extends Fragment implements View.OnClickListener {
    TextView mTitle;
    RecyclerView scoresRecyclerView;
    HighScoreAdapter scoreAdapter;
    TextView mDate;
    TextView mScore;
    View activityView;
    Button newGameButton;
    Button backButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.high_score_scroll_view, container, false);
        int countDownInd = getArguments().getInt("gameType");
        mTitle = (TextView) rootView.findViewById(R.id.high_score_title);
        activityView = (View) rootView.findViewById(R.id.high_score_activity_layout);
        mTitle = (TextView) rootView.findViewById(R.id.high_score_title);
        scoresRecyclerView = (RecyclerView) rootView.findViewById(R.id.score_list_view);
        setContent(countDownInd, rootView);

            return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        int countDownInd = getArguments().getInt("gameType");
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.new_game_in_high_score) {
            Intent homeIntent = new Intent(getActivity(), MainActivity.class);
            homeIntent.putExtra(MainActivity.BUTTON_TAPPED, R.string.new_game);
            getActivity().setResult(MainActivity.RESULT_CODE_HIGH_SCORE_FRAGMENT, homeIntent);
            getActivity().finish();

        }

        if (view.getId() == R.id.back_to_game_in_high_score) {
            }
        }


    public void setContent(int gameType, View view){
        switch (gameType) {
            case 0:
                mTitle.setText(getString(R.string.classic_high_score));
                break;
            case 1:
                mTitle.setText(getString(R.string.moderate_high_score));
                break;
            case 2:
                mTitle.setText(getString(R.string.speedy_high_score));
                break;
        }

        scoreAdapter = new HighScoreAdapter(PreferenceUtilities.getTopFive(getActivity().getBaseContext(), gameType), getActivity().getBaseContext()); //0= classic mode
        LinearLayoutManager scoreLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        scoreLayoutManager.setAutoMeasureEnabled(true);
        scoresRecyclerView.setLayoutManager(scoreLayoutManager);
        scoresRecyclerView.setAdapter(scoreAdapter);

        scoresRecyclerView.setLayoutFrozen(true);

        if (scoreAdapter.highScoreList == null) {
            mDate = (TextView) view.findViewById(R.id.Date_title);
            mDate.setText(getString(R.string.no_high_score));
            mScore = (TextView) view.findViewById(R.id.score_title);
            mScore.setVisibility(View.GONE);
        }


    }


}

