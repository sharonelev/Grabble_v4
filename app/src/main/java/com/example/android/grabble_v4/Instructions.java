package com.example.android.grabble_v4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by user on 03/10/2017.
 */

public class Instructions extends AppCompatActivity implements View.OnClickListener{
    /* Field to store our TextView */
    TextView bbl;
    ScrollView instruction_scrollview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);
        instruction_scrollview= (ScrollView)findViewById(R.id.inst_scroll);
        bbl= (TextView) findViewById(R.id.get_bbl_instructions);
        bbl.setText(R.string.show_bbl_instructions);


        /* Typical usage of findViewById... */
        //  mDisplayText = (TextView) findViewById(R.id.tv_display);

    ActionBar actionBar = this.getSupportActionBar();

    // Set the action bar back button to look like an up button
        if (actionBar != null) {
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.bubble_layout_instructions){
            Intent homeIntent = new Intent(Instructions.this, MainActivity.class);
            homeIntent.putExtra(MainActivity.BUTTON_TAPPED, R.string.show_bbl_instructions);
            setResult(MainActivity.RESULT_CODE_INSTRUCTIONS, homeIntent);
            finish();

        }
    }
}