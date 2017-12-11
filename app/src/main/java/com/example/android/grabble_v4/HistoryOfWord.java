package com.example.android.grabble_v4;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.grabble_v4.Utilities.Share;
import com.example.android.grabble_v4.data.Word;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class HistoryOfWord extends DialogFragment implements View.OnClickListener{


    public static int FRAGMENT_TILE_FIT=99;
    RecyclerView mWordsLevel;
    HistoryWordAdapter historyWordAdapter;
    TextView title;
    TextView subtitle;
    Word finalWord;
    List<List<Word>> wordLevel;
    List<Word> tempLevel;
    LinearLayoutManager linearLayoutManager;
    ImageView camera;
    ImageView shareButton;
    int totalPoints=0;
    View rootview;
    private static final String TAG = "HistoryOfWord";
    final static int REQUEST_CODE= 789;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.activity_history_of_word, container, true);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        title = (TextView)rootview.findViewById(R.id.fragment_title);
        subtitle= (TextView) rootview.findViewById(R.id.fragment_subtitle);
        mWordsLevel = (RecyclerView) rootview.findViewById(R.id.word_history_full_rv);
        wordLevel = new ArrayList<>();
        historyWordAdapter = new HistoryWordAdapter(getContext(),getActivity(), wordLevel);
        mWordsLevel.setAdapter(historyWordAdapter);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mWordsLevel.setLayoutManager(linearLayoutManager);

        if (Hawk.contains(MainActivity.WORD)) {
            finalWord = Hawk.get(MainActivity.WORD);
            findWord(finalWord);
            historyWordAdapter.notifyDataSetChanged();
            subtitle.setText(String.valueOf(totalPoints)+" points total");
        } else
            Log.i("Error", "no word sent");

        camera = (ImageView) rootview.findViewById(R.id.history_camera);
        camera.bringToFront();
        camera.setOnClickListener(this);
        shareButton = (ImageView) rootview.findViewById(R.id.history_share);
        shareButton.setOnClickListener(this);

        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        int screenHeightDP = Hawk.get(MainActivity.DEVICE_HEIGHT); //
        int screenWidthDP = Hawk.get(MainActivity.DEVICE_WIDTH); //
        int tileHeight = Hawk.get(MainActivity.TILE_HEIGHT);
        int tileWidth = Hawk.get(MainActivity.TILE_WIDTH);
        int screenHeightPX = MainActivity.dpToPx(getContext(), screenHeightDP); //
        int screenWidthPX = MainActivity.dpToPx(getContext(), screenWidthDP); //
        int levels = finalWord.getNodeLevel() + 1;
        int longestWord = finalWord.getTheWord().length();
        longestWord = Math.max(longestWord, 4);
        int fragmentHeight;
        int fragmentWidth;
        int widthByTile=tileWidth * (longestWord + getResources().getInteger(R.integer.fragment_width));
        int widthByScreen= (int) (screenWidthPX * 0.99);
        fragmentWidth = Math.min(widthByTile, widthByScreen);
        if (fragmentWidth==widthByScreen) {
            for (int i = 5; i <= longestWord; i++) {
                if(tileWidth * (i + getResources().getInteger(R.integer.fragment_width))>(int) (screenWidthPX * 0.99)) {
                    FRAGMENT_TILE_FIT = i;
                    break;
                }
            }
        }
        else FRAGMENT_TILE_FIT=99;


        Log.i("HistoryOfWord",String.valueOf(FRAGMENT_TILE_FIT));
        fragmentHeight = (int) Math.min(tileHeight * (levels * 1.2 + getResources().getInteger(R.integer.fragment_height)), screenHeightPX * 0.95);
        Window window = getDialog().getWindow();
        window.setLayout(fragmentWidth, fragmentHeight);
    }


    //RECURSION
    public void findWord(Word word) {
        if (word.getNodeLevel() > 0) { //has child
            List<Word> sortedList =new ArrayList<Word>();
            sortedList = word.getWordHistory();
            Collections.sort(sortedList, new Comparator<Word>() {

                @Override
                public int compare(Word t1, Word t2) {
                    return Double.compare(t1.getNodeLevel(), t2.getNodeLevel());
                }
            });
            int i=sortedList.size()-1;
            while(i>=0){
                findWord(sortedList.get(i));
                i--;
            }
            //for (Word child :sortedList)
               // (child); //send all his children
        }
        printWord(word);

        return;

    }

    public void printWord(Word word) {
        int nodeLevel = word.getNodeLevel();
        Log.i(String.valueOf(nodeLevel), word.getTheWord());
        int level_size = wordLevel.size();
        if (level_size > nodeLevel) { //level already exists
            wordLevel.get(nodeLevel).add(word);
        } else {
            tempLevel = new ArrayList<>();
            tempLevel.add(word);
            wordLevel.add(nodeLevel, tempLevel);
            //historyWordAdapter.notifyDataSetChanged();
        }
        totalPoints=word.getPoints()+totalPoints;
    }


    private Bitmap takeScreenshot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public File store(Bitmap bm, String fileName) {
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Tiles";//"/Pictures/Screenshots";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
            Log.i("ExternalStorage", "created folder");
        }
        File file = new File(dirPath, fileName);

        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
            Toast.makeText(getContext(), "Photo saved in Tiles folder", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(getContext(), new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
        return file;
    }


    private void shareImage(Bitmap bmp) {
        new Share(bmp, getContext(), getActivity());
    }

    @Override
    public void onClick(View view) {

        String fileName;
        fileName = finalWord.getTheWord() + "_History.png";
        switch (view.getId()) {

            case R.id.history_camera:
                if (checkPermission()) {
                    store(takeScreenshot(rootview), fileName);
                }
                break;
            case R.id.history_share:
                shareImage(takeScreenshot(rootview));
                break;

        }
    }


    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("permission", "Permission is granted");
                //File write logic here
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

                return false;
            }
        } else return true;

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                store(takeScreenshot(rootview), finalWord.getTheWord() + "_History");
            }else {
                Toast.makeText(getContext(), "Permission required to store history", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
