package com.example.android.grabble_v4;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.grabble_v4.Utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

//OBSOLETE
/**
 * Created by user on 25/09/2017.
 */

public class ValidateWord extends AsyncTask<URL, Void, String>{

     TextView mTextView;
    ProgressBar pBar;
    String word;
    Context mContext;

    public ValidateWord(TextView tv, ProgressBar progressBar, String wd, Context context){
        mTextView=tv;
        pBar=progressBar;
        word=wd;
        mContext=context;

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

        int valid= 0; //change to get the "scrabble" node , put in try catch incase no reply from server
        //    JSONObject jsonObject =new JSONObject(wordValidateResults);
         // String validWord= jsonObject.getJSONObject("scrabble").toString();
//            mTextView.setText(jsonObject.getString("scrabble"));
        if(valid==0) {
            mTextView.setText(wordValidateResults);
           dialogWrongWord();
        }
        else if(valid==1){
            dialogCorrectWord();
            // empty builder, pass to myWords. update score.

        }



    }


    public void dialogWrongWord(){
        new AlertDialog.Builder(mContext).setTitle("TOO BAD")
            .setMessage(word + " is not a valid word. Try again!")
            .setNeutralButton("OK",null).create().show();}

    public void dialogCorrectWord(){
        new AlertDialog.Builder(mContext).setTitle("Hurray")
                .setMessage(word + " is great! Keep it up!")
                .setNeutralButton("OK",null).create().show();}
    }
