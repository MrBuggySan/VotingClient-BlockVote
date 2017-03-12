package com.blockvote.fragments;

import android.support.annotation.NonNull;

import com.blockvote.interfaces.RegistrationDefaultInteractions;
import com.stepstone.stepper.VerificationError;

public class FilledForm extends RegistrationFormFragment {

    @Override
    public void EditUI(RegistrationDefaultInteractions registrationDefaultInteractions){

        //TODO: verify that the information on the QR is valid against what we have

        //TODO: display the name of the Election

        //TODO: take away the URL text and edittext

    }

    @Override
    public void stopLoadingAnimOnSuccess(){

    }

    @Override
    public void stopLoadingAnimOnFail(){

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
