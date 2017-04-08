package com.example.smileys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv=(TextView)findViewById(R.id.smileysText);
        Typeface face= Typeface.createFromAsset(getAssets(),
                "fonts/ComicNeue-Bold.ttf");

        tv.setTypeface(face);

        Button classicBtn = (Button)findViewById(R.id.btn_classic);
        Button statsBtn = (Button)findViewById(R.id.btn_stats);
        classicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openClassic();
            }
        });
        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStats();
            }
        });


        final Smiley smiley1 = (Smiley) findViewById(R.id.smiley1);
        final Smiley smiley2 = (Smiley) findViewById(R.id.smiley2);
        final Smiley smiley3 = (Smiley) findViewById(R.id.smiley3);

        smiley1.move();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                smiley2.move();
            }

        }, 1000);

        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                smiley3.move();
            }

        }, 2000);
    }

    public void openClassic(){
        Intent intent = new Intent(this, ClassicActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void openStats(){
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }
}
