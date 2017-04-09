package com.example.smileys;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.Collections;

public class StatsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private String[] myDataset = new String[1000];
    private String[] modeKeys = {"classicScores"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("High Scores");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        String userid = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = database.child("users").child(userid).child("scores");
        Query topScores = myRef
                .orderByChild("score");

        topScores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 1000)
                    myDataset = new String[1000];
                else
                    myDataset = new String[(int)dataSnapshot.getChildrenCount()];

                int counter = 0;
                for (DataSnapshot scoreSnapshot: dataSnapshot.getChildren()) {
                    ScoreObject highScore = scoreSnapshot.getValue(ScoreObject.class);
                    myDataset[counter] = highScore.score + " Date: " + highScore.date.toString();
                    counter++;
                }
                if (counter == 0){
                    myDataset = new String[1];
                    myDataset[1] = "No high scores found for this mode. Go catch some smileys!";
                }
                //reverse because data is sorted in ascending order from query
                for(int i = 0; i < myDataset.length / 2; i++)
                {
                    String temp = myDataset[i];
                    myDataset[i] = myDataset[myDataset.length - i - 1];
                    myDataset[myDataset.length - i - 1] = temp;
                }

                mAdapter = new MyAdapter(myDataset);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
