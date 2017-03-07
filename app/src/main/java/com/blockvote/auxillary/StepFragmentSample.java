package com.blockvote.auxillary;

import android.content.Context;
import android.net.Uri;
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
 * Activities that contain this fragment must implement the
 * {@link StepFragmentSample.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepFragmentSample#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepFragmentSample extends Fragment implements Step {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_step_fragment_sample, container, false);

        //initialize your UI

        return v;
    }

    @Override
    public VerificationError verifyStep() {
        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        return null;
    }

    @Override
    public void onSelected() {
        //update UI when selected
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
    }
}
