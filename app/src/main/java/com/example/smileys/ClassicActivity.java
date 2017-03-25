package com.example.smileys;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Maura on 3/24/2017.
 */

public class ClassicActivity extends AppCompatActivity {
    Handler myHandler = new Handler();
    String countdownText = "Time Left: 30";
    Integer countdownValue = 30;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic);
        final TextView countdownTxt =(TextView)findViewById(R.id.txt_countdown);
        Typeface face= Typeface.createFromAsset(getAssets(),
                "fonts/ComicNeue-Bold.ttf");
        countdownTxt.setTypeface(face);

        int countdownValue = 30;
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                createNewSmiley();
            }
        },0,500);

        new Timer().scheduleAtFixedRate(new TimerTask(){

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

    private void decrementCountdown(TextView v) {
        v.setText("Time Left: " + countdownValue.toString());
        countdownValue--;
    }

    private void createNewSmiley(){

    }
}
