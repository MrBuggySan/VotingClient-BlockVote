package com.blockvote.fragments;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.interfaces.RegistrationDefaultInteractions;
import com.blockvote.votingclient.R;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

public class ManualForm extends RegistrationFormFragment {
    private final String LOG_TAG = ManualForm.class.getSimpleName();

    @Override
    public void EditUI(RegistrationDefaultInteractions registrationDefaultInteractions){
        //Hide some of the ui components
        rootView.findViewById(R.id.registration_loadingPanel1).setVisibility(View.GONE);
        rootView.findViewById(R.id.registration_loadingPanel2).setVisibility(View.GONE);
        rootView.findViewById(R.id.regis_districtregistrar_ui).setVisibility(View.GONE);

        //TODO: Display the data from the downloads
        TextView blurb1 = (TextView) rootView.findViewById(R.id.regform_blurb1);
        blurb1.setText("Please fill in the URL and the required fields for the election.");

        //TODO: Edit the title name
        registrationDefaultInteractions.changeTitleBarName("New Election");

        //TODO: When Set is pressed, change the title of the tab
        Button setButton = (Button) rootView.findViewById(R.id.regis_setButton);
        setButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setupRemainingForm();
            }
        });
    }

    public void setupRemainingForm(){
        rootView.findViewById(R.id.regis_districtregistrar_ui).setVisibility(View.GONE);
        EditText urlEditText = (EditText)rootView.findViewById(R.id.regform_urledittext);
        if(TextUtils.isEmpty(urlEditText.getText().toString())){
            ToastWrapper.initiateToast(getContext(), "Please enter the election's URL");
            return;
        }
        String electionURL = urlEditText.getText().toString();

        //Test the electionURL if it is a valid URL
        if(!URLUtil.isValidUrl(electionURL)){
            ToastWrapper.initiateToast(getContext(), "Please enter a valid URL");
            return;
        }
        //TODO: help out the user so they do not have to write https://

        //Show the loading animation
        rootView.findViewById(R.id.registration_loadingPanel2).setVisibility(View.VISIBLE);

        //Download the data
        getElectionInfo(electionURL);



    }

    @Override
    public void stopLoadingAnimOnSuccess(){
        rootView.findViewById(R.id.registration_loadingPanel1).setVisibility(View.GONE);
        rootView.findViewById(R.id.registration_loadingPanel2).setVisibility(View.GONE);
        rootView.findViewById(R.id.regis_districtregistrar_ui).setVisibility(View.VISIBLE);
    }

    @Override
    public void stopLoadingAnimOnFail(){
        rootView.findViewById(R.id.registration_loadingPanel2).setVisibility(View.GONE);
    }

    @Override
    public void disableEditableViews(){
        //Make the fields uneditable.
        Log.d(LOG_TAG,"the spinners and the url field should be uneditable by now.");
        Spinner districtSpinner = (Spinner) rootView.findViewById(R.id.register_districtspinner);
        districtSpinner.setEnabled(false);
        Spinner registrarSpinner = (Spinner) rootView.findViewById(R.id.register_registrar_spinner);
        registrarSpinner.setEnabled(false);
        EditText urlEditText = (EditText) rootView.findViewById(R.id.regform_urledittext);
        urlEditText.setEnabled(false);
        Button setButton = (Button) rootView.findViewById(R.id.regis_setButton);
        setButton.setEnabled(false);
    }

    @Override
    public void prefillEditableViews(ElectionInstance electionInstance){
        ArrayList<String> registrarList = new ArrayList<String>();
        registrarList.add(electionInstance.getRegistrarName());
        displayRegistrarSpinner(registrarList);

        ArrayList<String> districtList = new ArrayList<String>();
        districtList.add(electionInstance.getDistrictName());
        displayDistrictsonSpinner(districtList);

        EditText urlEditText = (EditText) rootView.findViewById(R.id.regform_urledittext);
        urlEditText.setText(electionInstance.getElectionURL());
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        //ToastWrapper.initiateToast(getContext(), error.getErrorMessage());
    }

    @Override
    public void onSelected() {
        //update UI when selected
    }

    @Override
    public VerificationError verifyStep() {
        if(skipChecks) return null;
        if(hasValidElectionURL){
            //Attempt to insert the new electionInstance to the dataStore
            saveElectionInstance();
            if(isReadyForNextStep){
                return null;
            }
            else {
                return new VerificationError("You must enter the election's URL.");
            }
        }else{
            ToastWrapper.initiateToast(getContext(), "You must set the election's URL.");
            return new VerificationError("You must enter the election's URL.");
        }

    }


}
