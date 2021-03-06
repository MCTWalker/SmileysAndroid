package com.example.smileys;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Collections;

public class StatsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private String[] myDataset = new String[1000];
    private SimpleDateFormat sdf = new SimpleDateFormat("MMM d yyyy");
    private String selected = "classic";
    private String state = "own";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#77abff"));
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow);
        upArrow.setColorFilter( ContextCompat.getColor(this, R.color.mainText), PorterDuff.Mode.SRC_ATOP);

        ActionBar supportBar = getSupportActionBar();
        supportBar.setDisplayHomeAsUpEnabled(true);
        supportBar.setBackgroundDrawable(colorDrawable);

        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        String prev = getIntent().getStringExtra("prevActivity");

        if (prev != null)
            selected = prev;

        updateToolbarTitle();
        updateScores();
    }

    private void updateToolbarTitle(){
        ActionBar supportBar = getSupportActionBar();
        if (state.equals("own"))
            supportBar.setTitle(getResources().getString(R.string.highScores) + " - " + selected);
        else {
            supportBar.setTitle("Worldwide " + getResources().getString(R.string.highScores) + " - " + selected);
        }
    }

    private void updateScores() {
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        String userid = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef;
        if (state.equals("own")){
            myRef = database.child("users").child(userid).child(selected + "scores");
        } else {
            myRef = database.child(selected + "Scores");
        }
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
                    myDataset[counter] = highScore.score + "   - " + sdf.format(highScore.date);
                    counter++;
                }
                if (counter == 0){
                    myDataset = new String[1];
                    myDataset[0] = "No high scores found for this mode. Check your internet connection or go catch some smileys!";
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.own_classic:
                state = "own";
                selected = "classic";
                break;
            case R.id.own_intense:
                state = "own";
                selected = "intense";
                break;
            case R.id.worldwide_classic:
                state = "worldwide";
                selected = "classic";
                break;
            case R.id.worldwide_intense:
                state = "worldwide";
                selected = "intense";
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }

        updateToolbarTitle();
        updateScores();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
