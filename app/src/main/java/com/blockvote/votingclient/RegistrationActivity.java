package com.blockvote.votingclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.blockvote.auxillary.StepperAdapter;
import com.blockvote.fragments.ManualForm;
import com.stepstone.stepper.StepperLayout;

public class RegistrationActivity extends AppCompatActivity {

    private StepperLayout mStepperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //TODO: determine if I have to use FilledForm or ManualForm for this Election

        //TODO: save the selected RegistrationFormFragment inside datastore

        ManualForm manualForm= new ManualForm();

        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this, manualForm));

    }
}
