package com.example.android.grabble_v4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.grabble_v4.data.SingleLetter;
import com.example.android.grabble_v4.data.LetterBag;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener, BoardAdapter.ListItemClickListener{

    List<SingleLetter> bag = new ArrayList<SingleLetter>();
    List<SingleLetter> board = new ArrayList<SingleLetter>();
    private BoardAdapter mBoardAdapter;
    RecyclerView mBoardRecView;
    TextView mLetterBuild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLetterBuild = (TextView) findViewById(R.id.word_builder);

        //RecycleView reference
        mBoardRecView = (RecyclerView)findViewById(R.id.scrabble_letter_list);
        //set Layout
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mBoardRecView.setLayoutManager(gridLayoutManager);
        newGame();
    /*    String example =String.valueOf(list.get(79).getLetter()) +" value: "+ String.valueOf(list.get(5).getValue());
        mLetterBag.setText(example);
        Log.i("tag", "abc");

    */

        mBoardAdapter = new BoardAdapter(this, board, this);
        mBoardRecView.setAdapter(mBoardAdapter);


    }


    public void newGame(){
        //create bag
        LetterBag.createScrabbleSet(bag);
        //Log.i("tag", String.valueOf(bag.get(6).getLetter_name()) +" value: "+ String.valueOf(bag.get(5).getLetter_value()));
        //pick initial 4 randomly into board, remove from bag. recycleview=board
        for(int i =0; i<4 ;i++){
            addLetterToBoard();
        }

    }

    public void addLetterToBoard(){
        RandomSelector randomSelector =new RandomSelector(bag);
        SingleLetter selectedLetter;
        selectedLetter = randomSelector.getRandom();
        //reduce from bag
        for(int j=0; j<bag.size(); j++){
            if(bag.get(j).letter_name.equals(selectedLetter.letter_name))
            {
                bag.get(j).reduce_letter_probability();
            }
        }
        board.add(selectedLetter);

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.get_letter:
                //mLetterBuild.setText(String.valueOf(tilesLeft(bag)));
                if(tilesLeft(bag)==0)
                {Toast.makeText(getApplicationContext(),"No tiles lift in bag",Toast.LENGTH_LONG).show();
                    break;}
                addLetterToBoard();
                mBoardAdapter.notifyDataSetChanged();
                break;
        }
    }

    public int tilesLeft(List<SingleLetter> list){
        int totalSum=0;
        for(SingleLetter item : list) {
            totalSum = totalSum + item.letter_probability;
        }
        return totalSum;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        mLetterBuild.setText(board.get(clickedItemIndex).getLetter_name());

    }
}
