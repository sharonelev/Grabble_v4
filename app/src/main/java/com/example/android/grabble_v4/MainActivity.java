package com.example.android.grabble_v4;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.grabble_v4.Utilities.GridSpacingItemDecoration;
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
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener, BoardAdapter.LetterClickListener, myWordsAdapter.ListWordClickListener {

    List<SingleLetter> bag = new ArrayList<SingleLetter>();
    List<SingleLetter> board = new ArrayList<SingleLetter>();
    List<SingleLetter> builder = new ArrayList<SingleLetter>();
    List<int[]> builderLetterTypes = new ArrayList<>();// 0=from board 1=from myWords
    List<List<SingleLetter>> myWords =new ArrayList<>();
    private BoardAdapter mBoardAdapter;
    private BoardAdapter mBuilderAdapter;
    private myWordsAdapter mWordsAdapter;
    RecyclerView mBoardRecView;
    RecyclerView mBuilderRecView;
    RecyclerView mMyWordsRecView;
    TextView wordReview;
    ProgressBar pBar;
    TextView mScore;
    int playerScore;
    int dialogFlag=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // wordReview = (TextView) findViewById(R.id.success_words);
        pBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mScore = (TextView) findViewById(R.id.score);

        //RecycleView reference
        mBoardRecView = (RecyclerView) findViewById(R.id.scrabble_letter_list);
        mBuilderRecView = (RecyclerView) findViewById(R.id.word_builder_list);
        mMyWordsRecView = (RecyclerView) findViewById(R.id.myWordsRecyclerView);
        //set Layout
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        GridLayoutManager BuilderGridLayoutManager = new GridLayoutManager(this, 7);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        mMyWordsRecView.setLayoutManager(linearLayout);
        mBoardRecView.setLayoutManager(gridLayoutManager);
        mBuilderRecView.setLayoutManager(BuilderGridLayoutManager);
      /*  int spacingInPixels = getResources().getDimensionPixelSize(R.id.scrabble_letter_list);
        mBoardRecView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));*/
        newGame();

        mBoardAdapter = new BoardAdapter(this, board, this, R.id.scrabble_letter_list);
        mBoardRecView.setAdapter(mBoardAdapter);

        mBuilderAdapter = new BoardAdapter(this, builder, this, R.id.word_builder_list);
        mBuilderRecView.setAdapter(mBuilderAdapter);

        mWordsAdapter = new myWordsAdapter(this, myWords, this);
        mMyWordsRecView.setAdapter(mWordsAdapter);

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
                    Toast.makeText(getApplicationContext(), "No tiles lift in bag - GAME OVER", Toast.LENGTH_LONG).show();
                    break;
                }
                addLetterToBoard();
                mBoardAdapter.notifyDataSetChanged();
                playerScore--; //reduce a point for each tile the user adds
                mScore.setText(String.valueOf(playerScore));
                break;
            case R.id.send_word:
                Log.i("send word", "send word");
                //test validity of word:
                //dictionary and complies to rules

                String spellCheckWord = "";
                int addScore=0;
                for (int i = 0; i < builder.size(); i++) {

                    spellCheckWord = spellCheckWord + builder.get(i).getLetter_name();
                    addScore=addScore+builder.get(i).getLetter_value(); // later change this to include only the new letters score

                }
                //wordReview.setText(spellCheckWord);
                if(spellCheckWord.equals("")){
                    Toast.makeText(this,"No word",Toast.LENGTH_SHORT).show();
                    break;
                }
                // left a word broken in myWords
                int wordBroken=0;
                for(int i=0; i<myWords.size();i++){
                    int blankCounter=0;
                    for(int j=0; j<myWords.get(i).size(); j++){
                        if(myWords.get(i).get(j).getLetter_name().equals("")){
                            blankCounter++;
                        }
                    }
                    if(blankCounter!=myWords.get(i).size() && blankCounter>0){
                        wordBroken=1;
                        break;
                    }
                }
                if(wordBroken==1){//word rules don't comply
                    Toast.makeText(this,"Can't use part of a word",Toast.LENGTH_SHORT).show();
                    break;
                }

                //must change word order:
                //THE WORD MUST be checked as opposed to original because vest can become vests while the s is in the middle is from the board

                //word must be 4+
                if(builder.size()<3){
                    Toast.makeText(this,"word must be at least 3 letters long",Toast.LENGTH_SHORT).show();
                    break;
                }
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
    public void onWordItemClick(int clickedWord, int clickedLetter) {
        Log.i("Clicked Letter in Word",myWords.get(clickedWord).get(clickedLetter).letter_name);

        builder.add(myWords.get(clickedWord).get(clickedLetter));
        mBuilderAdapter.notifyDataSetChanged();
        builderLetterTypes.add(placer(1,clickedWord,clickedLetter));

       myWords.get(clickedWord).remove(clickedLetter);
       myWords.get(clickedWord).add(clickedLetter,new SingleLetter("",0,0));
      //  mWordsAdapter.notifyItemChanged(clickedWord); notify to mBoardAdapter occurs in myWordsAdapter

    }

    @Override
    public void onLetterClick(int recyler_id, int clickedItemIndex) {


        //convert to drag and drop!
        switch (recyler_id) {
            case R.id.scrabble_letter_list: //board
                builder.add(board.get(clickedItemIndex));
                //board.remove(board.get(clickedItemIndex));// why not
                board.remove(clickedItemIndex);
                board.add(clickedItemIndex,new SingleLetter("",0,0));
                builderLetterTypes.add(placer(0,-1,clickedItemIndex));
                mBoardAdapter.notifyDataSetChanged();
                mBuilderAdapter.notifyDataSetChanged();
                break;
            case R.id.word_builder_list: //what if it needs to be returned to myWords??
                int origin= builderLetterTypes.get(clickedItemIndex)[0];
                int wordIndex = builderLetterTypes.get(clickedItemIndex)[1];
                int letterIndex = builderLetterTypes.get(clickedItemIndex)[2];
                switch (origin) {
                    case 0:
                        board.remove(letterIndex);//remove place holder
                        board.add(letterIndex, builder.get(clickedItemIndex));
                        mBoardAdapter.notifyDataSetChanged();
                        break;
                    case 1: //myWords(wordplace.letterplace)
                        if(wordIndex<0){
                            Log.i("case word index=-1","shouldn't get here");
                            break;} //per caution. shouldn't get here
                        myWords.get(wordIndex).remove(letterIndex);
                        myWords.get(wordIndex).add(letterIndex,builder.get(clickedItemIndex));
                        mWordsAdapter.notifyItemChanged(wordIndex); //sometimes it's ok and sometimes the letter disappears. debug. LESERUGIN. it works evey other time
                        break;
                }  //in any case:

                    builder.remove(clickedItemIndex);
                    builderLetterTypes.remove(clickedItemIndex);
                    mBuilderAdapter.notifyItemRemoved(clickedItemIndex);
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
            //TODO return this when API works!
              try {
                wordValidateResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

            } catch (IOException e) {
                e.printStackTrace();
            }
            //wordValidateResults="temp";
            return wordValidateResults;
        }

        @Override
        protected void onPostExecute(String wordValidateResults) {
            super.onPostExecute(wordValidateResults);

            pBar.setVisibility(View.INVISIBLE);

            String valid = null; //change to get the "scrabble" node , put in try catch incase no reply from server
            if(wordValidateResults==null){
                Toast.makeText(getApplicationContext(), "no reply from server", Toast.LENGTH_SHORT).show();
                return;
            }
            //TODO return this when API works!
            try {
                valid = parseWordResult(wordValidateResults);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("valid","did not get valid result");
            } //remove this when API works!

            // valid="1";
            {
             //   wordReview.setText(valid);
//if valid remove place holders
                if (valid.equals("0")) {
                    // wordReview.setText(wordValidateResults);
                    dialogWrongWord(checkWord);
                } else if (valid.equals("1")) {
                    dialogCorrectWord(checkWord, tempScore); //TODO apply dialogue (removed for testing)

                }

            }
        }
    }
    public void afterDialogSuccess(int tempScore){
        addWordToMyWords(tempScore);
      //  if (board.size() == 0) {
            if (tilesLeft(bag) == 0) {
                Toast.makeText(getApplicationContext(), "No tiles lift in bag  - GAME OVER", Toast.LENGTH_LONG).show();
                return;
            }
            addLetterToBoard(); //when word played a new letter is added to board without penalty
            mBoardAdapter.notifyDataSetChanged();
        //}

    }


    public void clearBuilder() { ///change to clear to mywords as well
        int builder_size = builder.size();


        for (int i = builder_size - 1; i >= 0; i--) {


            int origin = builderLetterTypes.get(i)[0];
            int wordIndex = builderLetterTypes.get(i)[1];
            int letterIndex = builderLetterTypes.get(i)[2];
            switch (origin) {
                case 0: //board
                    board.remove(letterIndex);//remove place holder
                    board.add(letterIndex, builder.get(i));
                   //mBoardAdapter.notifyDataSetChanged();
                    break;
                case 1: //myWords(wordplace.letterplace)
                    if (wordIndex < 0) {
                        Log.i("case word index=-1", "shouldn't get here");
                        break;
                    } //per caution. shouldn't get here
                    myWords.get(wordIndex).remove(letterIndex); //remove space holder
                    myWords.get(wordIndex).add(letterIndex, builder.get(i));

                   // mWordsAdapter.notifyDataSetChanged(); //sometimes it's ok and sometimes the letter disappears. debug. LESERUGIN. it works evey other time
                    break;
            }
                //    board.add(builder.get(i));
                    builderLetterTypes.remove(i);
                    builder.remove(i);


        }
            mBoardAdapter.notifyDataSetChanged();
            mBuilderAdapter.notifyDataSetChanged();
            mWordsAdapter.notifyDataSetChanged();
        }

    public void dialogWrongWord(String word) {
        new AlertDialog.Builder(this).setTitle("TOO BAD")
                .setMessage(word + " is not a valid word. Try again!")
                .setNeutralButton("OK", null).create().show();
    }


    public void dialogCorrectWord(String word, final int score) {

        //check if reused words
        //int reusedFlag=0;
        String extraScore="";
        for(int[] letter:builderLetterTypes){
         if(letter[0]==1) { //at least one letter from reused word
           //  reusedFlag = 1;
             extraScore=" You get to keep the score for the original word as well!"; //replace original with word..? later on

             break;
         }
        }

        new AlertDialog.Builder(this).setTitle("Hurray")
                .setMessage(word + " is great! Keep it up!" + extraScore)
                .setNeutralButton("Oh Yeah!", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                afterDialogSuccess(score);
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        afterDialogSuccess(score);
                    }
                })
                .setPositiveButton("See word definition", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                  //      dialogFlag=1;
                        //DICTIONARY from internet... With intent - activity WordDefinition -to be continued
                        //after dictionary back to main screen withoiut dialog box
                        afterDialogSuccess(score);
                    }
                }).create().show();

    }

    public void addWordToMyWords(  int tempScore){

        List<SingleLetter> newWord = new ArrayList<>();

        //move letters to myWords
        int builder_size=builder.size();
        List<Integer> tempBoardLetters = new ArrayList<>();
        List<Integer> tempMyWordsLetters = new ArrayList<>();

        for (int i = 0; i < builder_size; i++) {
            newWord.add(i, builder.get(i));
            if(builderLetterTypes.get(i)[0]==0){
                tempBoardLetters.add(builderLetterTypes.get(i)[2]);
            }
            if(builderLetterTypes.get(i)[0]==1)
            {
                tempMyWordsLetters.add(builderLetterTypes.get(i)[1]); //save word indexes
            }
        }
//SORT templettertypes and remove from board accordingly
        Collections.sort(tempBoardLetters);

        for (int i=tempBoardLetters.size()-1;i>=0;i--){
              //0: from board
            int toRemove = tempBoardLetters.get(i);
            board.remove(toRemove);//remove place holders
        }
        //remove blank word
        Collections.sort(tempMyWordsLetters);
        Set uniqueValues = new HashSet(tempMyWordsLetters); //now unique
        List<Integer> tempMyWordsLettersUnique =  new ArrayList<>(uniqueValues);
        for(int i=uniqueValues.size()-1;i>=0;i--)
        {
            //none broken letter check was done earlier
            int toRemove = tempMyWordsLettersUnique.get(i);
            myWords.remove(toRemove);
            mWordsAdapter.notifyItemRemoved(toRemove);
        }


        builder.clear();
        builderLetterTypes.clear();
        mBuilderAdapter.notifyDataSetChanged();;
        mBoardAdapter.notifyDataSetChanged();
        myWords.add(newWord);
        mWordsAdapter.notifyDataSetChanged();

        playerScore = playerScore + tempScore;
        mScore.setText(String.valueOf(playerScore));

    };


    public String parseWordResult(String xmlString) throws IOException {

        InputStream stream = new ByteArrayInputStream(xmlString.getBytes());
        XmlPullParserFactory xmlFactoryObject = null;
        XmlPullParser myParser = null;
        String valid="";
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
                        if(name.equals("scrabble")){
                            if(myParser.next() == XmlPullParser.TEXT) {
                            valid = myParser.getText();}
                            //(null,"value");

                        }break;

                }
                    event = myParser.next();

            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
return valid;

    }

    public  int[] placer(int letterOrigin, int wordPlace, int letterPlace){
       //List placer = new ArrayList();
        int[] series={letterOrigin,wordPlace,letterPlace};//0: 0= from board 1 = from mywords, 1: word index, 2: letter index
      //  placer.add(series);
        return series;
    }
}
