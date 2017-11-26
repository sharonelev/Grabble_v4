package com.example.android.grabble_v4;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.grabble_v4.Utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;


/**************************** call execute.wordvalidator() *************************/
/*               URL wordSearchURL = NetworkUtils.buildUrlCheckWord(spellCheckWord);
                checkWord= new WordValidator(spellCheckWord, addScore).execute(wordSearchURL);
*/
/**************************** ORIGINAL CODE FOR WORD VALIDATOR USING API*************************/

/**
 * Created by user on 23/11/2017.
 */

public class checkWordFromAPI {
/*
    public class WordValidator extends AsyncTask<URL, Void, String>

    {
        String checkWord;
        int tempScore;

        public WordValidator(String aWord, int tempAddScore) {
            checkWord = aWord;
            tempScore = tempAddScore;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pBar.setVisibility(View.VISIBLE); //progress bar
        }

        @Override
        protected String doInBackground(URL... params) {

            boolean valid;

            String wordValidateResults = null;


            //API CODE:
            URL searchUrl = params[0];
            try {
                wordValidateResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return wordValidateResults;
        }

        @Override
        protected void onPostExecute(String wordValidateResults) {
            if(wordValidateResults==null){
                Toast.makeText(getBaseContext(),"Something went wrong. Try again later.", Toast.LENGTH_LONG).show();
                setEnableAll(true);
                pBar.setVisibility(View.INVISIBLE);
                return;
            }

            super.onPostExecute(wordValidateResults);

            pBar.setVisibility(View.INVISIBLE);
            String valid = null;

            if(wordValidateResults.equals("timeout")){
                Toast.makeText(getBaseContext(),"Something went wrong. Try again later.", Toast.LENGTH_LONG).show();
                setEnableAll(true);
                return;
            }
            try {
                valid = parseWordResult(wordValidateResults);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("valid", "did not get valid result");
            }

            //  valid="1"; //TODO REMVOE AFTER TESTING
            {

                if (valid.equals("0")) {
                    // wordReview.setText(wordValidateResults);
                    if (showWrongPopUp)
                        dialogWrongWord(checkWord);
                    else {
                        Toast.makeText(getApplicationContext(), checkWord + " is not a valid word", Toast.LENGTH_LONG).show();
                        setEnableAll(true);
                    }
                } else if (valid.equals("1")) {
                    if (countDownInd != 0) {
                        countDownTimer.cancel();
                    }
                    if (showCorrectPopUp)
                        dialogCorrectWord(checkWord, tempScore);
                    else {
                        afterDialogSuccess(tempScore);
                    }
                } else  if(valid.equals("")){
                    Toast.makeText(getBaseContext(),"Something went wrong. Try again later.", Toast.LENGTH_LONG).show();
                    setEnableAll(true);
                }
            }
        }
    }

*/
}
