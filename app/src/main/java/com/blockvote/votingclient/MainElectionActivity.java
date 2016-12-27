package com.blockvote.votingclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

/**
 * Created by Beast Mode on 12/26/2016.
 */

public class MainElectionActivity extends AppCompatActivity {
    private final String LOG_TAG = MainElectionActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electionmain);

        //get the extra data from intent
        Intent intent = getIntent();
        String electionName=intent.getStringExtra(getString(R.string.selectedElectionKey));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.electionmain_toolbar);
        setSupportActionBar(myToolbar);
        // Set the tool bar back button
        ActionBar myActionBar = (ActionBar) getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(true);

        myActionBar.setTitle(electionName);

        Log.d(LOG_TAG, electionName + " selected");

        //TODO: Call the corresponging fragment instead of only using the activity
        //TODO: Make a fragment 
    }

}
