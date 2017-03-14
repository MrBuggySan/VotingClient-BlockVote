package com.blockvote.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.ElectionState;
import com.blockvote.auxillary.QRCreatorService;
import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.crypto.TokenRequest;
import com.blockvote.interfaces.RegistrationDefaultInteractions;
import com.blockvote.votingclient.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.spongycastle.crypto.CryptoException;

public class GenerateQRFragment extends Fragment implements Step {
    private RegistrationDefaultInteractions registrationDefaultInteractions;
    private final String LOG_TAG = GenerateQRFragment.class.getSimpleName();
    private ElectionInstance electionInstance;


    private View rootView;

    public GenerateQRFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_generate_qr, container, false);

        //test if the electionInstance already has a QR code cached or its loading a QR still
        ElectionInstance electionInstance = registrationDefaultInteractions.getElectionInstance();
        if(electionInstance != null){
            ElectionState electionState = electionInstance.getElectionState();
            //hide the rest of the layouts

            if(electionState == ElectionState.START_GEN_QR ){
                //do nothing, we should not get here
                ToastWrapper.initiateToast(getContext(), "START_GEN_QR inside onCreate of GenerateQRFragment" +
                        " should not happen!");
                return rootView;
            }
            if(electionState == ElectionState.WORKING_GEN_QR){
                //do nothing, we are still waiting for the QR generation to finish
                return rootView;
            }
            if(electionState == ElectionState.FIN_GEN_QR){
                //display the cached QR
                Bitmap bitmap = electionInstance.getQR_code();
                Log.v(LOG_TAG, "Displaying the cached QR.");
                ImageView imageView = (ImageView) rootView.findViewById(R.id.image_QRCode);
                String registrarName = electionInstance.getRegistrarName();
                TextView textViewBlurb2 = (TextView) rootView.findViewById(R.id.GenQR_textBlurb2);
                textViewBlurb2.setText("Please show this QR code to your registrar, " + registrarName +
                        ". Press next When the registrar is done scanning.");
                imageView.setImageBitmap(bitmap);

                rootView.findViewById(R.id.genQR_UI).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.genQR_loadingQRUI).setVisibility(View.GONE);
                return rootView;
            }

        }else{
            rootView.findViewById(R.id.genQR_UI).setVisibility(View.GONE);
        }

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegistrationDefaultInteractions) {
            registrationDefaultInteractions = (RegistrationDefaultInteractions) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DefaultInteractions");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        registrationDefaultInteractions = null;
    }


    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
        //editText.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake_error));
        ToastWrapper.initiateToast(getContext(), error.getErrorMessage());
    }

    @Override
    public void onSelected() {
        //TODO: Only one QR generation for this electionInstance is allowed

        electionInstance = registrationDefaultInteractions.getElectionInstance();

        if(electionInstance == null){
            Log.d(LOG_TAG, "By this point we should have had some kind of electionInstance, but it" +
                    "appreas like we don't. Something went wrong.");
            ToastWrapper.initiateToast(getContext(),"By this point we should have had some kind of electionInstance, but it" +
                    "appreas like we don't. Something went wrong.");
            return;
        }

        if(electionInstance.getQR_code() == null && electionInstance.getElectionState() == ElectionState.START_GEN_QR){
            //Create tokenRequest
            try{
                TokenRequest tokenRequest = electionInstance.getBlindedToken().generateTokenRequest();
                byte[] tokenMsg = tokenRequest.getMessage();

                //start generating the QR
                //Create the background service
                Intent mServiceIntent = new Intent(getActivity(), QRCreatorService.class);
                mServiceIntent.putExtra(getString(R.string.QRCreatorServiceString), Base64.encodeToString(tokenMsg, Base64.DEFAULT));
                getActivity().startService(mServiceIntent);

                //TODO: update the electionInstance state
                registrationDefaultInteractions.updateElectionInstanceState(ElectionState.WORKING_GEN_QR);
                registrationDefaultInteractions.setupQRReceiver();



            }catch(CryptoException cryptoException){
                //TODO: handle this
            }
        }
    }

    @Override
    public VerificationError verifyStep() {
        ElectionInstance electionInstance = registrationDefaultInteractions.getElectionInstance();
        if(electionInstance.getElectionState() == ElectionState.FIN_GEN_QR ||
                electionInstance.getElectionState() == ElectionState.REGIS_FINAL_STEP){
            return null;
        }else{
            return new VerificationError("Your QR is not finished generating yet.");
        }

    }

}
