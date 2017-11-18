package com.example.android.grabble_v4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.example.android.grabble_v4.Utilities.BubbleInstructions;
import com.example.android.grabble_v4.Utilities.NetworkUtils;
import com.example.android.grabble_v4.Utilities.PreferenceUtilities;
import com.example.android.grabble_v4.Utilities.ShakeDetector;
import com.example.android.grabble_v4.data.Instructions;
import com.example.android.grabble_v4.data.LetterBag;
import com.example.android.grabble_v4.data.SendFeedback;
import com.example.android.grabble_v4.data.SingleLetter;
import com.example.android.grabble_v4.data.Word;
import com.orhanobut.hawk.Hawk;
import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.view.View.GONE;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;


public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        BoardAdapter.LetterClickListener,
        myWordsAdapter.ListWordClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener
{

    //RecyclerView elements
    List<SingleLetter> bag = new ArrayList<SingleLetter>();
    List<SingleLetter> board = new ArrayList<SingleLetter>();
    List<SingleLetter> builder = new ArrayList<SingleLetter>();
    List<int[]> builderLetterTypes = new ArrayList<>();// 0=from board 1=from myWords
    List<List<SingleLetter>> myWords = new ArrayList<>();
    List<Word> wordList = new ArrayList<>();
    private BoardAdapter mBoardAdapter;
    private BoardAdapter mBuilderAdapter;
    private myWordsAdapter mWordsAdapter;
    RecyclerView mBoardRecView;
    RecyclerView mBuilderRecView;
    RecyclerView mMyWordsRecView;
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager BuilderLayoutManager;
    StaggeredGridLayoutManager myWordsStaggeredManager;
    HighScoreScreenSlideDialog highScoreScreenSlideDialog;

    //global values
    int playerScore;
    int lettersLeft;
    int gameType;
    int game_limit;  //including 4 from start//
    int countDownInd; //0 = classic. 1=moderate. 2=speedy.
    long toEndTimer = 0;
    boolean initalizeTimers;
    RelativeLayout scoreRelativeLayout;
    //preferences
    public boolean showCorrectPopUp;
    public boolean showWrongPopUp;
    public boolean shakeToGetLetter;
    public boolean animatePoints;
    //UI elements
    ProgressBar pBar;
    TextView mScore;
    TextView mTiles;
    Button getLetter;
    Button playWord;
    Button clearWord;
    CountDownTimer countDownTimer = null;
    TextView countDownView;
    TextView pointsAnimation;
    // Shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    //Device specs
    public static final String DEVICE_HEIGHT = "Device_Height_px";
    public static final String DEVICE_WIDTH = "Device_Width_px";
    public int deviceHeight;
    public int deviceWidth;
    //add letter to board sources
    public static final String NEW_GAME = "new_game";
    public static final String MODERATE_GET_LETTER = "moderate_mid_timer";
    public static final String TIME_UP = "time_up";
    public static final String CLASSIC_GET_LETTER = "get_letter";
    public static final String AFTER_PLAYED_WORD = "after_played_word";
    //results from other activities
    public final static int RESULT_CODE_HIGH_SCORE_FRAGMENT = 123;
    public final static int RESULT_CODE_INSTRUCTIONS = 456;
    public final static String BUTTON_TAPPED = "Button_tapped";
    //first time bubble instructions
    public BubbleLayout bubbleLayout;
    public TextView bubbleText;
    public TextView bubbleX;
    public final static String SAW_BUBBLE_TOUR = "saw_bubble_tour";
    public boolean showingBubblesFlag=false;



    // LIFE CYCLE EVENTS
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //initializations
        Log.i("lifecycleEvent","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent homeScreen = getIntent();
        shakeDetectorInit();
        setDeviceDimensions();
        gameType = homeScreen.getIntExtra("game_type", R.id.button_classic_game);
        initalizeTimers=false;

        switch (gameType) {
            case R.id.button_classic_game:
                countDownInd = 0;
                break;
            case R.id.button_moderate_game:
                countDownInd = 1;
                break;
            case R.id.button_speedy_game:
                countDownInd = 2;
                break;
        }

        //UI elements initialization
        getLetter = (Button) findViewById(R.id.get_letter);
        playWord = (Button) findViewById(R.id.send_word);
        clearWord = (Button) findViewById(R.id.clear_word);
        pBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mScore = (TextView) findViewById(R.id.score);
        mTiles = (TextView) findViewById(R.id.tiles_left);
        scoreRelativeLayout = (RelativeLayout)findViewById(R.id.bottom_relative_layout);
        countDownView = (TextView) findViewById(R.id.linearTimer);
        //RecycleView reference
        mBoardRecView = (RecyclerView) findViewById(R.id.scrabble_letter_list);
        mBuilderRecView = (RecyclerView) findViewById(R.id.word_builder_list);
        mMyWordsRecView = (RecyclerView) findViewById(R.id.myWordsRecyclerView);

        //Others
        setDivider(); //dividers to builder RV
        pointsAnimation = (TextView) findViewById(R.id.points_textview);

        //Shared preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        setupSharedPreferences();

        //game initialization
        newGame();
        boolean saw_bubbles=Hawk.get(SAW_BUBBLE_TOUR,false);
        if(!saw_bubbles)
            showBubbles();

        //HockeyApp
        checkForUpdates();

    }

    @Override
    public void onResume() {
        Log.i("lifecycleEvent","onResume");
        super.onResume();
        long prevTimer= getIntent().getLongExtra("timer",0); //in case a game with timer was stopped and resumed
        if(prevTimer>0 && countDownInd!=0) {
            initalizeTimers=true;
            createTimer(prevTimer);
        }
        getIntent().putExtra("timer",0);
        if(mSensorManager!=null)
            mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
        //HockeyApp
        checkForCrashes();
    }

    @Override
    public void onPause() {
        Log.i("lifecycleEvent","onPause");
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
        //Hockey App
        unregisterManagers();
    }

    @Override
    public void onStop() {
        Log.i("lifecycleEvent","onStop");
        super.onStop();
        if(countDownInd!=0) {        //in case a game with timer was stopped
            countDownTimer.cancel();
            getIntent().putExtra("timer", toEndTimer);
        }
        //Hockey App
        unregisterManagers();
    }

    @Override
    public void onDestroy() {
        Log.i("lifecycleEvent","onDestroy");
        super.onDestroy();
        //Hockey App
        unregisterManagers();
        /** Cleanup the shared preference listener **/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }


    // CORE GAME METHODS
    public void newGame() {
        //create bag
        game_limit = getResources().getInteger(R.integer.tiles_in_game);
        lettersLeft = game_limit;

        setEnableAll(true);

        if(countDownInd==0) {
                gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.tiles_on_board_no_timer));
                enableCountDown(false);
        }
       else {
            gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.tiles_on_board));

            if(countDownInd==1){
                createTimer(getResources().getInteger(R.integer.timer_initial_moderate));
                countDownView.setText(getString(R.string.moderate_seconds));
                }
            else    { //countDownInd==2
                createTimer(getResources().getInteger(R.integer.timer_initial_speedy));
                countDownView.setText(getString(R.string.speedy_seconds));
            }
            //countdownind 1 and 2:
            enableCountDown(true);
            setTimerSize();
        }

        //Create managers
        BuilderLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        BuilderLayoutManager.setAutoMeasureEnabled(true);
        myWordsStaggeredManager = new StaggeredGridLayoutManager(setSpanForStaggered(), StaggeredGridLayoutManager.HORIZONTAL);
        myWordsStaggeredManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        //set managers
        mMyWordsRecView.setLayoutManager(myWordsStaggeredManager);
        mBoardRecView.setLayoutManager(gridLayoutManager);
        mBuilderRecView.setLayoutManager(BuilderLayoutManager);
        //changes per device metrics
        mBoardRecView.getLayoutParams().height = setHeightForRV();
        //element setups
        builderLetterTypes.clear();
        bag.clear();
        if (board.size() > 0) {
            board.clear();
            mBoardAdapter.notifyDataSetChanged();
        }
        if (builder.size() > 0) {
            builder.clear();
            mBuilderAdapter.notifyDataSetChanged();
        }
        if (myWords.size() > 0) {
            myWords.clear();
            mWordsAdapter.notifyDataSetChanged();
        }
        LetterBag.createScrabbleSet(bag);
        for (int i = 0; i < 4; i++) {
            addLetterToBoard(NEW_GAME);
        }
        getLetter.setText(getResources().getString(R.string.get_letter));
        mTiles.setText(String.valueOf(lettersLeft));
        playerScore = 0;
        mScore.setText(String.valueOf(playerScore));

        //Create and set adapters
        mBoardAdapter = new BoardAdapter(this, board, this, R.id.scrabble_letter_list);
        mBoardRecView.setAdapter(mBoardAdapter);

        mBuilderAdapter = new BoardAdapter(this, builder, this, R.id.word_builder_list);
        mBuilderRecView.setAdapter(mBuilderAdapter);

        mWordsAdapter = new myWordsAdapter(this, myWords, this);
        mMyWordsRecView.setAdapter(mWordsAdapter);

    }

    public void addLetterToBoard(String src){//(boolean newGame, boolean midTimer) {
        if (lettersLeft == 0) {
            return;
        }
        RandomSelector randomSelector = new RandomSelector(bag);
        SingleLetter selectedLetter;
        selectedLetter = randomSelector.getRandom();
        //reduce from bag
        for (int j = 0; j < bag.size(); j++) { //find letter to reduce probability from bag
            if (bag.get(j).letter_name.equals(selectedLetter.letter_name)) {
                bag.get(j).reduce_letter_probability();
            }
        }
        board.add(selectedLetter);
        lettersLeft--;
        mTiles.setText(String.valueOf(lettersLeft));


        if (!src.equals(NEW_GAME) && countDownInd!=0) {
            if(initalizeTimers) { //if game was stopped and resumed
                switch (countDownInd) {
                    case 1:
                        createTimer(getResources().getInteger(R.integer.timer_initial_moderate));
                        //createTimer(toEndTimer);
                        break;
                    case 2:
                        createTimer(getResources().getInteger(R.integer.timer_initial_speedy));

                }
                initalizeTimers=false;
            }
            else if(src.equals(MODERATE_GET_LETTER)){
                createTimer(toEndTimer);
                initalizeTimers=true;
            }
            else
                countDownTimer.start(); //start timer for new tile
        }
        if (lettersLeft == 0) {
            noLettersInBag();
        }
        return;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //TODO REMOVE AFTER TESTING and clickable in xml
            case R.id.my_words_title:
                showBubbles();
                break;

            case R.id.get_letter: //get letter or end game or new game
                if (getLetter.getText().equals(getResources().getString(R.string.new_game))) {
                    newGame();
                    break;
                }
                else if (lettersLeft == 0) { //means he tapped end game
                    getLetter.setEnabled(false);
                    int reduceScore = reduceScoreEndGame(); //calculate value of letters left on board
                    dialogEndGameSure(reduceScore); //could be 0

                    break;
                }

                onGetLetter();

                break;

            case R.id.send_word:
                Log.i("send word", "send word");

                //test validity of word
                //dictionary and complies to rules

                String spellCheckWord = "";

                int addScore = 0;
                for (int i = 0; i < builder.size(); i++) {

                    spellCheckWord = spellCheckWord + builder.get(i).getLetter_name(); //cast tiles to string
                    addScore = addScore + builder.get(i).getLetter_value();

                }

                //TEST 1 - no word
                if (spellCheckWord.equals("")) {
                    Toast.makeText(this, "No word", Toast.LENGTH_SHORT).show();
                    break;
                }

                //TEST 2 -  left a word broken in myWords
                int wordBroken = 0;
                for (int i = 0; i < myWords.size(); i++) { //loop per word
                    int blankCounter = 0;
                    for (int j = 0; j < myWords.get(i).size(); j++) { //loop per letter in word
                        if (myWords.get(i).get(j).getLetter_name().equals("")) {
                            blankCounter++;
                        }
                    }
                    if (blankCounter != myWords.get(i).size() && blankCounter > 0) {
                        wordBroken = 1;
                        break;
                    }
                }
                if (wordBroken == 1) {//word rules don't comply
                    Toast.makeText(this, "Can't use part of a word", Toast.LENGTH_SHORT).show();
                    break;
                }

                //TEST 3 - only  pluralised
                int i = 0;
                int lastLetterIndex = builderLetterTypes.size() - 1;
                int wordToCheckIndex =  builderLetterTypes.get(0)[1]; //first letter word index

                if (builderLetterTypes.get(lastLetterIndex)[0] == 0 && builderLetterTypes.get(0)[0]==1)//last letter from board (0) and first letter from words
                {
                   String wordToCheck=  wordList.get(wordToCheckIndex).getTheWord();
                    if((wordToCheck+"S").equals(spellCheckWord)){
                  /*  if (builder.get(lastLetterIndex).getLetter_name().equals("S")) {
                        for (i = 0; i < builderLetterTypes.size() - 1; i++) {
                            if (builderLetterTypes.get(i)[0] == 0)
                                break; //another letter is from board
                            if (builderLetterTypes.get(i)[2] != i)
                                break; //letter index in a different order
                        }
                        if (i == builderLetterTypes.size() - 1)//go to end without breaking*/
                        {
                            Toast.makeText(this, "You can't only pluralise a word", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }

                //TEST 4 - must add letters to existing word
                int fromWordsCounter = 0;
                for (int[] place : builderLetterTypes) {
                    if (place[0] == 0) {
                        break;
                    }
                    fromWordsCounter++;
                }

                if (fromWordsCounter == builderLetterTypes.size()) {
                    Toast.makeText(this, "Must add letters from board as well", Toast.LENGTH_SHORT).show();
                    break;
                }

                //TEST 5 - word must be 4+ letters
                if (builder.size() < 3) {
                    Toast.makeText(this, "word must be at least 3 letters long", Toast.LENGTH_SHORT).show();
                    break;
                }

                //if TEST 1- 5 ARE ALL GOOD:
                setEnableAll(false);

                //WORD VALIDATOR
                URL wordSearchURL = NetworkUtils.buildUrlCheckWord(spellCheckWord);
                new WordValidator(spellCheckWord, addScore).execute(wordSearchURL);
                break;

            case R.id.clear_word:
                clearBuilder();
                break;

        }
    }


    //tiles left algortihm if not game limit
  /*  public int tilesLeft(List<SingleLetter> list) {
        int totalSum = 0;
        for (SingleLetter item : list) {
            totalSum = totalSum + item.letter_probability;
        }
        return totalSum;
    }*/


    @Override
    public void onWordItemClick(int clickedWord, int clickedLetter) {
        Log.i("Main Activity", "onWordItemClick -letter" + clickedLetter);
        Log.i("Main Activity", "onWordItemClick -word" + clickedWord);
        Log.i("getSpanCount",String.valueOf(myWordsStaggeredManager.getSpanCount()));
        builder.add(myWords.get(clickedWord).get(clickedLetter));
        mBuilderAdapter.notifyDataSetChanged();
        builderLetterTypes.add(placer(1, clickedWord, clickedLetter)); //keeps track of new word in builder origin
        myWords.get(clickedWord).remove(clickedLetter);
        myWords.get(clickedWord).add(clickedLetter, new SingleLetter("", 0, 0)); //notify occurs in myWordsAdapter
    }

    @Override
    public void onLetterClick(int recyler_id, int clickedItemIndex) {
        Log.i("Main Activity", "onLetterClick " + clickedItemIndex);
        if (!mBoardRecView.isEnabled() || !mMyWordsRecView.isEnabled() || !mBuilderRecView.isEnabled())
        {
            return;
        }

        switch (recyler_id) { //from builder to board
            case R.id.scrabble_letter_list: //board
                builder.add(board.get(clickedItemIndex));
                board.remove(clickedItemIndex);
                board.add(clickedItemIndex, new SingleLetter("", 0, 0));
                builderLetterTypes.add(placer(0, -1, clickedItemIndex));
                mBoardAdapter.notifyDataSetChanged();
                mBuilderAdapter.notifyDataSetChanged();
                break;

            case R.id.word_builder_list: //from builder to board or words
                int origin = builderLetterTypes.get(clickedItemIndex)[0];
                int wordIndex = builderLetterTypes.get(clickedItemIndex)[1];
                int letterIndex = builderLetterTypes.get(clickedItemIndex)[2];
                switch (origin) {
                    case 0: //from board
                        board.remove(letterIndex);//remove place holder
                        board.add(letterIndex, builder.get(clickedItemIndex));
                        mBoardAdapter.notifyDataSetChanged();
                        break;
                    case 1: //from myWords(wordplace.letterplace)
                        if (wordIndex < 0) {
                            Log.i("case word index=-1", "shouldn't get here");
                            break;
                        } //per caution. shouldn't get here
                        myWords.get(wordIndex).remove(letterIndex);
                        myWords.get(wordIndex).add(letterIndex, builder.get(clickedItemIndex));
                        mWordsAdapter.notifyItemChanged(wordIndex); //
                        break;
                }  //in any case:

                builder.remove(clickedItemIndex);
                builderLetterTypes.remove(clickedItemIndex);
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
            pBar.setVisibility(View.VISIBLE); //progress bar
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

    public void afterDialogSuccess(int tempScore) {
        addWordToMyWords(tempScore);
        addLetterToBoard(AFTER_PLAYED_WORD); //when word played a new letter is added to board without penalty
        mBoardAdapter.notifyDataSetChanged();
        if (lettersLeft == 0) {
            noLettersInBag();
        }
        if (board.isEmpty() && builder.isEmpty()) {
            setFinalPoints(0);
        }
        setEnableAll(true);
       return;
    }


    public void clearBuilder() {
        int builder_size = builder.size();
        for (int i = builder_size - 1; i >= 0; i--) {
            int origin = builderLetterTypes.get(i)[0];
            int wordIndex = builderLetterTypes.get(i)[1];
            int letterIndex = builderLetterTypes.get(i)[2];
            switch (origin) {
                case 0: //board
                    board.remove(letterIndex);//remove place holder
                    board.add(letterIndex, builder.get(i));
                    break;
                case 1: //myWords(wordplace.letterplace)
                    if (wordIndex < 0) {
                        Log.i("case word index=-1", "shouldn't get here");
                        break;
                    } //per caution. shouldn't get here
                    myWords.get(wordIndex).remove(letterIndex); //remove space holder
                    myWords.get(wordIndex).add(letterIndex, builder.get(i));
                    break;
            }

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
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        setEnableAll(true);
                    }
                })
                .setNeutralButton("My bad", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setEnableAll(true);
                    }
                }).create().show();
    }

    public void dialogEndGameSure(final int boardPoints) {
        getLetter.setEnabled(true);
        new AlertDialog.Builder(this).setTitle("End Game")
                .setMessage("Are you sure you want to end game? You will lose " + boardPoints + " for tiles left in the board")
                .setPositiveButton("I am sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setFinalPoints(boardPoints);
                    }
                }).setNegativeButton("I'll keep trying", null).create().show();
    }



    public void dialogEndGame(final int res) {

if(highScoreScreenSlideDialog!=null) {
    if (highScoreScreenSlideDialog.isVisible()) {
        //   Log.i("end game dialog", "high score visible");
        highScoreScreenSlideDialog.dismiss();  //close high score dialog if end game dialog
    }
}
        String msg = "";

        if (res == 1) {
            msg = "You made a new high score!";
        }
        if (res == 0) {
            msg = "Well done!";
        }

        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.end_game))
                .setMessage("Your Score: " + playerScore + "\n" + msg)
                .setNegativeButton(R.string.new_game, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        newGame();
                    }
                }).setNeutralButton("BACK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getLetterButtonToNewGame();
            }
        })
             /*   .setNegativeButton(R.string.share_move, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                shareScore(playerScore);
                getLetterButtonToNewGame();
            }
        })*/
                .setPositiveButton("HIGH SCORES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getLetterButtonToNewGame();
               openHighScoreDialog();
            }
        })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                      dialogEndGame(res);
                    }
                }).create().show();
    }

    public void getLetterButtonToNewGame(){
        setEnableAll(false);
        getLetter.setText(R.string.new_game);
        getLetter.setEnabled(true);
    }


    public void dialogCorrectWord(String word, final int score) {

        String extraScore = "";
        String longScore = "";
        for (int[] letter : builderLetterTypes) {
            if (letter[0] == 1) { //at least one letter from reused word
                extraScore = "\n" + "You get to keep the score for the original word as well!";
                break;
            }
        }
        int wordLength = word.length();
        if (wordLength >= 7) {
            longScore = "\n" + "Woohoo! " + wordLength + " extra points for a " + wordLength + " letter word!";
        }
        else
            wordLength=0;

        new AlertDialog.Builder(this).setTitle("Hurray! +" + (score+wordLength) +" points!")
                .setMessage(word + " is great! Keep it up!" + extraScore + longScore)
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
                }).create().show();

    }

    public void addWordToMyWords(int tempScore) {

        List<SingleLetter> newWord = new ArrayList<>();
        String theWord="";
        //move letters to myWords
        int builder_size = builder.size();
        List<Integer> tempBoardLetters = new ArrayList<>();
        List<Integer> tempMyWordsLetters = new ArrayList<>();


        if (builder_size >= 7) { //BONUS for long words!
            tempScore = tempScore + builder_size;
        }

        startPointAnimation(tempScore,"+");
        for (int i = 0; i < builder_size; i++) { //add to word array
            newWord.add(i, builder.get(i));
            theWord= theWord + builder.get(i).getLetter_name();
            if (builderLetterTypes.get(i)[0] == 0) {
                tempBoardLetters.add(builderLetterTypes.get(i)[2]);

            }
            if (builderLetterTypes.get(i)[0] == 1) {
                tempMyWordsLetters.add(builderLetterTypes.get(i)[1]); //save word indexes
            }
        }
     //SORT templettertypes and remove from board accordingly - start removing from end
        Collections.sort(tempBoardLetters);
        for (int i = tempBoardLetters.size() - 1; i >= 0; i--) {
            int toRemove = tempBoardLetters.get(i);
            board.remove(toRemove);//remove place holders
        }

        //remove blank letters from mywords - start removing from end
        Collections.sort(tempMyWordsLetters);
        Set uniqueValues = new HashSet(tempMyWordsLetters); //now unique
        List<Integer> tempMyWordsLettersUnique = new ArrayList<>(uniqueValues);
        for (int i = uniqueValues.size() - 1; i >= 0; i--) {
            //none broken letter check was done earlier
            int toRemove = tempMyWordsLettersUnique.get(i);
            myWords.remove(toRemove);
            wordList.remove(toRemove); //TODO Test this
            mWordsAdapter.notifyDataSetChanged();
        }

        builder.clear();
        builderLetterTypes.clear();
        mBuilderAdapter.notifyDataSetChanged();
        mBoardAdapter.notifyDataSetChanged();
        myWords.add(newWord); //always added to end
        mWordsAdapter.notifyDataSetChanged();
        myWordsStaggeredManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

       // int getIndex= myWords.indexOf(newWord);
        Word addWord = new Word( theWord);
        wordList.add(addWord);
        playerScore = playerScore + tempScore;
        mScore.setText(String.valueOf(playerScore));
    }


    public String parseWordResult(String xmlString) throws IOException {

        InputStream stream = new ByteArrayInputStream(xmlString.getBytes());
        XmlPullParserFactory xmlFactoryObject = null;
        XmlPullParser myParser = null;
        String valid = "";
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        try {
            myParser = xmlFactoryObject.newPullParser();
            myParser.setInput(stream, null);
            int event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {

                    case XmlPullParser.START_TAG:
                        if (name.equals("scrabble")) {
                            if (myParser.next() == XmlPullParser.TEXT) {
                                valid = myParser.getText();
                            }
                        }
                        break;
                }
                event = myParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return valid;
    }

    //CORE ASSISTING METHODS
    public void noLettersInBag() {
        if (getLetter.getText() == getResources().getString(R.string.end_game)) {
            //not the first time counter became 0
            //RESUME counter
            Log.d("timer", "creating_new_timer");
            createTimer(toEndTimer);
            return;
        }
        getLetter.setText(getResources().getString(R.string.end_game));
        Toast.makeText(getApplicationContext(), "No tiles lift in bag", Toast.LENGTH_LONG).show();
        return;
    }
    public void onGetLetter() {
        if (getLetter.getText().equals(getString(R.string.get_letter))) {

            if (countDownInd != 0) {
                if (board.size() == 2 * (getResources().getInteger(R.integer.tiles_on_board))) {
                    Toast.makeText(getApplicationContext(), "The board is full, scroll to see more letters", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (board.size() == 2 * (getResources().getInteger(R.integer.tiles_on_board_no_timer))) {
                    Toast.makeText(getApplicationContext(), "The board is full, scroll to see more letters", Toast.LENGTH_SHORT).show();
                }
            }

            if(countDownInd==1)//moderate mode
            {
                addLetterToBoard(MODERATE_GET_LETTER);
                initalizeTimers=false;
            }
            else
                addLetterToBoard(CLASSIC_GET_LETTER);

            startPointAnimation(getResources().getInteger(R.integer.get_letter_points_loss), "-");
            mBoardAdapter.notifyDataSetChanged();
            playerScore--; //reduce a point for each tile the user adds
            mScore.setText(String.valueOf(playerScore));

        }
    }

    public void setFinalPoints(final int boardPoints){
        int tempScore = playerScore;
        if(boardPoints>0)
            startPointAnimation(boardPoints,"-");
        playerScore = tempScore - boardPoints;
        if (countDownInd!=0)
            countDownTimer.cancel();
        mScore.setText(String.valueOf(playerScore));
        final int res = PreferenceUtilities.newScoreSend(this, playerScore, countDownInd);
        Handler myHandler= new Handler();
        getLetter.setEnabled(false);
        setEnableAll(false);
        myHandler.postDelayed(new Runnable(){
            @Override
            public void run()
            {
                dialogEndGame(res);
            }
        }, 1250);

    }
    public int reduceScoreEndGame() {
        int reduceScore = 0;

        for (SingleLetter letter : board) {
            reduceScore = reduceScore + letter.getLetter_value();
        }
        for(int i=0; i<builder.size();i++){
            if(builderLetterTypes.get(i)[0]==0){//from board
                reduceScore = reduceScore + builder.get(i).getLetter_value();
            }
        }

        return reduceScore;
    }

    public int[] placer(int letterOrigin, int wordPlace, int letterPlace) {
        int[] series = {letterOrigin, wordPlace, letterPlace};//0: 0= from board 1 = from mywords, 1: word index, 2: letter index
        return series;
    }


    private void setEnableAll(boolean state) {
        if (countDownInd==2) {
            getLetter.setEnabled(false); //never possible in speedy mode
        } else
            getLetter.setEnabled(state);

        playWord.setEnabled(state);
        clearWord.setEnabled(state);
        mBoardRecView.setEnabled(state);
        mBuilderRecView.setEnabled(state);
        mMyWordsRecView.setEnabled(state);

        if(mMyWordsRecView!=null) {
            int size=mMyWordsRecView.getChildCount();
            for (int i = 0; i < size; i++) {

                myWordsAdapter.WordViewHolder wordView = (myWordsAdapter.WordViewHolder)mMyWordsRecView.findViewHolderForLayoutPosition(i);
                if(wordView!=null)
                    wordView.mySetEnabled(state);

            }
        }
    }



    //MENU METHODS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context context = MainActivity.this;
        Class destinationActivity;
        Intent intent;
        switch (item.getItemId()) {
            case R.id.new_game_button:
                newGame();
                return true;
            case R.id.home_screen:
                destinationActivity = HomeActivity.class;
                intent = new Intent(context, destinationActivity);
                startActivity(intent);
                return true;
            case  R.id.instructions_menu:
                destinationActivity = Instructions.class;
                intent = new Intent(context, destinationActivity);
                startActivityForResult(intent,RESULT_CODE_INSTRUCTIONS);
                return true;
            case R.id.high_score_in_menu:
            openHighScoreDialog();
                return true;
            case R.id.send_feedback:
                destinationActivity = SendFeedback.class;
                intent = new Intent(context, destinationActivity);
                startActivity(intent);
                return true;
            case R.id.settings:
                destinationActivity = SettingsActivity.class;
                intent= new Intent(context, destinationActivity);
                startActivity(intent);
                return true;
        } //switch
        return super.onOptionsItemSelected(item); //if not action_search
    }


    //HOCKEY APP
    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }

    //GET RESULT
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE_HIGH_SCORE_FRAGMENT && resultCode == RESULT_CODE_HIGH_SCORE_FRAGMENT) {
            if (data.getIntExtra(BUTTON_TAPPED, 0) == (R.string.new_game))
                newGame();
        }
        if (requestCode == RESULT_CODE_INSTRUCTIONS && resultCode == RESULT_CODE_INSTRUCTIONS) {
            if(data.getIntExtra(BUTTON_TAPPED, 0)==(R.string.show_bbl_instructions)) {
                showBubbles();

            }
        }
    }
    public void openHighScoreDialog(){

        highScoreScreenSlideDialog = HighScoreScreenSlideDialog.createInstance(countDownInd);
        highScoreScreenSlideDialog.show(getSupportFragmentManager(),"Dialog Fragment");
    }

    //SHARED PREFERENCES METHODS
    private void setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up
        SharedPreferences sharedPreferences = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);
        shakeToGetLetter = sharedPreferences.getBoolean(getString(R.string.shake_key), getResources().getBoolean(R.bool.preferences_default_true));
        animatePoints= sharedPreferences.getBoolean(getString(R.string.animate_key), getResources().getBoolean(R.bool.preferences_default_true));
        if (countDownInd==0) {
            showCorrectPopUp = sharedPreferences.getBoolean(getString(R.string.pop_up_correct_key), getResources().getBoolean(R.bool.preferences_default_true));
            showWrongPopUp = sharedPreferences.getBoolean(getString(R.string.pop_up_wrong_key), getResources().getBoolean(R.bool.preferences_default_true));
        } else {
            showCorrectPopUp = false;
            showWrongPopUp = false;
        }
        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.pop_up_correct_key))) {
            if (countDownInd==0)
                showCorrectPopUp = (sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.preferences_default_true)));
            else showCorrectPopUp = false;
        } else if (key.equals(getString(R.string.pop_up_wrong_key))) {
            if (countDownInd==0)
                showWrongPopUp = (sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.preferences_default_true)));
            else
                showWrongPopUp = false;
        } else if (key.equals(getString(R.string.shake_key))) {
            shakeToGetLetter = (sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.preferences_default_true)));
        } else if (key.equals(getString(R.string.animate_key))) {
            animatePoints =(sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.preferences_default_true)));
        }
    }


    //TIMER METHODS
    public void enableCountDown(boolean enable) {

        if (enable) {
            countDownView.setVisibility(View.VISIBLE);
            if(countDownInd==2)
            {getLetter.setEnabled(false);}
            //if(Hawk.get()) -- better off having func in bubble method
            countDownTimer.start();

        } else {
            getLetter.setEnabled(true);
            countDownView.setVisibility(GONE);
            if (countDownTimer != null)
                countDownTimer.cancel();
        }
    }

    public void createTimer(final long milliseconds)
    {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        countDownTimer = new CountDownTimer(milliseconds, 1000) {

            public void onTick(long millisUntilFinished) {
                toEndTimer = millisUntilFinished;
                Log.i("timer", String.valueOf(millisUntilFinished));
                if (countDownInd!=0)
                    countDownView.setText(String.format("%02d", millisUntilFinished / 1000));
            }

            public void onFinish() {
                if (countDownInd!=0) {
                    if (lettersLeft > 0) {
                        addLetterToBoard(TIME_UP);
                        startPointAnimation(getResources().getInteger(R.integer.get_letter_points_loss),"-");
                        playerScore = playerScore - 1;
                        mScore.setText(String.valueOf(playerScore));
                    } else if (lettersLeft == 0) {
                        countDownTimer.cancel();
                        setFinalPoints(reduceScoreEndGame());
                    }
                }
            }
        };
        if(!showingBubblesFlag)
            countDownTimer.start();
    }



    //FEATURES
    //////SHARE
    public void shareScore(int score){
        String mimeType ="text/plain";
        String title = getString(R.string.share_score_title);
        String textToShare = getString(R.string.share_score_1) + score + getString(R.string.share_score_2);
        //// ADD LINK TO PLAY STORE when available
        ShareCompat.IntentBuilder
                /* The from method specifies the Context from which this share is coming from */
                .from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(textToShare)
                .startChooser();
    }

   //////ANIMATION
    public void startPointAnimation(int score, String plusOrMinus){
        if(animatePoints) {
            pointsAnimation.setText(plusOrMinus + String.valueOf(score));
            final Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation);
            animation.reset();
            pointsAnimation.setY(mBuilderRecView.getY());
            pointsAnimation.startAnimation(animation);
            pointsAnimation.setVisibility(View.INVISIBLE);
        }
    }

    //////SHAKER
    public void shakeDetectorInit(){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
				/*
				 * The following method, "handleShakeEvent(count):" is a stub //
				 * method you would use to setup whatever you want done once the
				 * device has been shook.
				 */
                handleShakeEvent();
            }
        });
    }

    public void handleShakeEvent(){
        if(shakeToGetLetter) {
            onGetLetter();
            Log.i("fruit", "shake");
        }
    }



    //DEVICE METRICS and UI METHODS

    public void setDivider(){
        DividerItemDecoration divider;
        divider= new DividerItemDecoration(mBuilderRecView.getContext(),DividerItemDecoration.HORIZONTAL);
        if(deviceWidth<800)
            divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.line_divider_s));
        else if(deviceWidth<1000)
            divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.line_divider_m));
        else
            divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.line_divider_l));
        mBuilderRecView.addItemDecoration(divider);
    }
    public static DisplayMetrics getDeviceMetrics(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    public static int dpToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public void setDeviceDimensions(){
        Hawk.init(this).build();
        if(!Hawk.contains(DEVICE_WIDTH) || !Hawk.contains(DEVICE_HEIGHT)) { //first time for device
            //save device metrics:
            DisplayMetrics metrics = getDeviceMetrics(this);
            Hawk.put(DEVICE_HEIGHT, metrics.heightPixels);
            Hawk.put(DEVICE_WIDTH,  metrics.widthPixels);
        }
        else
        {
            deviceHeight = Hawk.get(DEVICE_HEIGHT);
            deviceWidth  = Hawk.get(DEVICE_WIDTH);
        }
    }

    private int setSpanForStaggered() {
        //if(deviceHeight<1500)
        //return  3;
        if (deviceHeight<1750)
            return 4;
        else return 5;
    }

    private int setHeightForRV() {
        if(deviceHeight<1500)
            return dpToPx(this,60);
        else if (deviceHeight<1750)
            return  dpToPx(this,80);
        else return  dpToPx(this,100);
    }

    private void setTimerSize(){
        if(deviceHeight<1500)
            countDownView.setTextSize(26);
        else if (deviceHeight<1750)
            countDownView.setTextSize(28);
        else return;
    }


    //OTHERS
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        finish();

    }

    //BUBBLE INSTRUCTIONS
    //save in Hawk - did the user already have instructions - for 3 modes. have ifs for all 7 scenarios

    //Create bubbles with design:

    public void showBubbles(){

        showingBubblesFlag = true;
        Log.i("method","show bubbles");
        if(countDownInd!=0){
            countDownTimer.cancel();
        }
        setEnableAll(false);

//TODO add timer instructions for speedy and mod. make ifs if already saw each mode or other mode. start timer only after last dismiss.
        final String bubble1= getString(R.string.bubble_on_board);
        final String bubble2= getString(R.string.bubble_on_myWords);
        final String bubble3= getString(R.string.classic_moderate_bubble_on_GetLetter);
        final String bubble3_1= getString(R.string.speedy_bubble_to_timer);
        final String bubble3_2= getString(R.string.moderate_bubble_to_timer);
        final String bubble4= getString(R.string.bubble_to_score);
        final String bubble5= getString(R.string.bubble_final);

        // bubble1 on board
        bubbleLayout = (BubbleLayout)findViewById(R.id.bubble_layout);
        bubbleLayout.setX(deviceWidth/2);
        bubbleLayout.setY(mBoardRecView.getY()-15);
        bubbleLayout.setArrowDirection(ArrowDirection.LEFT);
        bubbleText = (TextView) findViewById(R.id.instruction_bubble);
        bubbleText.setText(bubble1);
        bubbleText.setTextSize(18);
        bubbleLayout.setVisibility(View.VISIBLE);
        bubbleX = (TextView)findViewById(R.id.bubble_quit);
        bubbleX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentText = String.valueOf(bubbleText.getText());
               if(currentText.equals(bubble1)){
                    Log.i("onclick", "bubble1");
                   bubbleLayout.setArrowDirection(ArrowDirection.TOP);
                    bubbleLayout.setX(mMyWordsRecView.getX()+20);
                    bubbleLayout.setY(mMyWordsRecView.getY()+100);
                    bubbleText.setText(bubble2);
                }

               else if(currentText.equals(bubble2)){
                   if(countDownInd<2) { //moderate or classic
                       Log.i("onclick", "bubble3");
                       bubbleLayout.setX(deviceWidth/2);
                       bubbleLayout.setY(mBoardRecView.getY()+10);
                      // bubbleLayout.setForegroundGravity(Gravity.BOTTOM);
                       bubbleText.setText(bubble3);
                       bubbleLayout.setArrowDirection(ArrowDirection.BOTTOM);
                   }
                   else if(countDownInd==2){
                       Log.i("onclick", "bubble3");
                       bubbleText.setText(bubble3_1);
                       bubbleLayout.setArrowDirection(ArrowDirection.RIGHT);
                       bubbleLayout.setX(countDownView.getX()-countDownView.getWidth()-bubbleLayout.getWidth());
                       bubbleLayout.setY(mBoardRecView.getY()+10);

                   }

               }
               else if(countDownInd==1 && currentText.equals(bubble3)){
                    Log.i("onclick", "bubble3");
                    bubbleText.setText(bubble3_2);
                    bubbleLayout.setArrowDirection(ArrowDirection.RIGHT);
                   bubbleLayout.setX(countDownView.getX()-countDownView.getWidth()-bubbleLayout.getWidth());
                   bubbleLayout.setY(mBoardRecView.getY()+10);
                }


               else if((currentText.equals(bubble3) && countDownInd==0)|| currentText.equals(bubble3_1) || currentText.equals(bubble3_2)){
                   Log.i("onclick", "bubble3 or 3_1 or 3_2");
                   bubbleText.setText(bubble4);
                   bubbleLayout.setX(mMyWordsRecView.getX()+20);
                   bubbleLayout.setY(scoreRelativeLayout.getY()-bubbleLayout.getHeight());
                   bubbleLayout.setArrowDirection(ArrowDirection.BOTTOM);

               } else if(currentText.equals(bubble4)){
                   Log.i("onclick", "bubble4");
                   bubbleLayout.setArrowDirection(ArrowDirection.LEFT);
                   //bubbleLayout.setX();
                   bubbleLayout.setY(deviceHeight/2);
                   bubbleText.setTextSize(50);
                   bubbleLayout.setForegroundGravity(Gravity.CENTER_HORIZONTAL);
                   bubbleText.setText(bubble5);
               }

               else if(currentText.equals(bubble5)){
                   Log.i("onclick", "bubble5");
                Hawk.put(SAW_BUBBLE_TOUR,true);
                bubbleLayout.setVisibility(GONE);
                   if(countDownInd!=0) {
                       countDownTimer.start();
                   }
                showingBubblesFlag=false;
                setEnableAll(true);

               }

            }
        });


    }


}



