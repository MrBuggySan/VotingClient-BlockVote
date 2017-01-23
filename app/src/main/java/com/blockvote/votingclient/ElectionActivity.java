package com.blockvote.votingclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

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
    private String electionStateKey ;
    private String voterKey;
    private String electionName;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_election_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Option_ClearData:
                //clear the SharedPreferences data
                SharedPreferences dataStore = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = dataStore.edit();
                //only delete the data associated for this election
                editor.remove(voterKey);

                editor.remove(electionStateKey);
                editor.commit();

                Context context = this;
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(context, "Data cleared, press back on this activity" +
                        " and start over.", duration).show();

                //TODO: exit the ElectionActivity

                Log.d(LOG_TAG, "Clear data option selected");
                return true;
            case R.id.Option_Help:
                //TODO: Start the HelpActivity
                Log.d(LOG_TAG, "Help option selected");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election);

        //get the extra data from intent
        Intent intent = getIntent();

        electionName=intent.getStringExtra(getString(R.string.selectedElectionKey));
        Log.d(LOG_TAG, electionName + " selected");
        //Toolbar setup
        Toolbar myToolbar = (Toolbar) findViewById(R.id.electionmain_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar myActionBar = (ActionBar) getSupportActionBar();
        // Set the tool bar back button
//        myActionBar.setDisplayHomeAsUpEnabled(true);
        myActionBar.setTitle(electionName);



        //select the appropriate fragment to display according to the data from the DB, (for now I will use the simple SharedPreferences)
        SharedPreferences dataStore = getPreferences(MODE_PRIVATE);
        //define the keys for this particular election
        electionStateKey=  getString(R.string.ElectionActivityState)+ electionName;

        voterKey = getString(R.string.voterNameKey)+ electionName;

        if (!dataStore.contains(electionStateKey)){
            //Init the state
            SharedPreferences.Editor editor = dataStore.edit();
            editor.putString(electionStateKey, getString(R.string.RegistrationState));
            editor.commit();
        }
        //get the current state
        String currentState =dataStore.getString(electionStateKey, "error");
        if(currentState.equals("error")){
            Log.e(LOG_TAG, "Could not find the current state of ElectionActivity");
            throw new RuntimeException(LOG_TAG + " could not find the state.");
        }
        Log.d(LOG_TAG, currentState + " is the current state.");
        if (savedInstanceState == null) {
            if(currentState.equals(getString(R.string.RegistrationState))){
                RegisterFragment registerFragment = new RegisterFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, registerFragment).commit();
                return;
            }
            if(currentState.equals(getString(R.string.RegistrationStatusState))){
                //grab the voters data from data store
                String voterName = dataStore.getString(voterKey, "error");


                Log.v(LOG_TAG, voterName + " has been retrieved from db.");
                RegistrationStatusFragment registrationStatusFragment = RegistrationStatusFragment.newInstance(voterName);
                getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, registrationStatusFragment).commit();
                return;
            }
            if(currentState.equals(getString(R.string.VoteButtonState))){
                VoteButtonFragment voteButtonFragment = new VoteButtonFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, voteButtonFragment).commit();
                return;
            }
            if(currentState.equals(getString(R.string.ReviewBallotState))){
                ReviewBallotFragment reviewBallotFragment = new ReviewBallotFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, reviewBallotFragment).commit();
                return;
            }
            Log.e(LOG_TAG, "Could not start the appropriate fragment");
            throw new RuntimeException(LOG_TAG + " fragment missing... ");


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

        //switch SelectCandidateFragment
        FragmentManager fragmentManager= getSupportFragmentManager();

        //Pop all of the previous registration fragments
        fragmentManager.popBackStack("Transition to BallotConfirmationFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        Log.d(LOG_TAG, "Voter does not confirm");
    }

    public void onRegisterButtonInteraction(String firstName, String lastName, String districtName){
        RegistrationConfirmationFragment registrationConfirmationFragment = RegistrationConfirmationFragment.newInstance(
                firstName, lastName, districtName
        );

        //Switch the RegisterFragment with the RegistrationConfirmationFragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ElectionContainer, registrationConfirmationFragment);
        //add the transaction to the BackStack
        transaction.addToBackStack("Transition to RegistrationConfirmationFragment");
        transaction.commit();
        Log.d(LOG_TAG,"Opening RegistrationConfirmationFragment");


    }

    public void onYesRegistrationInteraction(String voterName){

        //Save the voter name in DB, for now use SharedPreferences

        SharedPreferences dataStore = getPreferences(MODE_PRIVATE);
        if (dataStore.contains(voterKey)){
            //SharedPrefences already contians a key
            String voterNameError = dataStore.getString(voterKey, "noName");
            Log.e(LOG_TAG, voterNameError + " is already in SharedPrefences, you must delete it");
            throw new RuntimeException(LOG_TAG
                    + " must delete the voter name inside SharedPreferences before trying to add a new one");
        }

        Log.d(LOG_TAG, voterName + " is being saved in db");
        SharedPreferences.Editor editor = dataStore.edit();
        editor.putString(voterKey, voterName);

        //change the state of ElectionActivity to RegistrationStatusState
        String newState = getString(R.string.RegistrationStatusState);
        editor.putString(electionStateKey, newState);

        editor.commit();

        //Start the RegistrationStatusFragment


        RegistrationStatusFragment registrationStatusFragment = RegistrationStatusFragment.newInstance(
                voterName
        );

        FragmentManager fragmentManager= getSupportFragmentManager();

        //Pop all of the previous registration fragments
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        //Switch the RegisterFragment with the RegistrationConfirmationFragment
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.ElectionContainer, registrationStatusFragment);
        transaction.commit();
        Log.d(LOG_TAG,"Opening registrationStatusFragment ");


    }
    public void onNoRegistrationInteraction(){
        //switch with RegisterFragment
        FragmentManager fragmentManager= getSupportFragmentManager();

        //Pop the transaction to go back to RegisterFragment
        fragmentManager.popBackStack("Transition to RegistrationConfirmationFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Log.d(LOG_TAG,"Voter does not confirm registration request. ");
    }


}
