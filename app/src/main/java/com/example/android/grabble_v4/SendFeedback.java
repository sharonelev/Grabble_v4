package com.example.android.grabble_v4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by user on 14/10/2017.
 */

public class SendFeedback extends AppCompatActivity implements View.OnClickListener{
    /* Field to store our TextView */

    EditText feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_feedback);
        feedback =(EditText) findViewById(R.id.feedback_text);


        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    public class sendAsync extends AsyncTask<Intent, Void, String>{


        @Override
        protected String doInBackground(Intent... params) {
            Intent email = params[0];
            startActivityForResult(Intent.createChooser(email, "Choose an Email client :"),123);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialogThanks();
        }
    }

    @Override
    public void onClick(View view) {
        Intent email = new Intent(Intent.ACTION_SEND);
        String to="apps.by.sha@gmail.com";
        String subject="Feedback from TILES app";
        String message=feedback.getText().toString();
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, message);
//need this to prompts email client only.
        email.setType("message/rfc822");

        new sendAsync().execute(email);
    }

    public void dialogThanks() {
        new AlertDialog.Builder(this).setTitle("Thank you for your feedback!")
               .setOnCancelListener(new DialogInterface.OnCancelListener() {
                   @Override
                   public void onCancel(DialogInterface dialogInterface) {
                       Context context = SendFeedback.this;
                       Class destinationActivity = MainActivity.class;
                       Intent main_intent= new Intent(context,destinationActivity);
                       startActivity(main_intent);
                   }
               })
                .setNeutralButton("Back to Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Context context = SendFeedback.this;
                        Class destinationActivity = MainActivity.class;
                        Intent main_intent= new Intent(context,destinationActivity);
                        startActivity(main_intent);
                    }
                }).create().show();


    }
}