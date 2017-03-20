package com.blockvote.fragments;

import android.support.annotation.NonNull;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.interfaces.RegistrationDefaultInteractions;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

public class FilledForm extends RegistrationFormFragment {

    @Override
    public void EditUI(RegistrationDefaultInteractions registrationDefaultInteractions){
        //TODO: take away the URL text and edittext
        //TODO: show the option to scan the QR again



        //TODO: validate the URL
        //TODO: validate the registrar
        //TODO: display the name of the Election

    }

    @Override
    public void stopLoadingAnimOnSuccess(){
        //There is no need to do this for FilledForm
    }

    @Override
    public void stopLoadingAnimOnFail(){
        //There is no need to do this for FilledForm
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
