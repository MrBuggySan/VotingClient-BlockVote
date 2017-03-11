package com.blockvote.fragments;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.blockvote.interfaces.DefaultInteractions;
import com.blockvote.votingclient.R;
import com.blockvote.votingclient.RegistrationActivity;
import com.stepstone.stepper.VerificationError;

public class ManualForm extends RegistrationFormFragment {
    private RegistrationActivity registrationActivity;

    @Override
    public void EditUI(DefaultInteractions defaultInteractions){

        //TODO: Display the data from the downloads

        TextView blurb1 = (TextView) rootView.findViewById(R.id.regform_blurb1);
        blurb1.setText("Please fill in the URL and the required fields for the election.");

        //TODO: Edit the title name
        defaultInteractions.changeTitleBarName("New Election");
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
