package com.example.smileys;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Maura on 4/15/2017.
 */

public class FloatingScore extends TextView {
    int score;
    Timer timer = new Timer();
    Context context;

    public FloatingScore(Context in_context) {
        super(in_context);
        this.context = in_context;
        this.setTextColor(getResources().getColor(R.color.mainText));
        this.setTextSize(getResources().getDimension(R.dimen.mainTextSize));
    }

    public void setDefaultParams(int in_score, int initialX, int initialY){
        score = in_score;
        this.setX(initialX);
        this.setY(initialY);
        this.setText(String.valueOf(in_score));
        invalidate();
    }
}
