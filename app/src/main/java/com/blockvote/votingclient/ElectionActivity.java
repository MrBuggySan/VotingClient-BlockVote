package com.blockvote.votingclient;

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

import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.fragments.GenerateQRFragment;
import com.blockvote.fragments.RegistrationFormFragment;
import com.blockvote.fragments.ReviewBallotFragment;
import com.blockvote.fragments.SelectCandidateFragment;
import com.blockvote.fragments.VoteButtonFragment;

/**
 * Created by Beast Mode on 12/26/2016.
 */

public class ElectionActivity extends AppCompatActivity
        implements RegistrationFormFragment.OnFragmentInteractionListener,
        GenerateQRFragment.OnFragmentInteractionListener,
        VoteButtonFragment.OnFragmentInteractionListener,
        SelectCandidateFragment.OnFragmentInteractionListener,
        ReviewBallotFragment.OnFragmentInteractionListener {


    private final String LOG_TAG = ElectionActivity.class.getSimpleName();
    private String electionStateKey ;
    private String voterNameKey;
    private String districtKey;
    private String registrarNameKey;
    private String electionName;



    private SharedPreferences dataStore;



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
                SharedPreferences.Editor editor = dataStore.edit();
                //only delete the data associated for this election
                editor.remove(voterNameKey);

                editor.remove(electionStateKey);
                editor.commit();

                ToastWrapper.initiateToast(this, "Data cleared, press back on this activity" +
                        " and start over.");
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

        //TODO:get the election name from the server
        electionName = "False election";

        //Toolbar setup
        Toolbar myToolbar = (Toolbar) findViewById(R.id.electionmain_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar myActionBar = (ActionBar) getSupportActionBar();
        myActionBar.setTitle(electionName);
        dataStore = getPreferences(MODE_PRIVATE);
        //define the keys for this particular election
        electionStateKey=  getString(R.string.ElectionActivityStateKey)+ electionName;
        voterNameKey = getString(R.string.voterNameKey)+ electionName;
        districtKey = getString(R.string.districtKey) + electionName;
        registrarNameKey = getString(R.string.regigstrarNameKey) + electionName;
        if (!dataStore.contains(electionStateKey)){
            //This is the first time the user has opened the app
            SharedPreferences.Editor editor = dataStore.edit();
            editor.putString(electionStateKey, getString(R.string.RegistrationState));
            editor.commit();
        }
        //get the current state
        String currentState = dataStore.getString(electionStateKey, "error");
        if(currentState.equals("error")){
            Log.e(LOG_TAG, "Could not find the current state of ElectionActivity");
            throw new RuntimeException(LOG_TAG + " could not find the state.");
        }
        Log.d(LOG_TAG, currentState + " is the current state.");
        if (savedInstanceState == null) {
            if(currentState.equals(getString(R.string.RegistrationState))){
                RegistrationFormFragment registerFragment = new RegistrationFormFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, registerFragment).commit();
                return;
            }
            if(currentState.equals(getString(R.string.GenQRState))){


                return;
            }
            if(currentState.equals(getString(R.string.VoteButtonState))){
                //TODO:get the district name

                String voterName = dataStore.getString(voterNameKey, null);

                if(voterName == null){
                    Log.e(LOG_TAG, "failure to retrieve voter's name");
                    ToastWrapper.initiateToast(this, "failure to retrieve voter's name");
                }
                VoteButtonFragment voteButtonFragment = VoteButtonFragment.newInstance(voterName);
                getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, voteButtonFragment).commit();
                return;
            }
            if(currentState.equals(getString(R.string.ReviewBallotState))){
                //TODO:get the district name

                String voterName= dataStore.getString(voterNameKey, null);

                if(voterName == null){
                    Log.e(LOG_TAG, "failure to retrieve voter's name");
                    ToastWrapper.initiateToast(this, "failure to retrieve voter's name");
                }
                ReviewBallotFragment reviewBallotFragment = ReviewBallotFragment.newInstance(voterName);
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

    public void onDistrictListNextInteraction(String firstName, String lastName, String districtName, String registrarName){
        SharedPreferences.Editor editor = dataStore.edit();
        //Store the voter's specifics
        editor.putString(districtKey, districtName);
        editor.putString(voterNameKey, firstName + " " + lastName);
        editor.putString(registrarNameKey, registrarName);
        //change the state of ElectionActivity
        editor.putString(electionStateKey, getString(R.string.GenQRState));
        editor.commit();


        //Call the GenerateQRFragment
        GenerateQRFragment generateQRFragment = GenerateQRFragment.newInstance(null, null);
        //Switch the VoteButtonFragment with the SelectCandidateFragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ElectionContainer, generateQRFragment);
        //add the transaction to the BackStack
        transaction.addToBackStack("Transition to generateQRFragment");
        transaction.commit();
        Log.d(LOG_TAG,"Opening generateQRFragment ");
    }

    public void onBackGenQRSelected(){
        //change the state of ElectionActivity
        SharedPreferences.Editor editor = dataStore.edit();
        editor.putString(electionStateKey, getString(R.string.RegistrationState));
        editor.commit();
        //Activate RegistrationFormFragment again.
        FragmentManager fragmentManager= getSupportFragmentManager();

        //Pop all of the previous registration fragments
        fragmentManager.popBackStack("Transition to SelectRegistrarFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        Log.d(LOG_TAG,"Opening RegistrationFormFragment again.");

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





    public void onCandidateSelectInteraction(String choice, String timestamp){

    }


    public void onReviewBallotButtonPress(){

    }
    public void onReviewResultsButtonPress(){

    }
}
