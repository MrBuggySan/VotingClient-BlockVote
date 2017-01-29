package com.blockvote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.blockvote.fragments.ElectionListFragment;
import com.blockvote.fragments.R;

public class RootActivity extends AppCompatActivity
    implements ElectionListFragment.OnFragmentInteractionListener {
    private final String LOG_TAG = RootActivity.class.getSimpleName();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_root_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Option_rootHelp:
                //TODO: Start the HelpActivity
                Log.d(LOG_TAG, "Help option selected");
                return true;
            case R.id.Option_BeamTest:
                //Start the HelpActivity
                Log.d(LOG_TAG, "Beam Test selected");
                Intent intent = new Intent(this, BeamTestActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        //Note: AppCompatActivity enables the use of Toolbar. Actionbar is depracated.
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

    public void onElectionOptionClick(String electionName){
        //Starting the ElectionActivity
        Intent intent = new Intent(this, ElectionActivity.class);
        //Give the name of the election Selected
        //TODO: Pass an election object instead of a String, well I can use this String as a key to the DB
        intent.putExtra(getString(R.string.selectedElectionKey), electionName);
        startActivity(intent);
    }
}
