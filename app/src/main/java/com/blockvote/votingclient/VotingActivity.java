package com.blockvote.votingclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.webkit.URLUtil;

import com.blockvote.auxillary.DataStore;
import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.ElectionState;
import com.blockvote.auxillary.FinishedElectionList;
import com.blockvote.auxillary.OngoingElectionList;
import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.fragments.NewElectionFragment;
import com.blockvote.fragments.SelectCandidateFragment;
import com.blockvote.fragments.VoteLater;
import com.blockvote.fragments.VoteNow;
import com.blockvote.fragments.VoteNowOrLaterFragment;
import com.blockvote.interfaces.DefaultInteractions;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * Created by Beast Mode on 12/26/2016.
 */

public class VotingActivity extends AppCompatActivity implements DefaultInteractions,
NewElectionFragment.NewElectionOnClick,
        VoteNowOrLaterFragment.OnVoteNowOrLaterInteractionListener,
        SelectCandidateFragment.OnClickSubmitBallot{


    private final String LOG_TAG = VotingActivity.class.getSimpleName();
    private ElectionInstance electionInstance;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_voting_activity, menu);
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
        Toolbar myToolbar = (Toolbar) findViewById(R.id.votingactivity_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar myActionBar = (ActionBar) getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(true);

        //determine which fragment to display
        Intent intent = getIntent();
        if(intent.getBooleanExtra(getString(R.string.newelectionKey), true)){
            NewElectionFragment newElectionFragment = new NewElectionFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, newElectionFragment).commit();
            return;
        }else{
            //Determine if the election is available
            int id = intent.getIntExtra(getString(R.string.electionIDKey), -1);
            OngoingElectionList ongoingElectionList = DataStore.getOngoingElectionList(this);
            electionInstance = ongoingElectionList.getElectionWithID(id);
            changeTitleBarName(electionInstance.getElectionName());
            //Calculate the time remaining or time to open
            String timeString = electionInstance.getTimeString();
            if(!electionInstance.isOpenForVoting()){
                VoteLater voteLater = new VoteLater();
                getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, voteLater).commit();
                return;
            }else{
                VoteNow voteNow = new VoteNow();
                getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, voteNow).commit();
                return;
            }

        }
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

    @Override
    public void onScanQRCodeClick(){

        //Call the ScanQRfragment
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setPrompt("");
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d(LOG_TAG, "Cancelled scan");
                ToastWrapper.initiateToast(this, "Cancelled");
            } else {
                String contents = result.getContents();
                try{
                    JSONObject jsonTest = new JSONObject(contents);
                    String electionURL = jsonTest.getString("url");
                    String registrarName = jsonTest.getString("registrar");
//                    ToastWrapper.initiateToast(this, electionURL + "\n" + registrarName);
                    if(!URLUtil.isValidUrl(electionURL)){
                        ToastWrapper.initiateToast(this, "This QR does not have a valid URL.");
                        return;
                    }
                    Intent intent = new Intent(this, RegistrationActivity.class);
                    intent.putExtra(getString(R.string.newelectionKey), true);
                    intent.putExtra(getString(R.string.isManualFormKey), false);
                    intent.putExtra(getString(R.string.electionURLKey), electionURL);
                    intent.putExtra(getString(R.string.regigstrarNameKey), registrarName);

                    startActivity(intent);
                }catch(Exception e){
                    ToastWrapper.initiateToast(this, "The QR code scanned is not valid." );
                    Log.e(LOG_TAG, "fail to read the json from QR ");
                }
            }
        }
    }

    @Override
    public void onManualOptionClick(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.putExtra(getString(R.string.newelectionKey), true);
        intent.putExtra(getString(R.string.isManualFormKey), true);
        startActivity(intent);
    }

    @Override
    public void onVoteNowButtonInteraction(){
        //go to the ballots
        SelectCandidateFragment selectCandidateFragment = new SelectCandidateFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ElectionContainer, selectCandidateFragment);
        //add the transaction to the BackStack
        transaction.addToBackStack("Transition to SelectCandidateFragment");
        transaction.commit();
    }

    @Override
    public void onVoteLaterButtonInteraction(){
        //TODO:go back to main page
    }

    @Override
    public void onYesConfirmCandidateSelect(){
        //Change the state to POST_VOTING
        //TODO:change this directly for finishedElections
        updateElectionInstanceState(ElectionState.POST_VOTING);

        //move the electionInstance to finishedElectionsList
        FinishedElectionList finishedElectionList = DataStore.getFinishedElectionList(this);
        OngoingElectionList ongoingElectionList = DataStore.getOngoingElectionList(this);

        ongoingElectionList.finishedElection(electionInstance, finishedElectionList);

        DataStore.saveOngoingElectionList(this, ongoingElectionList);
        DataStore.saveFinishedElectionList(this, finishedElectionList);


        // call the PostVotingActivity
        Intent intent = new Intent(this, PostVotingActivity.class);
        intent.putExtra(getString(R.string.electionIDKey), electionInstance.getId());
        startActivity(intent);
        return;


    }

    public boolean updateElectionInstanceState(ElectionState electionState){
        OngoingElectionList ongoingElectionList = DataStore.getOngoingElectionList(this);
        if(electionInstance != null && ongoingElectionList !=null ){
            electionInstance.setElectionState(electionState);
            //update the electionInstance in ElectionList as well
            ongoingElectionList.updateElection(electionInstance);
            //save the ongoingElectionList to data store
            DataStore.saveOngoingElectionList(this, ongoingElectionList);
            return true;
        }else{
            ToastWrapper.initiateToast(this, "It is not possible to update an election that was never in the list.");
            return false;
        }
    }

    @Override
    public void onBackPressed(){
        Log.d(LOG_TAG, "Back pressed");
        //Call the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
