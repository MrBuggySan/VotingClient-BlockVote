package com.blockvote.votingclient;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.blockvote.auxillary.StepperAdapter;
import com.blockvote.fragments.ManualForm;
import com.blockvote.interfaces.DefaultInteractions;
import com.stepstone.stepper.StepperLayout;

public class RegistrationActivity extends AppCompatActivity implements DefaultInteractions {

    private StepperLayout mStepperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //TODO: determine if I have to use FilledForm or ManualForm for this Election

        //TODO: save the selected RegistrationFormFragment inside datastore

        Toolbar myToolbar = (Toolbar) findViewById(R.id.registration_toolbar);
        setSupportActionBar(myToolbar);

        ManualForm manualForm= new ManualForm();

        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this, manualForm));

    }

    @Override
    public void changeTitleBarName(String name){
        ActionBar myActionBar = (ActionBar) getSupportActionBar();
        myActionBar.setTitle(name);
    }
}
