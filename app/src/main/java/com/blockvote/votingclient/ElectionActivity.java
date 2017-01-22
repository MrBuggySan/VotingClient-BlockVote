package com.blockvote.votingclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
        BallotConfirmationFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener,
        RegistrationConfirmationFragment.OnFragmentInteractionListener{
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

        //select the appropriate fragment to display according to the data from the DB, (for now I will use the simple SharedPreferences)
        SharedPreferences dataStore = getPreferences(MODE_PRIVATE);
        stateKey += electionName;
        if (!dataStore.contains(stateKey)){
            //Init the state
            SharedPreferences.Editor editor = dataStore.edit();
            editor.putString(stateKey, getString(R.string.RegistrationState));
            editor.commit();
        }
        //get the current state
        String currentState =dataStore.getString(stateKey, "error");
        if(currentState.equals("error")){
            Log.e(LOG_TAG, "Could not find the current state of ElectionActivity");
            throw new RuntimeException(LOG_TAG + " could not find the state.");
        }

        if (savedInstanceState == null) {
            if(currentState.equals(R.string.RegistrationState)){
                RegisterFragment registerFragment = new RegisterFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, registerFragment).commit();
                return;
            }
            if(currentState.equals(R.string.RegistrationStatusState)){
                RegistrationStatusFragment registrationStatusFragment = new RegistrationStatusFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, registrationStatusFragment).commit();
                return;
            }
            if(currentState.equals(R.string.VoteButtonState)){
                VoteButtonFragment voteButtonFragment = new VoteButtonFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, voteButtonFragment).commit();
                return;
            }
            if(currentState.equals(R.string.ReviewBallotState)){
                ReviewBallotFragment reviewBallotFragment = new ReviewBallotFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, reviewBallotFragment).commit();
                return;
            }
            Log.e(LOG_TAG, "Could not start the appropriate fragment");
            throw new RuntimeException(LOG_TAG + "fragment missing... ");


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
     * @param choice
     * @param timestamp
     */
    public void onOptionSelectInteraction(String choice, String timestamp){
        //TODO: get the firstName and lastName from sharedPreferences

        BallotConfirmationFragment ballotConfirmationFragment = BallotConfirmationFragment.newInstance(choice, timestamp);
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

    public void onRegisterButtonInteraction(String firstName, String lastName){
        RegistrationConfirmationFragment registrationConfirmationFragment = RegistrationConfirmationFragment.newInstance(
                firstName, lastName
        );

        //Switch the RegisterFragment with the RegistrationConfirmationFragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ElectionContainer, registrationConfirmationFragment);
        //add the transaction to the BackStack
        transaction.addToBackStack("Transition to RegistrationConfirmationFragment");
        transaction.commit();
        Log.d(LOG_TAG,"Opening RegistrationConfirmationFragment ");


    }

    public void onYesRegistrationInteraction(String voterName, boolean sentSuccesfully ){

        //Save the voter name in DB, for now use SharedPreferences
        String voterKey = getString(R.string.voterNameKey);
        SharedPreferences dataStore = getPreferences(MODE_PRIVATE);
        if (dataStore.contains(voterKey)){
            //SharedPrefences already contians a key
            String voterNameError = dataStore.getString(voterKey, "noName");
            Log.e(LOG_TAG, voterNameError + " is already in SharedPrefences, you must delete it");
            throw new RuntimeException(LOG_TAG
                    + " must delete the voter name inside SharedPreferences before trying to add a new one");
        }
        SharedPreferences.Editor editor = dataStore.edit();
        editor.putString(voterKey, voterName);

        //change the state of ElectionActivity to RegistrationStatusState
        String newState = getString(R.string.RegistrationStatusState);
        editor.putString(stateKey, newState);
        //change the status of RegistrationStatusState
        String regSentSucessKey = getString(R.string.regSentSucess);
        editor.putBoolean(regSentSucessKey, sentSuccesfully);
        editor.commit();

        //TODO: Start the RegistrationStatusFragment
        RegistrationStatusFragment registrationStatusFragment = RegistrationStatusFragment.newInstance(
                voterName, sentSuccesfully
        );

        FragmentManager fragmentManager= getSupportFragmentManager();

        //Pop all of the previous transactions

        //Switch the RegisterFragment with the RegistrationConfirmationFragment
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.ElectionContainer, registrationStatusFragment);
        transaction.commit();
        Log.d(LOG_TAG,"Opening registrationStatusFragment ");



    }
    public void onNoRegistrationInteraction(){
        //TODO: switch with RegisterFragment
    }

}
