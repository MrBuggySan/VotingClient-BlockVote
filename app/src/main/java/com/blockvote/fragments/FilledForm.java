package com.blockvote.fragments;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.blockvote.auxillary.DataStore;
import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.interfaces.RegistrationDefaultInteractions;
import com.stepstone.stepper.VerificationError;
import com.blockvote.votingclient.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FilledForm extends RegistrationFormFragment {
    private final String LOG_TAG = FilledForm.class.getSimpleName();
    private String registrarName;

    @Override
    public void EditUI(){
        // take away the URL text and edittext
        rootView.findViewById(R.id.regform_URLtext).setVisibility(View.GONE);
        rootView.findViewById(R.id.regform_setURL).setVisibility(View.GONE);

        String[] dataFromQR = DataStore.getURLandRegistrarFromQR(getContext());
        if(dataFromQR[0] == null || dataFromQR[1] == null){
            Log.e(LOG_TAG, "There was no data from the QR that was saved. Error");
            return;
        }
        String electionURL = dataFromQR[0];
        registrarName = dataFromQR[1];
        PingThenGetElectionInfo(electionURL);
    }

    @Override
    public void stopLoadingAnimOnSuccess(){
        String districtName = "";
        //check validity of the registrar
        try{
            for(int i = 0 ; i < registrarInfoList.size(); i++){
                JSONObject registrarInfo = registrarInfoList.get(i);
                if(registrarInfo.getString("RegistrarName").equals(registrarName)){
                    districtName = registrarInfo.getString("RegistrationDistrict");
                    break;
                }
                if(i == registrarInfoList.size() - 1){
                    Log.e(LOG_TAG, "BlockVote does not have this registrar");
                    stopLoadingAnimOnFail();
                }
            }

            return;

        }catch(JSONException e){
            Log.e(LOG_TAG, "fail to get the registrar name.");
        }
        //display the name of the election
        String electionName = electionInstance.getElectionName();
        registrationDefaultInteractions.changeTitleBarName(electionName);

        Log.d(LOG_TAG, "Displaying " + registrarName + " in Spinner");
        ArrayList<String> registrarList = new ArrayList<String>();
        registrarList.add(registrarName);
        displayRegistrarSpinner(registrarList);

        ArrayList<String> districtList = new ArrayList<String>();
        districtList.add(districtName);
        displayDistrictsonSpinner(districtList);

        TextView blurb1 = (TextView) rootView. findViewById(R.id.regform_blurb1);
        blurb1.setText("Below is your " + electionInstance.getDistrictAlias() + " for the " + electionName);

        TextView blurb2 = (TextView) rootView. findViewById(R.id.regform_blurb2);
        blurb2.setText("If there is a mistake with the above information, please scan the QR again.");

        Spinner districtSpinner = (Spinner) rootView.findViewById(R.id.register_districtspinner);
        districtSpinner.setEnabled(false);
        Spinner registrarSpinner = (Spinner) rootView.findViewById(R.id.register_registrar_spinner);
        registrarSpinner.setEnabled(false);


    }

    @Override
    public void stopLoadingAnimOnFail(){
        ToastWrapper.initiateToast(getContext(), "The QR code you scanned is not valid");
        getActivity().finish();
    }

    @Override
    public void disableEditableViews(){
        //There is no need to do this for FilledForm
    }

    @Override
    public void prefillEditableViews(ElectionInstance electionInstance){
        ArrayList<String> registrarList = new ArrayList<String>();
        registrarList.add(electionInstance.getRegistrarName());
        displayRegistrarSpinner(registrarList);

        ArrayList<String> districtList = new ArrayList<String>();
        districtList.add(electionInstance.getDistrictName());
        displayDistrictsonSpinner(districtList);

    }

    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
        //editText.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake_error));
    }

    @Override
    public void onSelected() {
        //update UI when selected
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
