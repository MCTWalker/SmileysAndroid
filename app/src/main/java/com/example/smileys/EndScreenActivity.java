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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EndScreenActivity extends AppCompatActivity {

    private final String TAG = "EndScreenActivity";
    boolean newHighScore = false;
    ScoreObject score = new ScoreObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);

        // Write a message to the database
        score.userid = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final Integer numCaught = getIntent().getIntExtra("numCaught", 0);
        final String prev = getIntent().getStringExtra("prevActivity");
        Calendar c = Calendar.getInstance();
        DatabaseReference myRef = database.child("users").child(score.userid).child(prev + "scores");
        final Query topScore = myRef
                .orderByChild("score").limitToLast(1);

        score.score = numCaught;
        score.date = c.getTime();
        score.isAuthorized = true;

        topScore.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot scoreSnapshot: dataSnapshot.getChildren()) {
                    ScoreObject highScore = scoreSnapshot.getValue(ScoreObject.class);
                    updateHighScore(highScore);
                    TextView vNumCaught = (TextView) findViewById(R.id.youCaughtText);
                    if (newHighScore)
                        vNumCaught.setText("Congratulations, you scored " + numCaught.toString() + "! That's a new highscore!");
                    else if (numCaught > 0 && prev.equals("intense")) {
                        vNumCaught.setText("You scored " + numCaught.toString() + "!");
                    }
                    else if (numCaught > 0 && prev.equals("classic")) {
                        final Integer totalCreated = getIntent().getIntExtra("total", 0);
                        vNumCaught.setText("You scored " + numCaught.toString() + " out of " + totalCreated + " smileys!");
                    }else {
                        vNumCaught.setText("You scored " + numCaught.toString() + ". Were you sleeping?");
                    }
                }

                topScore.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        database.child(prev + "Scores").push().setValue(score);
        database.child("users").child(score.userid).child(prev + "scores").push().setValue(score);

        Button btnAgain = (Button) findViewById(R.id.btn_play_again);
        Button btnMenu = (Button) findViewById(R.id.btn_menu);
        Button btnHighScores = (Button) findViewById(R.id.btn_highscores);
        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prev.equals("classic"))
                    openClassic();
                if (prev.equals("intense"))
                    openIntense();
            }
        });
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenu();
            }
        });
        btnHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStats();
            }
        });
    }

    private void updateHighScore(ScoreObject highScore) {
        if (highScore == null || score.score > highScore.score){
            newHighScore = true;
        }
    }

    public void openClassic(){
        Intent intent = new Intent(this, ClassicActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void openIntense(){
        Intent intent = new Intent(this, IntenseActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void openStats(){
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }

    public void openMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
