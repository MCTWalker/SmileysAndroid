package com.example.smileys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Maura on 3/24/2017.
 */

public class ClassicActivity extends AppCompatActivity {
    Handler myHandler = new Handler();
    Integer countdownValue = 30;
    Random rand = new Random();
    Timer countdownTimer = new Timer();
    Timer smileyTimer = new Timer();
    Integer numCaught = 0;
    int smileyRate = 900;//milliseconds
    int frownyRate;
    int totalCreated = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic);
        final TextView countdownTxt =(TextView)findViewById(R.id.txt_countdown);

        frownyRate = rand.nextInt(8000);

        myHandler.postDelayed(new Runnable(){
            @Override
            public void run() {
                createNewSmiley();
            }
        }, 500);

        myHandler.postDelayed(new Runnable(){
            @Override
            public void run() {
                createNewFrowny();
            }
        }, frownyRate);

        countdownTimer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        decrementCountdown(countdownTxt);
                    }
                });
            }
        },0,1000);

    }

    public int getImgResource(Context context, String name) {
        int resId = context.getResources().getIdentifier(name, "drawable", "com.example.smileys");
        return resId;
    }

    private void decrementCountdown(TextView v) {
        v.setText("Time Left: " + countdownValue.toString());
        countdownValue--;
        if (countdownValue == -1){
            countdownTimer.cancel();
            smileyTimer.cancel();
            openEndScreen();
        }
    }

    public void openEndScreen(){
        Intent intent = new Intent(this, EndScreenActivity.class);
        intent.putExtra("numCaught", numCaught);
        intent.putExtra("prevActivity", "classic");
        intent.putExtra("total", totalCreated);
        startActivity(intent);
        this.finish();
    }

    private void createNewSmiley(){
        Log.d("ClassicActivity", "Total Created: " + totalCreated);
        totalCreated++;
        final Context c = ClassicActivity.this;
        final Smiley smiley = new Smiley(c);
        final TextView numCaughtTxt =(TextView)findViewById(R.id.txt_caughtNum);
        smiley.setLayoutParams(new ViewGroup.LayoutParams((int) SmileyUtil.pxFromDp(c, 100f), ViewGroup.LayoutParams.WRAP_CONTENT));
        smiley.setImageResource(getImgResource(c, "gray_smiley"));
        smiley.setScaleType(ImageView.ScaleType.FIT_XY);
        smiley.setAdjustViewBounds(true);
        smiley.endless = false;
        smiley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (smiley.caught == false) {
                    smiley.caught = true;
                    smiley.setImageResource(getImgResource(c, "smiley"));
                    numCaught++;
                    numCaughtTxt.setText(numCaught.toString());
                }
            }
        });
        FrameLayout fl = (FrameLayout) findViewById(R.id.classic_layout);
        fl.addView(smiley);
        smiley.move();
        if (smileyRate > 400){
            smileyRate -= 50;
        } else if (smileyRate > 300 && countdownValue < 10){
            smileyRate = 300;
        }
        if (countdownValue > 1){
            scheduleSmiley();
        }
    }

    private void createNewFrowny(){
        final Context c = ClassicActivity.this;
        final Smiley smiley = new Smiley(c);
        final TextView numCaughtTxt =(TextView)findViewById(R.id.txt_caughtNum);
        smiley.setLayoutParams(new ViewGroup.LayoutParams((int) SmileyUtil.pxFromDp(c, 100f), ViewGroup.LayoutParams.WRAP_CONTENT));
        smiley.setImageResource(getImgResource(c, "frowny"));
        smiley.setScaleType(ImageView.ScaleType.FIT_XY);
        smiley.setAdjustViewBounds(true);
        smiley.endless = false;
        smiley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (smiley.caught == false) {
                    smiley.caught = true;
                    startFloatingScore((int)smiley.getX(), (int)smiley.getY());
                    smiley.setImageResource(getImgResource(c, "red_frowny"));
                    numCaught--;
                    numCaughtTxt.setText(numCaught.toString());
                }
            }
        });
        FrameLayout fl = (FrameLayout) findViewById(R.id.classic_layout);
        fl.addView(smiley);
        smiley.move();
        frownyRate = rand.nextInt(8000);
        if (countdownValue > frownyRate/1000d) {
            scheduleFrowny();
        }
    }

    private void startFloatingScore(int in_x, int in_y) {
        final FloatingScore score = new FloatingScore(this);
        final Timer floatingTimer = new Timer();
        score.setDefaultParams(-1, in_x, in_y);
        FrameLayout fl = (FrameLayout) findViewById(R.id.classic_layout);
        fl.addView(score);
        floatingTimer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int y = (int)score.getY();
                        if (y > 0) {
                            y--;
                            score.setY(y);
                        } else {
                            floatingTimer.cancel();
                            score.setVisibility(View.GONE);
                        }
                    }
                });
            }
        },0,5);
    }

    private void scheduleSmiley() {
        smileyTimer.schedule(new TimerTask(){
            @Override
            public void run(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createNewSmiley();
                    }
                });
            }
        },smileyRate);
    }

    private void scheduleFrowny() {
        smileyTimer.schedule(new TimerTask(){
            @Override
            public void run(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createNewFrowny();
                    }
                });
            }
        },frownyRate);
    }
}
