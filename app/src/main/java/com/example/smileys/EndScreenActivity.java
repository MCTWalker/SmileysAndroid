package com.example.smileys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.provider.Settings.Secure;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EndScreenActivity extends AppCompatActivity {

    private final String TAG = "EndScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);

        // Write a message to the database
        ScoreObject score = new ScoreObject();
        score.userid = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        Integer numCaught = getIntent().getIntExtra("numCaught", 0);
        final String prev = getIntent().getStringExtra("prevActivity");
        Calendar c = Calendar.getInstance();

        score.score = numCaught;
        score.date = c.getTime();
        score.isAuthorized = true;

        database.child(prev + "Scores").push().setValue(score);
        database.child("users").child(score.userid).child("scores").push().setValue(score);

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
