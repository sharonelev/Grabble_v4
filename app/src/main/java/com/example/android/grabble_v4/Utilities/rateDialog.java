package com.example.android.grabble_v4.Utilities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import angtrim.com.fivestarslibrary.NegativeReviewListener;
import angtrim.com.fivestarslibrary.ReviewListener;


/**
 * Created by user on 27/11/2017.
 */

public class rateDialog implements DialogInterface.OnClickListener

    {
        private static final String DEFAULT_TITLE = "Rate this app";
        private static final String DEFAULT_TEXT = "How much do you love our app?";
        private static final String DEFAULT_POSITIVE = "Ok";
        private static final String DEFAULT_NEGATIVE = "Not Now";
        private static final String DEFAULT_NEVER = "Never";
        private static final String SP_NUM_OF_ACCESS = "numOfAccess";
        private static final String SP_DISABLED = "disabled";
        private static final String TAG = rateDialog.class.getSimpleName();
        private final Context context;
        private boolean isForceMode = false;
        SharedPreferences sharedPrefs;
        private String supportEmail;
        private TextView contentTextView;
        private RatingBar ratingBar;
        private String title = null;
        private String rateText = null;
        private AlertDialog alertDialog;
        private View dialogView;
        private int upperBound = 4;
        private int starColor;

        //interfaces
        private NegativeReviewListener negativeReviewListener;
        private ReviewListener reviewListener;
        private NotNowReviewListener notNowReviewListener;
        private NeverReviewListener neverReviewListener;
        private CancelReviewListener cancelListener;
        private WhileDialogShows whileDialogShows;
        private NoStartReviewListener noStartReviewListener;

    public rateDialog(Context context, String supportEmail) {
        this.context = context;
        this.sharedPrefs = context.getSharedPreferences(context.getPackageName(), 0);
        this.supportEmail = supportEmail;
    }

    private void build() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        this.dialogView = inflater.inflate(angtrim.com.fivestarslibrary.R.layout.stars, (ViewGroup)null);
        String titleToAdd = this.title == null?"Rate this app":this.title;
        String textToAdd = this.rateText == null?"How much do you love our app?":this.rateText;
        this.contentTextView = (TextView)this.dialogView.findViewById(angtrim.com.fivestarslibrary.R.id.text_content);
        this.contentTextView.setText(textToAdd);
        this.ratingBar = (RatingBar)this.dialogView.findViewById(angtrim.com.fivestarslibrary.R.id.ratingBar);
        this.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Log.d("test", "Rating changed : " + v);
                if(rateDialog.this.isForceMode && v >= (float)rateDialog.this.upperBound) {
                    rateDialog.this.openMarket();
                    if(rateDialog.this.reviewListener != null) {
                        rateDialog.this.reviewListener.onReview((int)ratingBar.getRating());
                    }
                }

            }
        });
        if (starColor != -1){
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(1).setColorFilter(starColor, PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(2).setColorFilter(starColor, PorterDuff.Mode.SRC_ATOP);
                   }
        this.alertDialog = builder.setTitle(titleToAdd).setView(this.dialogView).setNegativeButton("Not Now", this).setPositiveButton("Ok", this).setNeutralButton("Never", this).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.i("on_cancel","star dialog");
                cancelListener.onCancelReview();
            }
        }).create();
    }

    private void disable() {
        SharedPreferences shared = this.context.getSharedPreferences(this.context.getPackageName(), 0);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean("disabled", true);
        editor.apply();
    }

    private void openMarket() {
        String appPackageName = this.context.getPackageName();

        try {
            this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException var3) {
            this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }

    }

    private void sendEmail() {
        Intent emailIntent = new Intent("android.intent.action.SEND");
        emailIntent.setType("plain/text");
        emailIntent.putExtra("android.intent.extra.EMAIL", this.supportEmail);
        emailIntent.putExtra("android.intent.extra.SUBJECT", "App Report (" + this.context.getPackageName() + ")");
        emailIntent.putExtra("android.intent.extra.TEXT", "");
        this.context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    private void show() {
        boolean disabled = this.sharedPrefs.getBoolean("disabled", false);
       if(!disabled)
        {
            this.whileDialogShows.onShowDialog();
            this.build();
            this.alertDialog.show();
        }

    }

    public void showAfter(int numberOfAccess) {
        this.build();
        SharedPreferences.Editor editor = this.sharedPrefs.edit();
        int numOfAccess = this.sharedPrefs.getInt("numOfAccess", 0);
        editor.putInt("numOfAccess", numOfAccess + 1);
        editor.apply();
        if(numOfAccess + 1 >= numberOfAccess) {
            this.show();
        }
    }


    public void onClick(DialogInterface dialogInterface, int i) {
        if(i == -1) { //OK
            if(this.ratingBar.getRating()==0){
                this.noStartReviewListener.onOkNoStars();
                return;
            }
            else if(this.ratingBar.getRating() < (float)this.upperBound) {
                if(this.negativeReviewListener == null) {
                    this.sendEmail();
                } else {
                    this.negativeReviewListener.onNegativeReview((int)this.ratingBar.getRating());
                }
            } else if(!this.isForceMode) {
                this.openMarket();
            }

            this.disable();
            if(this.reviewListener != null) {
                this.reviewListener.onReview((int)this.ratingBar.getRating());
            }
        }

        if(i == -3) { //NEVER
            this.disable();
            if(this.neverReviewListener != null) {
                this.neverReviewListener.onNeverReview();
            }
        }

        if(i == -2) { //NOT NOW
            SharedPreferences.Editor editor = this.sharedPrefs.edit();
            editor.putInt("numOfAccess", 0);
            editor.apply();
            if(this.notNowReviewListener != null) {
                this.notNowReviewListener.onNotNowReview();
            }
        }

        this.alertDialog.hide();
    }

    public rateDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public rateDialog setSupportEmail(String supportEmail) {
        this.supportEmail = supportEmail;
        return this;
    }

    public rateDialog setRateText(String rateText) {
        this.rateText = rateText;
        return this;
    }

    public rateDialog setForceMode(boolean isForceMode) {
        this.isForceMode = isForceMode;
        return this;
    }

    public rateDialog setUpperBound(int bound) {
        this.upperBound = bound;
        return this;
    }

    public rateDialog setNegativeReviewListener(NegativeReviewListener listener) {
        this.negativeReviewListener = listener;
        return this;
    }

    public rateDialog setReviewListener(ReviewListener listener) {
        this.reviewListener = listener;
        return this;
    }

    public rateDialog setNotNowListener(NotNowReviewListener listener) {
            this.notNowReviewListener = listener;
            return this;
        }

    public rateDialog setNeverReviewListener(NeverReviewListener listener) {
        this.neverReviewListener = listener;
        return this;
    }

    public rateDialog setCancelReviewListener(CancelReviewListener listener) {
        this.cancelListener = listener;
        return this;
    }

    public rateDialog setWhileDialogListener(WhileDialogShows listener){
        this.whileDialogShows =listener;
        return this;
    }

    public rateDialog setNoStarsListener(NoStartReviewListener listener){
        this.noStartReviewListener =listener;
        return this;
    }

    public rateDialog setColor(int color){
        starColor=color;
        return this;
    }

}
