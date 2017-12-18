package com.example.android.grabble_v4;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.grabble_v4.data.LetterBag;
import com.example.android.grabble_v4.data.SingleLetter;

import java.util.ArrayList;
import java.util.List;


public class DetailsActivity extends AppCompatActivity {

    RecyclerView tilesDetails;
    DetailsAdapter detailsAdapter;
    LinearLayoutManager linearLayoutManager;
    List<SingleLetter> letterBag =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        LetterBag.createScrabbleSet(letterBag);
        tilesDetails=(RecyclerView)findViewById(R.id.details_recyclerview);
        detailsAdapter = new DetailsAdapter(letterBag);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        tilesDetails.setAdapter(detailsAdapter);
        tilesDetails.setLayoutManager(linearLayoutManager);



        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

            }


}
