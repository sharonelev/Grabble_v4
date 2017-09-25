package com.example.android.grabble_v4;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.grabble_v4.Utilities.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by user on 25/09/2017.
 */

public class ValidateWord extends AsyncTask<URL, Void, String>{

     TextView mTextView;
    ProgressBar pBar;
    public ValidateWord(TextView tv, ProgressBar progressBar){
        mTextView=tv;
        pBar=progressBar;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(URL... params) {
        URL searchUrl =params[0];
        String wordValidateResults = null;
        try {
            wordValidateResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordValidateResults;
    }

    @Override
    protected void onPostExecute(String wordValidateResults) {
        super.onPostExecute(wordValidateResults);
        pBar.setVisibility(View.INVISIBLE);
        mTextView.setText(wordValidateResults);
    }

  /*  protected void onPostExecute(String wordValidateResults) {
        super.onPostExecute(wordValidateResults);
        mTextView.setText(wordValidateResults);
    }*/



}
