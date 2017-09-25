package com.example.android.grabble_v4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.grabble_v4.Utilities.NetworkUtils;
import com.example.android.grabble_v4.data.SingleLetter;
import com.example.android.grabble_v4.data.LetterBag;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener, BoardAdapter.ListItemClickListener {

    List<SingleLetter> bag = new ArrayList<SingleLetter>();
    List<SingleLetter> board = new ArrayList<SingleLetter>();
    List<SingleLetter> builder = new ArrayList<SingleLetter>();
    private BoardAdapter mBoardAdapter;
    private BoardAdapter mBuilderAdapter;
    RecyclerView mBoardRecView;
    RecyclerView mBuilderRecView;
    TextView wordReview;
    ProgressBar pBar;
    TextView mScore;
    int playerScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordReview = (TextView) findViewById(R.id.success_words);
        pBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mScore = (TextView) findViewById(R.id.score);

        //RecycleView reference
        mBoardRecView = (RecyclerView) findViewById(R.id.scrabble_letter_list);
        mBuilderRecView = (RecyclerView) findViewById(R.id.word_builder_list);
        //set Layout
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        GridLayoutManager BuildergridLayoutManager = new GridLayoutManager(this, 4);
        mBoardRecView.setLayoutManager(gridLayoutManager);
        mBuilderRecView.setLayoutManager(BuildergridLayoutManager);
        newGame();
    /*    String example =String.valueOf(list.get(79).getLetter()) +" value: "+ String.valueOf(list.get(5).getValue());
        mLetterBag.setText(example);
        Log.i("tag", "abc");

    */

        mBoardAdapter = new BoardAdapter(this, board, this, R.id.scrabble_letter_list);
        mBoardRecView.setAdapter(mBoardAdapter);

        mBuilderAdapter = new BoardAdapter(this, builder, this, R.id.word_builder_list);
        mBuilderRecView.setAdapter(mBuilderAdapter);


    }


    public void newGame() {
        //create bag
        LetterBag.createScrabbleSet(bag);
        //Log.i("tag", String.valueOf(bag.get(6).getLetter_name()) +" value: "+ String.valueOf(bag.get(5).getLetter_value()));
        //pick initial 4 randomly into board, remove from bag. recycleview=board
        for (int i = 0; i < 4; i++) {
            addLetterToBoard();
        }
        playerScore=0;
        mScore.setText(String.valueOf(playerScore));

    }

    public void addLetterToBoard() {
        RandomSelector randomSelector = new RandomSelector(bag);
        SingleLetter selectedLetter;
        selectedLetter = randomSelector.getRandom();
        //reduce from bag
        for (int j = 0; j < bag.size(); j++) {
            if (bag.get(j).letter_name.equals(selectedLetter.letter_name)) {
                bag.get(j).reduce_letter_probability();
            }
        }
        board.add(selectedLetter);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.get_letter:
                //mLetterBuild.setText(String.valueOf(tilesLeft(bag)));
                if (tilesLeft(bag) == 0) {
                    Toast.makeText(getApplicationContext(), "No tiles lift in bag", Toast.LENGTH_LONG).show();
                    break;
                }
                addLetterToBoard();
                mBoardAdapter.notifyDataSetChanged();
                break;
            case R.id.send_word:
                Log.i("send word", "send word");
                String spellCheckWord = "";
                int addScore=0;
                for (int i = 0; i < builder.size(); i++) {

                    spellCheckWord = spellCheckWord + builder.get(i).getLetter_name();
                    addScore=addScore+builder.get(i).getLetter_value(); // later change this to include only the new letters score

                }
                //wordReview.setText(spellCheckWord);
                URL wordSearchURL = NetworkUtils.buildUrl(spellCheckWord);
                new WordValidator(spellCheckWord, addScore).execute(wordSearchURL);
                //add dictionary check!
                break;
            case R.id.clear_word:
                clearBuilder();
                break;

        }
    }

    public int tilesLeft(List<SingleLetter> list) {
        int totalSum = 0;
        for (SingleLetter item : list) {
            totalSum = totalSum + item.letter_probability;
        }
        return totalSum;
    }

    @Override
    public void onListItemClick(int recyler_id, int clickedItemIndex) {


        //convert to drag and drop!
        switch (recyler_id) {
            case R.id.scrabble_letter_list:
                builder.add(board.get(clickedItemIndex));
                board.remove(board.get(clickedItemIndex));
                mBoardAdapter.notifyDataSetChanged();
                mBuilderAdapter.notifyDataSetChanged();
                break;
            case R.id.word_builder_list:
                board.add(builder.get(clickedItemIndex));
                builder.remove(builder.get(clickedItemIndex));
                mBoardAdapter.notifyDataSetChanged();
                mBuilderAdapter.notifyDataSetChanged();
                break;
        }

    }

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
            pBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
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

            int valid=0;
         //   parseWordResult(wordValidateResults);
           // int valid = parseWordResult(wordValidateResults); //change to get the "scrabble" node , put in try catch incase no reply from server
            wordReview.setText(String.valueOf(valid));

            if (valid == 0) {
               // wordReview.setText(wordValidateResults);
                dialogWrongWord(checkWord);
            } else if (valid == 1) {
                dialogCorrectWord(checkWord);
                clearBuilder();

                playerScore = playerScore +tempScore ;
                // empty builder, pass to myWords. update score.

            }


        }
    }

    public void clearBuilder(){
        int builder_size = builder.size();
        for (int i = builder_size - 1; i >= 0; i--) {
            board.add(builder.get(i));
            builder.remove(builder.get(i));

        }
        mBoardAdapter.notifyDataSetChanged();
        mBuilderAdapter.notifyDataSetChanged();
    }

    public void dialogWrongWord(String word) {
        new AlertDialog.Builder(this).setTitle("TOO BAD")
                .setMessage(word + " is not a valid word. Try again!")
                .setNeutralButton("OK", null).create().show();
    }

    public void dialogCorrectWord(String word) {
        new AlertDialog.Builder(this).setTitle("Hurray")
                .setMessage(word + " is great! Keep it up!")
                .setNeutralButton("Oh Yeah!", null)
              /*  .setPositiveButton("See word definition", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //DICTIONARY from internet... With intent

                    }
                })

              */
                .create().show();
    }

/*
    public int parseWordResult(String xmlString){

       // File file = new File(xmlString);//YOSSI explain stream
//        InputStream targetStream = new FileInputStream();
        InputStream stream = new ByteArrayInputStream(xmlString.getBytes());
        XmlPullParserFactory xmlFactoryObject = null;
        XmlPullParser myParser = null;
        int valid=0;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        try {
           myParser = xmlFactoryObject.newPullParser();
            myParser.setInput(stream,null);
            int event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT)  {
                String name=myParser.getName();
                switch (event){
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.END_TAG:
                        if(name.equals("scrabble")){
                            valid = Integer.parseInt(myParser.getAttributeValue(null,"value"));
                        }
                        break;
                }
                try {
                    event = myParser.next();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
return valid;

    }
*/
}
