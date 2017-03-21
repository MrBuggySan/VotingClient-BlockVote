package com.blockvote.fragments;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.votingclient.R;
import com.stepstone.stepper.VerificationError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ManualForm extends RegistrationFormFragment {
    private final String LOG_TAG = ManualForm.class.getSimpleName();

    @Override
    public void EditUI(){
        //Hide some of the ui components
        rootView.findViewById(R.id.registration_loadingPanel1).setVisibility(View.GONE);
        rootView.findViewById(R.id.registration_loadingPanel2).setVisibility(View.GONE);
        rootView.findViewById(R.id.regis_districtregistrar_ui).setVisibility(View.GONE);
//        rootView.findViewById(R.id.regform_blurb2).setVisibility(View.GONE);
//        rootView.findViewById(R.id.regisform_RescanQR).setVisibility(View.GONE);

        //Display the data from the downloads
        TextView blurb1 = (TextView) rootView.findViewById(R.id.regform_blurb1);
        blurb1.setText("Please fill in the URL and the required fields for the election.");

        //Edit the title name
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

        //Ping the server first, then get the electionInfo
        PingThenGetElectionInfo(electionURL);
    }

    @Override
    public void registarSpinnerSetup(){
        Spinner spinner = (Spinner) rootView.findViewById(R.id.register_districtspinner);
        //set up an event to change the registrarlist available when a district is chosen.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                TextView textView = (TextView) selectedItemView;
                String selectedDistrict = textView.getText().toString();
                Log.v(LOG_TAG, "District selected : " + selectedDistrict);

                ArrayList<String>  registrarsToDisplay = new ArrayList<String>();

                try{
                    for(int i = 0 ; i < registrarInfoList.size(); i++){
                        JSONObject registrarInfo = registrarInfoList.get(i);
                        if(registrarInfo.getString("RegistrationDistrict").equals(selectedDistrict)){
                            String registrarName = registrarInfo.getString("RegistrarName");
                            Log.v(LOG_TAG, registrarName + " is a registrar in " + selectedDistrict);
                            registrarsToDisplay.add(registrarName);
                        }
                    }
                    displayRegistrarSpinner(registrarsToDisplay);
                }catch(JSONException e){
                    Log.e(LOG_TAG, "fail to get the registrar name.\nstack trace: "  + e.getStackTrace());
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

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
