package com.example.candycrush;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    GridLayout gridLayout;
    TextView scoreResult;
    int[] candies = {
            R.drawable.bluecandy,
            R.drawable.greencandy,
            R.drawable.orangecandy,
            R.drawable.purplecandy,
            R.drawable.redcandy,
            R.drawable.yellowcandy
    };

    int widthOfBlock, noOfBlock = 8, widthOfScreen, heightOfScreen;
    ArrayList<ImageView> candy = new ArrayList<>();
    int candyToBeDragged, candyToBeReplace, notCandy=R.drawable.ic_launcher_background;
    Handler handler;
    int interval = 100, score = 0;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scoreResult = findViewById(R.id.score);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthOfScreen   = displayMetrics.widthPixels;
        heightOfScreen  = displayMetrics.heightPixels;
        widthOfBlock = widthOfScreen / noOfBlock;
        createBoard();
        for(final ImageView imageView: candy){
            imageView.setOnTouchListener(new OnSwipeListener(this)
            {
                @Override
                void onSwipeLeft() {
                    super.onSwipeLeft();
                    candyToBeDragged = imageView.getId();
                    candyToBeReplace = candyToBeDragged - 1 ;
                    candyInterchange();
                }

                @Override
                void onSwipeRight() {
                    super.onSwipeRight();
                    candyToBeDragged = imageView.getId();
                    candyToBeReplace = candyToBeDragged + 1 ;
                    candyInterchange();
                }

                @Override
                void onSwipeTop() {
                    super.onSwipeTop();
                    candyToBeDragged = imageView.getId();
                    candyToBeReplace = candyToBeDragged - noOfBlock ;
                    candyInterchange();
                }

                @Override
                void onSwipeBottom() {
                    super.onSwipeBottom();
                    candyToBeDragged = imageView.getId();
                    candyToBeReplace = candyToBeDragged + noOfBlock ;
                    candyInterchange();
                }
            });

        }
        handler = new Handler();
        startRepeat();
    }

    private void checkRowForThree(){
        for (int i=0; i<62; i++){
            int choseCandy = (int) candy.get(i).getTag();
            boolean isBlank = choseCandy == notCandy;
            Integer[] notValid = {6,7,14,15,22,23,30,31,38,39,46,47,54,55};
            List<Integer> list = Arrays.asList(notValid);
            if(!list.contains(i)){
                int x=i;
                if((int) candy.get(x++).getTag()==choseCandy && !isBlank &&
                        (int) candy.get(x++).getTag()==choseCandy &&
                        (int) candy.get(x).getTag()==choseCandy){
                    score += 3;
                    scoreResult.setText(String.valueOf(score));
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x--;
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x--;
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                }
            }
        }
        moveDownCandies();
    }

    private void checkColumnForThree(){
        for (int i=0; i<47; i++){
            int choseCandy = (int) candy.get(i).getTag();
            boolean isBlank = choseCandy == notCandy;
                int x=i;
                if((int) candy.get(x).getTag()==choseCandy && !isBlank &&
                        (int) candy.get(x+noOfBlock).getTag()==choseCandy &&
                        (int) candy.get(x+2*noOfBlock).getTag()==choseCandy){
                    score += 3;
                    scoreResult.setText(String.valueOf(score));
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x = x + noOfBlock;
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x = x + noOfBlock;
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                }
        }
        moveDownCandies();
    }

    private void moveDownCandies(){
        Integer[] firstRow = {1,2,3,4,5,6,7};
        List<Integer> arrayList = Arrays.asList(firstRow);
        for(int i = 55; i >= 0; i--){
            if((int) candy.get(i+noOfBlock).getTag() == notCandy){
                candy.get(i+noOfBlock).setImageResource((int) candy.get(i).getTag());
                candy.get(i+noOfBlock).setTag(candy.get(i).getTag());
                candy.get(i).setImageResource(notCandy);
                candy.get(i).setTag(notCandy);

                if(arrayList.contains(i) && (int) candy.get(i).getTag() == notCandy){
                    int randomColor = (int) Math.floor(Math.random() * candies.length);
                    candy.get(i).setImageResource(candies[randomColor]);
                    candy.get(i).setTag(candies[randomColor]);
                }
            }
        }
        for(int i = 0; i < 8; i++){
            if((int) candy.get(i).getTag() == notCandy){
                int randomColor = (int) Math.floor(Math.random() * candies.length);
                candy.get(i).setImageResource(candies[randomColor]);
                candy.get(i).setTag(candies[randomColor]);
            }
        }
    }

    Runnable reapeatChecker = new Runnable() {
        @Override
        public void run() {
            try {
                checkRowForThree();
                checkColumnForThree();
                moveDownCandies();
            }
            finally {
                handler.postDelayed(reapeatChecker,interval);
            }
        }
    };

    void startRepeat(){
        reapeatChecker.run();
    }
    private void candyInterchange(){
        int background = (int) candy.get(candyToBeReplace).getTag();
        int background1 = (int) candy.get(candyToBeDragged).getTag();
        candy.get(candyToBeDragged).setImageResource(background);
        candy.get(candyToBeReplace).setImageResource(background1);
        candy.get(candyToBeDragged).setTag(background);
        candy.get(candyToBeReplace).setTag(background1);
    }

    private void createBoard() {
       gridLayout = findViewById(R.id.board);
       gridLayout.setRowCount(noOfBlock);
       gridLayout.setColumnCount(noOfBlock);
       // set up square
       gridLayout.getLayoutParams().width = widthOfScreen;
       gridLayout.getLayoutParams().height = widthOfScreen ;
       for(int i = 0; i< noOfBlock*noOfBlock; i++){
           ImageView imageView = new ImageView(this);
           imageView.setId(i);
           imageView.setLayoutParams(new android.view.ViewGroup.LayoutParams(widthOfBlock, widthOfBlock));
           imageView.setMaxHeight(widthOfBlock);
           imageView.setMaxWidth(widthOfBlock);
           //create value random
           int randomCandy = (int) Math.floor(Math.random() * candies.length);
           imageView.setImageResource(candies[randomCandy]);
           imageView.setTag(candies[randomCandy]);
           candy.add(imageView);
           gridLayout.addView(imageView);
       }
    }
}