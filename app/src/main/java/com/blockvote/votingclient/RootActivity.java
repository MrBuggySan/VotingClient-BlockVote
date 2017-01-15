package com.blockvote.votingclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;

public class RootActivity extends AppCompatActivity {
    private final String LOG_TAG = RootActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        LinearLayout containerLayout = (LinearLayout) findViewById(R.id.container);

        if (savedInstanceState == null) {

            //Create the Election selection fragment
            ElectionListFragment selectElectionFragment = new ElectionListFragment();


            getSupportFragmentManager().beginTransaction().add(R.id.ElectionSelectionContainer, selectElectionFragment).commit();
            Log.d(LOG_TAG, "RootActivity onCreate");
        }else{
            Log.d(LOG_TAG, "savedInstanceState is not null");
        }

    }
}
