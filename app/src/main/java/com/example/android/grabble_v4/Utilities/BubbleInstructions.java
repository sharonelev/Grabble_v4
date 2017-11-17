package com.example.android.grabble_v4.Utilities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.example.android.grabble_v4.MainActivity;
import com.example.android.grabble_v4.R;
import com.orhanobut.hawk.Hawk;

/**
 * Created by user on 16/11/2017.
 */

public class BubbleInstructions extends DialogFragment{

    private final static String INSTRUCTION_TEXT = "instruction_text";
    private final static String DIMENSION_X = "xDimension";
    private final static String DIMENSION_Y = "yDimension";
    private final static String ARROW_DIR = "arrow_direction";

    public String instructionText;
    public float xDimension;
    public float yDimension;
    public String arrowDirection;

    TextView instructions;
    BubbleLayout bubbleLayout;

    public static BubbleInstructions createInstance(String txt, float x, float y, String dir){
        BubbleInstructions bubbleInstructions = new BubbleInstructions();
        Bundle bundle= new Bundle();
        bundle.putString(INSTRUCTION_TEXT,txt);
        bundle.putFloat(DIMENSION_X,x);
        bundle.putFloat(DIMENSION_Y,y);
        bundle.putString(ARROW_DIR,dir);
        bubbleInstructions.setArguments(bundle);
        return bubbleInstructions;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //init
        View rootview = inflater.inflate(R.layout.bubble_instructions,container,true);
        instructionText =getArguments().getString(INSTRUCTION_TEXT);
        xDimension =getArguments().getFloat(DIMENSION_X);
        yDimension =getArguments().getFloat(DIMENSION_Y);
        arrowDirection =getArguments().getString(ARROW_DIR);
        instructions = (TextView)rootview.findViewById(R.id.instruction_bubble);
        bubbleLayout= (BubbleLayout) rootview.findViewById(R.id.bubble_layout);
        bubbleLayout.setBackgroundColor(Color.TRANSPARENT);
        //for transparency
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //for x y position
     //  final WindowManager.LayoutParams m = getDialog().getWindow().getAttributes();
       // m.x= (int)xDimension;
        //m.y= (int)yDimension;
        //msg 1
        WindowManager.LayoutParams p= getDialog().getWindow().getAttributes();
        //unclickable
        setCancelable(false);
        //fill in pararms
        Handler myHandler = new Handler();
        myHandler.postDelayed(new Runnable(){
            //Before run have the first one without run
            @Override
            public void run()
            {
                WindowManager.LayoutParams p= getDialog().getWindow().getAttributes();
                getDialog().getWindow().setGravity(Gravity.TOP);

                //p.x= (int)xDimension;
                //p.y= (int)yDimension;
                //msg 2
                bubbleLayout.setArrowDirection(ArrowDirection.LEFT);
                instructions.setText("message 1");

            }
        }, 0);
        myHandler.postDelayed(new Runnable(){
            @Override
            public void run()
            {
                getDialog().getWindow().setGravity(Gravity.BOTTOM);
                //WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
                WindowManager.LayoutParams p= getDialog().getWindow().getAttributes();
                p.x= -800;
                p.y= 550;
                instructions.setText("message 2");
                bubbleLayout.setArrowDirection(ArrowDirection.TOP);


            }
        }, 3000);
        myHandler.postDelayed(new Runnable(){
            @Override
            public void run()
            {
                WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
                p.x= 0;
                p.y= 550;
                instructions.setText("message 3");
                bubbleLayout.setArrowDirection(ArrowDirection.TOP);


            }
        }, 6000);

        return rootview;
    }

}
/*  switch (arrowDirection){
                    case "LEFT":
                        bubbleLayout.setArrowDirection(ArrowDirection.LEFT);
                        break;
                    case "RIGHT":
                        bubbleLayout.setArrowDirection(ArrowDirection.RIGHT);
                        break;
                    case "TOP":
                        bubbleLayout.setArrowDirection(ArrowDirection.TOP);
                        break;
                    case "BOTTOM":
                        bubbleLayout.setArrowDirection(ArrowDirection.BOTTOM);
                        break;
                }*/