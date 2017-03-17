package com.blockvote.votingclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.blockvote.auxillary.DataStore;
import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.OngoingElectionList;
import com.blockvote.fragments.NewElectionFragment;
import com.blockvote.fragments.VoteLater;
import com.blockvote.fragments.VoteNow;
import com.blockvote.interfaces.DefaultInteractions;
import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Created by Beast Mode on 12/26/2016.
 */

public class VotingActivity extends AppCompatActivity implements DefaultInteractions,
NewElectionFragment.NewElectionOnClick{


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

        //TODO: determine which fragment to display
        Intent intent = getIntent();
        if(intent.getBooleanExtra(getString(R.string.newelectionKey), true)){
            NewElectionFragment newElectionFragment = new NewElectionFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, newElectionFragment).commit();
            return;
        }else{
            //Determine if the election is available
            int index = intent.getIntExtra(getString(R.string.electionIndexKey), -1);
            OngoingElectionList ongoingElectionList = DataStore.getOngoingElectionList(this);
            electionInstance = ongoingElectionList.getElectionAt(index);
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
//        ToastWrapper.initiateToast(this, "Scan QR Code has been selected.");
        //Call the ScanQRfragment
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

    }

    @Override
    public void onManualOptionClick(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.putExtra(getString(R.string.newelectionKey), true);
        intent.putExtra(getString(R.string.isManualFormKey), true);
        startActivity(intent);
    }
}
