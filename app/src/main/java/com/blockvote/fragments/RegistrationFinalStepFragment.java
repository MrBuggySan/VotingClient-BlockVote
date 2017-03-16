package com.blockvote.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockvote.votingclient.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFinalStepFragment extends Fragment implements Step {

    private FinalStepQRCode finalStepQRCode;

    public RegistrationFinalStepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_registration_final, container, false);
        View scanQRCard = rootView.findViewById(R.id.finalreg_scanqr);
        scanQRCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalStepQRCode.onScanQRCodeClickFromFinalStep();
            }
        });
        return rootView;
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
        //editText.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake_error));
    }

    @Override
    public void onSelected() {

    }

    @Override
    public VerificationError verifyStep() {

        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FinalStepQRCode) {
            finalStepQRCode = (FinalStepQRCode) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FinalStepQRCode");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        finalStepQRCode = null;
    }

    public interface FinalStepQRCode {
        void onScanQRCodeClickFromFinalStep();
    }

}
