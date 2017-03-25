package com.example.smileys;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Maura on 3/24/2017.
 */

public class Smiley extends ImageView {
    public boolean bouncing = true;
    public float vy = 1.0f;
    public float vx = 0.0f;
    public float gravity = 0.2f;
    public float bounceFactor = 0.7f;
    public int canvasHeight = 350;
    public int canvasWidth = 700;
    public float diameter = 60;
    public boolean endless = true;
    private Handler myHandler;
    private int counter = 0;

    public Smiley(Context context, AttributeSet attrs) {
        super(context, attrs);
        myHandler = new Handler();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        canvasHeight = displayMetrics.heightPixels;
        canvasWidth = displayMetrics.widthPixels;
        this.setVisibility(View.INVISIBLE);
    }

    private void resetVars(){
        this.setY(0.0f);
        vy = 1.0f;
        vx = 0.0f;
        counter = 0;
        bouncing = true;
        float rand_x = (float) Math.random() * (canvasWidth);
        this.setX(rand_x);
    }

    public void move(){
        resetVars();
        invalidate();
        this.setVisibility(View.VISIBLE);
        final View v = this;
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        update();
                        invalidate();
                    }
                });
            }
        },0,16);
    }

    public void update() {
        if (counter == 459 && endless){
            resetVars();
            bouncing = true;
        }
        counter++;
        if (bouncing == false)
        {
            return;
        }
        diameter = this.getWidth();
        // Now, lets make the ball move by adding the velocity vectors to its position
        this.setY(this.getY() + vy);
        // Ohh! The ball is moving!
        // Lets add some acceleration
        vy += gravity;
        //Perfect! Now, lets make it rebound when it touches the floor
        if(this.getY() + diameter > canvasHeight) {
            // First, reposition the ball on top of the floor and then bounce it!
            if (Math.abs(vy) < 8) {
                bouncing = false;
                //shapes.splice(index, 1);
                return;
            }
            this.setY(canvasHeight - diameter);
            vy *= -bounceFactor;
            // The bounceFactor variable that we created decides the elasticity or how elastic the collision will be. If it's 1, then the collision will be perfectly elastic. If 0, then it will be inelastic.
        }
    }
}
