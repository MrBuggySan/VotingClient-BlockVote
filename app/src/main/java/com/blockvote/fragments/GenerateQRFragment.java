package com.blockvote.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.QRCreatorService;
import com.blockvote.crypto.TokenRequest;
import com.blockvote.interfaces.RegistrationDefaultInteractions;
import com.blockvote.votingclient.R;
import com.google.gson.Gson;
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

        //hide the rest of the layouts
        rootView.findViewById(R.id.genQR_UI).setVisibility(View.GONE);


        return rootView;
    }

    public void confirmNext(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.dialog_message_scanQR)
                .setTitle(R.string.dialog_title_WARNING);

        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Scan the registrar's QR code.
//                mListener.onNextGenQRSelected();

            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do nothing
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onBackGenQRSelected();
        void onNextGenQRSelected();
        void store_BlindedKey_RSAKeyParam(String jsonBlindedToken, String jsonRSAKeyParams);
    }


    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
        //editText.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake_error));
    }

    @Override
    public void onSelected() {
        //TODO: Only one QR generation for this electionInstance is allowed



        //TODO: get the electionInstance's blindedToken
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.globalSharedPrefKey), Context.MODE_PRIVATE);
        String electionURL = registrationDefaultInteractions.getActiveElection();
        String jsonElectionInstance = sharedPref.getString(electionURL, null);

        Gson gson = new Gson();
        electionInstance = gson.fromJson(jsonElectionInstance, ElectionInstance.class);


       //Create tokenRequest
        try{
            TokenRequest tokenRequest = electionInstance.getBlindedToken().generateTokenRequest();
            byte[] tokenMsg = tokenRequest.getMessage();

            //start generating the QR
            //Create the background service
            Intent mServiceIntent = new Intent(getActivity(), QRCreatorService.class);
            mServiceIntent.putExtra(getString(R.string.QRCreatorServiceString), Base64.encodeToString(tokenMsg, Base64.DEFAULT));
            getActivity().startService(mServiceIntent);

            registrationDefaultInteractions.setupQRReceiver();
            //TODO: When back is pressed, cancel the IntentService


        }catch(CryptoException cryptoException){
            //TODO: handle this
        }

        //TODO: I must somehow cache the QR so I don't have to generate it everytime.




    }

    @Override
    public VerificationError verifyStep() {
        //return null if the user can go to the next stepper_layout, create a new VerificationError instance otherwise
//        return TextUtils.isEmpty(editText.getText().toString())
//                ? new VerificationError("Password cannot be empty")
//                : null;
        return null;
    }

}
