package com.example.android.grabble_v4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.android.grabble_v4.Utilities.PreferenceUtilities;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);



    ActionBar actionBar = this.getSupportActionBar();

    // Set the action bar back button to look like an up button
        if (actionBar != null) {
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}




}
