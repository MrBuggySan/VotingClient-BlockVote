package com.blockvote.votingclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.ElectionState;
import com.blockvote.auxillary.StepperAdapter;
import com.blockvote.fragments.ManualForm;
import com.blockvote.interfaces.RegistrationDefaultInteractions;
import com.google.gson.Gson;
import com.stepstone.stepper.StepperLayout;

public class RegistrationActivity extends AppCompatActivity implements RegistrationDefaultInteractions {
    private final String LOG_TAG = RegistrationActivity.class.getSimpleName();
    private StepperLayout mStepperLayout;
    private ElectionInstance electionInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //TODO: determine how I can manage the activeElection string

        //TODO: determine if I have to use FilledForm or ManualForm for this Election

        //TODO: save the selected RegistrationFormFragment inside datastore

        Toolbar myToolbar = (Toolbar) findViewById(R.id.registration_toolbar);
        setSupportActionBar(myToolbar);

        ManualForm manualForm= new ManualForm();

        //TODO: determine the correct position to start with depending on the state of the electionInstance
//        int startingStepPosition = 1;
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this, manualForm));

    }

    @Override
    public void changeTitleBarName(String name){
        ActionBar myActionBar = (ActionBar) getSupportActionBar();
        myActionBar.setTitle(name);
    }

    @Override
    public boolean saveElectionInstance(ElectionInstance electionInstance_){
        this.electionInstance = electionInstance_;

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.globalSharedPrefKey), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        //Store the electionInstance inside dataStore
        //The key is the election's URL
        Gson gson = new Gson();
        String jsonElectionInstance = gson.toJson(this.electionInstance);
        editor.putString(electionInstance.getElectionURL(), jsonElectionInstance);
        editor.commit();

        //TODO: Add the electionInstance to OnGoingElectionList inside the sharedPref

        //TODO: return true if added successfully, or return false if there is already an election with the same name.

        //TODO: return true for now
        return true;

    }

    public boolean updateElectionInstanceState(ElectionState electionState){
        if(electionInstance != null){
            electionInstance.setElectionState(electionState);
            //TODO: update the electionInstance in ElectionList
            return true;
        }else return false;
    }

    @Override
    public ElectionInstance getElectionInstance(){
        return electionInstance;
    }

    @Override
    public void setupQRReceiver(){
        // The filter's action is BROADCAST_ACTION
        IntentFilter statusIntentFilter = new IntentFilter(getString(R.string.BackgroundQRAction));
        // Instantiates a new DownloadStateReceiver
        RegistrationActivity.DownloadStateReceiver mDownloadStateReceiver =
                new DownloadStateReceiver();
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mDownloadStateReceiver,
                statusIntentFilter);
    }

    // Broadcast receiver for receiving status updates from the IntentService
    private class DownloadStateReceiver extends BroadcastReceiver
    {
        // Prevents instantiation
        private DownloadStateReceiver() {
        }

        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        @Override
        public void onReceive(Context context, Intent intent) {
            Bitmap bitmap = intent.getParcelableExtra(getString(R.string.GeneratedQR_from_background));
            Log.v(LOG_TAG, "Displaying the QR now, this is from the background service.");
            ImageView imageView = (ImageView) findViewById(R.id.image_QRCode);
            imageView.setImageBitmap(bitmap);


            findViewById(R.id.genQR_UI).setVisibility(View.VISIBLE);
            findViewById(R.id.QR_animation_view).setVisibility(View.GONE);

            // cache the QR for this electionInstance
            electionInstance.setQR_code(bitmap);
            updateElectionInstanceState(ElectionState.FIN_GEN_QR);
        }
    }


}
