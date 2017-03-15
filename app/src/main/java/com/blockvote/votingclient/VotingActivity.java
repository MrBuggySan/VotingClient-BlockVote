package com.blockvote.votingclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.fragments.NewElectionFragment;
import com.blockvote.interfaces.DefaultInteractions;

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
        if(intent.getBooleanExtra(getString(R.string.newelectionKey), false)){
            NewElectionFragment newElectionFragment = new NewElectionFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.ElectionContainer, newElectionFragment).commit();
            return;
        }



        //TODO: Get the electionInstance from dataStore and decide which fragment to show

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
        ToastWrapper.initiateToast(this, "Scan QR Code has been selected.");
    }

    @Override
    public void onManualOptionClick(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.putExtra(getString(R.string.newelectionKey), true);
        intent.putExtra(getString(R.string.isManualFormKey), true);
        startActivity(intent);
    }
}
