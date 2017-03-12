package com.blockvote.fragments;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.interfaces.DefaultInteractions;
import com.blockvote.votingclient.R;
import com.stepstone.stepper.VerificationError;

public class ManualForm extends RegistrationFormFragment {


    @Override
    public void EditUI(DefaultInteractions defaultInteractions){
        //Hide some of the ui components
        rootView.findViewById(R.id.registration_loadingPanel1).setVisibility(View.GONE);
        rootView.findViewById(R.id.registration_loadingPanel2).setVisibility(View.GONE);
        rootView.findViewById(R.id.regis_districtregistrar_ui).setVisibility(View.GONE);

        //TODO: Display the data from the downloads
        TextView blurb1 = (TextView) rootView.findViewById(R.id.regform_blurb1);
        blurb1.setText("Please fill in the URL and the required fields for the election.");

        //TODO: Edit the title name
        defaultInteractions.changeTitleBarName("New Election");

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
//        getElectionInfo(electionURL);



    }

    @Override
    public void stopLoadingAnim(){
        rootView.findViewById(R.id.registration_loadingPanel2).setVisibility(View.GONE);
        rootView.findViewById(R.id.regis_districtregistrar_ui).setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        ToastWrapper.initiateToast(getContext(), error.getErrorMessage());
    }

    @Override
    public void onSelected() {
        //update UI when selected
    }

    @Override
    public VerificationError verifyStep() {
        if(!isReadyForNextStep)
            return new VerificationError("You must enter the election's URL.");
        else {
            //TODO: Insert the electionInstance to the dataStore
            //saveElectionInstance();
            return null;
        }
    }


}
