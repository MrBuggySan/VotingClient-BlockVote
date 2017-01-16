package com.blockvote.votingclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

/**
 * Created by Beast Mode on 12/26/2016.
 */

public class ElectionActivity extends AppCompatActivity
        implements VoteButtonFragment.OnFragmentInteractionListener,
        SelectCandidateFragment.OnFragmentInteractionListener {
    private final String LOG_TAG = ElectionActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election);

        //get the extra data from intent
        Intent intent = getIntent();

        //TODO: use the electionName to access the DB later on
        //TODO: only the ElectionActivity will have access to the DB
        String electionName=intent.getStringExtra(getString(R.string.selectedElectionKey));

        //Toolbar setuo
        Toolbar myToolbar = (Toolbar) findViewById(R.id.electionmain_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar myActionBar = (ActionBar) getSupportActionBar();
        // Set the tool bar back button
        myActionBar.setDisplayHomeAsUpEnabled(true);
        myActionBar.setTitle(electionName);

        //TODO:select the appropriate fragment to display according to the data from the DB

        if (savedInstanceState == null) {

            //TODO: Call the VoteButtonFragment (for testing purposes only)
            VoteButtonFragment voteButtonFragment = new VoteButtonFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, voteButtonFragment).commit();

        }else{
            Log.d(LOG_TAG, "savedInstanceState is not null");
        }

        Log.d(LOG_TAG, electionName + " selected");


    }

    /**
     * VoteButtonFragment will call this function
     */
    public void onVoteButtonInteraction(){
        //Setup the SelectCandidateFragment
        SelectCandidateFragment selectCandidateFragment = SelectCandidateFragment.newInstance();
        //Switch the VoteButtonFragment with the SelectCandidateFragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ElectionContainer, selectCandidateFragment);
        //add the transaction to the BackStack
        transaction.addToBackStack("Transition to SelectCandidateFragment");
        transaction.commit();
        Log.d(LOG_TAG,"Opening SelectCandidateFragment ");
    }

    /**
     * SelectcandidateFragment will call this function
     * @param firstName
     * @param lastName
     * @param choice
     * @param timestamp
     */
    public void onOptionSelectInteraction(String firstName,
                                          String lastName,
                                          String choice, String timestamp){

        BallotConfirmationFragment ballotConfirmationFragment = BallotConfirmationFragment.newInstance(firstName,
                lastName, choice, timestamp);
        //Switch the SelectCandidateFragment with the BallotConfirmationFragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ElectionContainer, ballotConfirmationFragment);
        //add the transaction to the BackStack
        transaction.addToBackStack("Transition to BallotConfirmationFragment");
        transaction.commit();
        Log.d(LOG_TAG,"Opening BallotConfirmationFragment ");

    }


}
