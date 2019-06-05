package com.example.android.grabble_v4.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by user on 10/12/2017.
 */

public class Share {

    Context mContext;
    String mText;
    Bitmap mBMP;
    Activity mActivity;
    final static String DIRECTORY = "images";
    final static String IMAGE = "temp.png";
    final static String PLAY_STORE = "Play TILES too at https://play.google.com/store/apps/details?id=com.sha.android.tiles";
    final static String SUBJECT= "TILES";
    public Share(Bitmap bmp, Context context, Activity activity){
        mBMP=bmp;
        mContext=context;
        mActivity=activity;
        shareBMP();
    }

    public Share(String text, Context context, Activity activity){
        mText=text;
        mContext=context;
        mActivity=activity;
        shareText();
    }

    public void shareBMP(){
        saveToCache();
        File imagePath = new File(mContext.getCacheDir(), DIRECTORY);
        File newFile = new File(imagePath, IMAGE);

        Uri contentUri = FileProvider.getUriForFile(mContext, "com.example.android.grabble_v4.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, mContext.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, SUBJECT);
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, PLAY_STORE);
            mActivity.startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }

    }


    private void saveToCache(){

        try {

            File cachePath = new File(mContext.getCacheDir(), DIRECTORY);
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/"+IMAGE); // overwrites this image every time
            mBMP.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void shareText(){
        String mimeType ="text/html";
        String title = SUBJECT;
        String textToShare = mText + PLAY_STORE;
        ShareCompat.IntentBuilder
                /* The from method specifies the Context from which this share is coming from */
                .from(mActivity)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(textToShare)
                .startChooser();}
}
