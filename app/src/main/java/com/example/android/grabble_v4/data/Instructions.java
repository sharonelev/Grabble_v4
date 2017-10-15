package com.example.android.grabble_v4.data;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.grabble_v4.R;

/**
 * Created by user on 03/10/2017.
 */

public class Instructions extends AppCompatActivity {
    /* Field to store our TextView */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);

        /* Typical usage of findViewById... */
        //  mDisplayText = (TextView) findViewById(R.id.tv_display);
    }
}