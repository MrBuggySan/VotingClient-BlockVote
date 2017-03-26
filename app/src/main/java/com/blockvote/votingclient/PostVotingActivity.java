package com.blockvote.votingclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.blockvote.auxillary.DataStore;
import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.FinishedElectionList;
import com.blockvote.fragments.ReviewBallotFragment;
import com.blockvote.fragments.ShowBallotFragment;
import com.blockvote.interfaces.DefaultInteractions;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class PostVotingActivity extends AppCompatActivity implements DefaultInteractions,
ReviewBallotFragment.OnReviewPress{
    private ElectionInstance electionInstance;
    private final String LOG_TAG = PostVotingActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_voting);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.postvoting_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar myActionBar = (ActionBar) getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int electionID = intent.getIntExtra(getString(R.string.electionIDKey), -1);

        FinishedElectionList finishedElectionList = DataStore.getFinishedElectionList(this);
        electionInstance = finishedElectionList.getElectionWithID(electionID);
        if(electionInstance == null){
            Log.e(LOG_TAG, "electionInstance is null, this should not have happened");
        }

        changeTitleBarName(electionInstance.getElectionName());

        ReviewBallotFragment reviewBallotFragment = new ReviewBallotFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.PostVoting_container, reviewBallotFragment).commit();

    }

    @Override
    public void changeTitleBarName(String name){
        ActionBar myActionBar = (ActionBar) getSupportActionBar();
        myActionBar.setTitle(name);
    }
    @Override
    public ElectionInstance getElectionInstance(){
        return electionInstance;
    }

    public void onReviewBallotButtonPress(){
        Log.d(LOG_TAG, "opening ShowBallotFragment");

        ShowBallotFragment showBallotFragment = new ShowBallotFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.PostVoting_container, showBallotFragment);
        //add the transaction to the BackStack
        transaction.addToBackStack("Transition to ShowBallotFragment");
        transaction.commit();
    }
    public void onReviewResultsButtonPress(){
        Intent intent = new Intent(this, WebViewActivity.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed(){
        Log.d(LOG_TAG, "Back pressed");
        //Call the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NO_ANIMATION);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}