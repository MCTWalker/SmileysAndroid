package com.example.smileys;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv=(TextView)findViewById(R.id.smileysText);
        Typeface face= Typeface.createFromAsset(getAssets(),
                "fonts/ComicNeue-Bold.ttf");

        tv.setTypeface(face);
    }
}
