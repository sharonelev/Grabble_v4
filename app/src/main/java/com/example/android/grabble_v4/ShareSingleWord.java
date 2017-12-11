package com.example.android.grabble_v4;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.android.grabble_v4.data.SingleLetter;
import com.example.android.grabble_v4.data.Word;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ShareSingleWord extends DialogFragment{


    public static int FRAGMENT_TILE_FIT=99;
    RecyclerView mWordsLevel;
    HistoryWordAdapter wordAdapter;
    TextView title;
    Word finalWord;
    List<List<Word>> mList;
    LinearLayoutManager linearLayoutManager;
    View rootview;
    private static final String TAG = "SingleWordShare";



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.activity_single_word, container, true);
        mWordsLevel = (RecyclerView) rootview.findViewById(R.id.word_single_rv);
        mList = new ArrayList<>();
        title =(TextView)rootview.findViewById(R.id.fragment_title_single_word);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        wordAdapter= new HistoryWordAdapter(getContext(), getActivity(),mList);
        mWordsLevel.setAdapter(wordAdapter);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mWordsLevel.setLayoutManager(linearLayoutManager);

        if (Hawk.contains(MainActivity.WORD)) {
            finalWord = Hawk.get(MainActivity.WORD);
            List<Word> tempList= new ArrayList<>();
            tempList.add(finalWord);
            mList.add(tempList);
            wordAdapter.notifyDataSetChanged();
            title.setText("I just got "+finalWord.getPoints()+" points for the word:");
        //    title.setText(getString(R.string.share_word_1)+points+ getString(R.string.share_word_2));
            rootview.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @SuppressWarnings("deprecation")
                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                rootview.getViewTreeObserver()
                                        .removeOnGlobalLayoutListener(this);
                            } else {
                                rootview.getViewTreeObserver()
                                        .removeGlobalOnLayoutListener(this);
                            }

                            new Share(takeScreenshot(rootview),getContext(),getActivity());

/*                            Handler myHandler=new Handler();
                            myHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dismiss();


                                }
                            }, 1000);*/

                            dismiss();
                        }
                    });
        }

        return rootview;
    }


    @Override
    public void onResume() {
        super.onResume();
        int tileHeight = Hawk.get(MainActivity.TILE_HEIGHT);
        Window window = getDialog().getWindow();
        window.setLayout(ViewPager.LayoutParams.WRAP_CONTENT,(int)(tileHeight * (getResources().getInteger(R.integer.fragment_height)+0.5)));

    }

    private Bitmap takeScreenshot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }
}
