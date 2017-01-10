package com.blockvote.votingclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        LinearLayout containerLayout = (LinearLayout) findViewById(R.id.container);

        if (savedInstanceState == null) {

            //Create the Election selection fragment
            SelectElectionFragment selectElectionFragment = new SelectElectionFragment();


            getSupportFragmentManager().beginTransaction().add(R.id.ElectionSelectionContainer, selectElectionFragment).commit();
            Log.d(LOG_TAG, "MainActivity onCreate");
        }else{
            Log.d(LOG_TAG, "savedInstanceState is not null");
        }

    }
}
