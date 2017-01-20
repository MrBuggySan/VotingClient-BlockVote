package com.blockvote.votingclient;

import android.content.Intent;
import android.content.SharedPreferences;
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
        SelectCandidateFragment.OnFragmentInteractionListener,
        BallotConfirmationFragment.OnFragmentInteractionListener{
    private final String LOG_TAG = ElectionActivity.class.getSimpleName();
    private String stateKey = getString(R.string.ElectionActivityState);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election);

        //get the extra data from intent
        Intent intent = getIntent();

        String electionName=intent.getStringExtra(getString(R.string.selectedElectionKey));
        Log.d(LOG_TAG, electionName + " selected");
        //Toolbar setuo
        Toolbar myToolbar = (Toolbar) findViewById(R.id.electionmain_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar myActionBar = (ActionBar) getSupportActionBar();
        // Set the tool bar back button
        myActionBar.setDisplayHomeAsUpEnabled(true);
        myActionBar.setTitle(electionName);

        //TODO: add a developer only reset button to reset the state. (This is for testing purposes only)

        //TODO:select the appropriate fragment to display according to the data from the DB, (for now I will use the simple SharedPreferences)
        SharedPreferences dataStore = getPreferences(MODE_PRIVATE);
        stateKey += electionName;
        if (!dataStore.contains(stateKey)){
            //Init the state
            SharedPreferences.Editor editor = dataStore.edit();
            editor.putString(stateKey, getString(R.string.RegistrationState));
        }
        //get the current state
        String currentState =dataStore.getString(stateKey, "error");
        if(currentState.equals("error")){
            Log.e(LOG_TAG, "Could not find the current state of ElectionActivity");
            throw new RuntimeException("State missing... ");
        }

        if (savedInstanceState == null) {
            if(currentState.equals(R.string.RegistrationState)){
                RegisterFragment registerFragment = new RegisterFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, registerFragment).commit();
            }
            if(currentState.equals(R.string.RegistrationStatusState)){
                RegistrationStatusFragment registrationStatusFragment = new RegistrationStatusFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, registrationStatusFragment).commit();
            }
            if(currentState.equals(R.string.VoteButtonState)){
                VoteButtonFragment voteButtonFragment = new VoteButtonFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, voteButtonFragment).commit();
            }
            if(currentState.equals(R.string.ReviewBallotState)){
                ReviewBallotFragment reviewBallotFragment = new ReviewBallotFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, reviewBallotFragment).commit();
            }
            Log.e(LOG_TAG, "Could not start the appropriate fragment");
            throw new RuntimeException("fragment missing... ");


        }else{
            //TODO: protect the activity and its current fragments if it is somehow destroyed
            Log.e(LOG_TAG, "The Election Activity was temporarily destroyed, and now it has nowhere to go...");
            throw new RuntimeException("Unexpected destruction of Election Activity");
        }




    }

    /**
     * VoteButtonFragment will be switched to the SelectcandidateFragment
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
     * SelectCandidateFragment will be switched with BallotConfirmationFragment
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

    public void onYesBallotConfirmation(){
        Log.d(LOG_TAG, "Voter confirms");
    }

    public void onNoBallotConfirmation(){
        Log.d(LOG_TAG, "Voter does not confirm");
    }


}
