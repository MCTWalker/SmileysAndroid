package com.example.smileys;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class IntenseActivity extends AppCompatActivity {
    Handler myHandler = new Handler();
    Integer numCaught = 0;
    public Integer lives = 3;
    int smileyRate = 900;
    int frownyRate = 900;
    Random rand = new Random();
    Timer smileyTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intense);

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

    }

    private void createNewSmiley(){
        final Context c = IntenseActivity.this;
        final Smiley smiley = new Smiley(c);
        final TextView numCaughtTxt =(TextView)findViewById(R.id.txt_caughtNumI);
        smiley.setLayoutParams(new ViewGroup.LayoutParams((int) SmileyUtil.pxFromDp(c, 100f), ViewGroup.LayoutParams.WRAP_CONTENT));
        smiley.setImageResource(getImgResource(c, "gray_smiley"));
        smiley.setScaleType(ImageView.ScaleType.FIT_XY);
        smiley.setAdjustViewBounds(true);
        smiley.endless = false;
        smiley.ia = this;
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
        FrameLayout fl = (FrameLayout) findViewById(R.id.intense_layout);
        fl.addView(smiley);
        smiley.fall();
        if (smileyRate > 399){
            smileyRate -= 50;
        }
        scheduleSmiley();
    }

    public void setLives(int num){
        lives = num;
        final TextView liveTxt =(TextView)findViewById(R.id.txt_lives);
        liveTxt.setText("Lives: " + lives.toString());
    }

    private void createNewFrowny(){
        final Context c = IntenseActivity.this;
        final Smiley smiley = new Smiley(c);
        final TextView numCaughtTxt =(TextView)findViewById(R.id.txt_caughtNumI);
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
                    setLives(lives - 1);
                    numCaughtTxt.setText(numCaught.toString());
                    if (lives == 0)
                        openEndScreen();
                }
            }
        });
        FrameLayout fl = (FrameLayout) findViewById(R.id.intense_layout);
        fl.addView(smiley);
        smiley.fall();
        frownyRate = rand.nextInt(8000);
        scheduleFrowny();
    }

    public void startFloatingScore(int in_x, int in_y) {
        final FloatingScore score = new FloatingScore(this);
        final Timer floatingTimer = new Timer();
        score.setDefaultParams(-1, in_x, in_y);
        FrameLayout fl = (FrameLayout) findViewById(R.id.intense_layout);
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

    public int getImgResource(Context context, String name) {
        int resId = context.getResources().getIdentifier(name, "drawable", "com.example.smileys");
        return resId;
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

    public void openEndScreen(){
        if (smileyTimer != null)
            smileyTimer.cancel();
        Intent intent = new Intent(this, EndScreenActivity.class);
        intent.putExtra("numCaught", numCaught);
        intent.putExtra("prevActivity", "intense");
        intent.putExtra("total", 0);
        startActivity(intent);
        this.finish();
    }
}
