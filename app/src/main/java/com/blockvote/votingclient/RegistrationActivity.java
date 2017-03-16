package com.blockvote.votingclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blockvote.auxillary.DataStore;
import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.ElectionState;
import com.blockvote.auxillary.HACKVERSION;
import com.blockvote.auxillary.OngoingElectionList;
import com.blockvote.auxillary.StepperAdapter;
import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.crypto.BlindedToken;
import com.blockvote.crypto.Token;
import com.blockvote.fragments.FilledForm;
import com.blockvote.fragments.ManualForm;
import com.blockvote.fragments.RegistrationFinalStepFragment;
import com.blockvote.fragments.RegistrationFormFragment;
import com.blockvote.interfaces.RegistrationDefaultInteractions;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.stepstone.stepper.StepperLayout;

import org.spongycastle.crypto.digests.SHA1Digest;
import org.spongycastle.crypto.engines.RSAEngine;
import org.spongycastle.crypto.params.RSAKeyParameters;
import org.spongycastle.crypto.signers.PSSSigner;

public class RegistrationActivity extends AppCompatActivity implements RegistrationDefaultInteractions,
        RegistrationFinalStepFragment.FinalStepQRCode{
    private final String LOG_TAG = RegistrationActivity.class.getSimpleName();
    private StepperLayout mStepperLayout;
    private ElectionInstance electionInstance;
    private View genQRrootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.registration_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar myActionBar = (ActionBar) getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        RegistrationFormFragment registrationFormFragment;
        int startingStepPosition = 0;
        if(intent.getBooleanExtra(getString(R.string.newelectionKey), true)){
            startingStepPosition = 0;
            //Either display ManualForm or FilledForm
            if(intent.getBooleanExtra(getString(R.string.isManualFormKey), true)){
                //display ManualForm
                registrationFormFragment = new ManualForm();
            }else{
                //display FilledForm
                registrationFormFragment = new FilledForm();
            }

        }else{
            //An electionInstance in the middle of registration is selected
            int index = intent.getIntExtra(getString(R.string.electionIndexKey), -1);
            if(index == -1 ){
                Log.e(LOG_TAG, "There should have been an index here");
                return;
            }
            OngoingElectionList ongoingElectionList = DataStore.getOngoingElectionList(this);
            electionInstance = ongoingElectionList.getElectionAt(index);
            startingStepPosition = 1;
            //always show the manual form
            registrationFormFragment = new ManualForm();
        }

        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this, registrationFormFragment),startingStepPosition);

    }

    @Override
    protected void onPause(){
        super.onPause();
        if(electionInstance !=null &&
                (electionInstance.getElectionState() == ElectionState.FIN_GEN_QR ||
                electionInstance.getElectionState() == ElectionState.REGIS_FINAL_STEP ||
                        electionInstance.getElectionState() == ElectionState.WORKING_GEN_QR)){
            //Reset the electionInstance to WORKING_GEN_QR
            updateElectionInstanceState(ElectionState.START_GEN_QR);
            Log.d(LOG_TAG, "onPause called, resetting the state of electionInstance");
            //TODO: kill the background service
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mDownloadStateReceiver);

        }

    }

    @Override
    public void changeTitleBarName(String name){
        ActionBar myActionBar = (ActionBar) getSupportActionBar();
        myActionBar.setTitle(name);
    }

    @Override
    public boolean saveNewElectionInstance(ElectionInstance electionInstance_){
        this.electionInstance = electionInstance_;
        //Add the electionInstance to OnGoingElectionList inside the sharedPref
        OngoingElectionList ongoingElectionList = DataStore.getOngoingElectionList(this);
        if(HACKVERSION.forDemo){
            electionInstance.setId(ElectionInstance.currentCount());
        }
        if(ongoingElectionList.addElection(electionInstance)){
            //the electionInstance has been added succesfully

            //save the ongoingElectionList to data store
            DataStore.saveOngoingElectionList(this, ongoingElectionList);
            return true;
        }else{
            return false;
        }

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
    public ElectionInstance getElectionInstance(){
        return electionInstance;
    }

    private RegistrationActivity.DownloadStateReceiver mDownloadStateReceiver;

    @Override
    public void setupQRReceiver(){
        // The filter's action is BROADCAST_ACTION
        IntentFilter statusIntentFilter = new IntentFilter(getString(R.string.BackgroundQRAction));

        // Instantiates a new DownloadStateReceiver
        mDownloadStateReceiver =
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
            ImageView imageView = (ImageView) genQRrootView.findViewById(R.id.image_QRCode);
            imageView.setImageBitmap(bitmap);

            genQRrootView.findViewById(R.id.genQR_UI).setVisibility(View.VISIBLE);
            genQRrootView.findViewById(R.id.genQR_loadingQRUI).setVisibility(View.GONE);
            String registrarName = electionInstance.getRegistrarName();
            TextView textViewBlurb2 = (TextView) genQRrootView.findViewById(R.id.GenQR_textBlurb2);
            textViewBlurb2.setText("Please show this QR code to your registrar, " + registrarName +
            ". Press next when the registrar is done scanning.");

            updateElectionInstanceState(ElectionState.FIN_GEN_QR);

        }
    }

    //Initiate the QR scanner at the final registration step
    public void onScanQRCodeClickFromFinalStep(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setPrompt("");
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    //Handle the data coming from QR scanner activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //verify authenticity of signedBlindedToken
                //Signature from registrar to be verified
                String signature = result.getContents();

                //get the blindedToken
                BlindedToken blindedToken = electionInstance.getBlindedToken();

                Token token = blindedToken.unblindToken(Base64.decode(signature, Base64.DEFAULT));

                //get the RSA parameters
                RSAKeyParameters rsaKeyParameters = electionInstance.getrSAkeyParams();

                byte[] signedTokenID = token.getID();
                byte[] signedTokenSig = token.getSignature();


                PSSSigner signer = new PSSSigner(new RSAEngine(), new SHA1Digest(), 20);
                signer.init(false, rsaKeyParameters);

                signer.update(signedTokenID, 0, signedTokenID.length);

                // Verify that the coin has a valid signature using our public key.
                if(signer.verifySignature(signedTokenSig)){
                    //good signature
                    Log.v(LOG_TAG, "The QR was from legit registrar");
                    //Update the state to PRE_VOTING
                    updateElectionInstanceState(ElectionState.PRE_VOTING);
                    electionInstance.setSignedTokenID(signedTokenID);
                    electionInstance.setSignedTokenSignature(signedTokenSig);
                    //Call the next Activity

                }else{
                    //badsignature
                    Log.e(LOG_TAG, "The QR scanned is not valid.");
                    ToastWrapper.initiateToast(this, "The QR code you scanned is not valid");
                }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void setGenQRrootView(View rootView_){
        this.genQRrootView = rootView_;
    }


}
