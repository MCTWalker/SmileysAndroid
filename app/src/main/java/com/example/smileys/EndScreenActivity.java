package com.example.smileys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EndScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);

        Integer numCaught = getIntent().getIntExtra("numCaught", 0);
        final String prev = getIntent().getStringExtra("prevActivity");

        TextView vNumCaught = (TextView) findViewById(R.id.youCaughtText);
        if (numCaught > 0)
            vNumCaught.setText("You caught " + numCaught.toString() + " smileys, enough to make a joke!");
        else {
            vNumCaught.setText("You caught " + numCaught.toString() + " smileys. Were you sleeping?");
            ImageView joke = (ImageView) findViewById(R.id.img_joke);
            joke.setVisibility(View.GONE);
            TextView vJokeText = (TextView) findViewById(R.id.jokeText);
            vJokeText.setVisibility(View.GONE);
        }
        Button btnAgain = (Button) findViewById(R.id.btn_play_again);
        Button btnMenu = (Button) findViewById(R.id.btn_menu);
        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prev.equals("classic"))
                    openClassic();
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenu();
            }
        });
    }

    public void openClassic(){
        Intent intent = new Intent(this, ClassicActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void openMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
