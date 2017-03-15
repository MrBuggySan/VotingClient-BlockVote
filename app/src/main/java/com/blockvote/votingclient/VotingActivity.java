package com.blockvote.votingclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.blockvote.auxillary.ElectionInstance;

/**
 * Created by Beast Mode on 12/26/2016.
 */

public class VotingActivity extends AppCompatActivity {


    private final String LOG_TAG = VotingActivity.class.getSimpleName();
    private ElectionInstance electionInstance;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_election_activity, menu);
        return true;
    }


    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

    }


}
