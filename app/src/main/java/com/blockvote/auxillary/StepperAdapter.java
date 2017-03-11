package com.blockvote.auxillary;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.blockvote.fragments.GenerateQRFragment;
import com.blockvote.fragments.RegistrationFormFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * Created by andreibuiza on 06/03/17.
 */

public class StepperAdapter extends AbstractFragmentStepAdapter {

    private RegistrationFormFragment regisForm;
    public StepperAdapter(FragmentManager fm, Context context, RegistrationFormFragment regisForm) {
        super(fm, context);
        this.regisForm = regisForm;
    }

    @Override
    public Step createStep(int position) {
        switch (position) {
            case 0:
                return regisForm;
            case 1:
                return new GenerateQRFragment();
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {

        switch (position) {
            case 0:
                return new StepViewModel.Builder(context)
                        .setTitle("Registration Form")
                        .create();
            case 1:
                return new StepViewModel.Builder(context)
                        .setTitle("QR Code")
                        .create();
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }
    }

}
