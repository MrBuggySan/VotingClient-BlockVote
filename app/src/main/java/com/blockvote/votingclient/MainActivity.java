package com.blockvote.votingclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.blockvote.auxillary.ElectionState;
import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.fragments.FinishedElections;
import com.blockvote.fragments.OnGoingElections;
import com.blockvote.interfaces.OnCardInterActionActivityLevel;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class MainActivity extends AppCompatActivity implements OnCardInterActionActivityLevel {



    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("BlockVote");
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Main_Option_Help:
                //TODO: Start the HelpActivity
                intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                return true;
            case R.id.Main_Option_About:
                //TODO: Start the HelpActivity
                return true;
            case R.id.Main_Option_Credits:
                intent = new Intent(this, CreditsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OnGoingElections(), "Ongoing");
        adapter.addFragment(new FinishedElections(), "Completed");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onNewElectionCardPress(){
        Intent intent = new Intent(this, VotingActivity.class);
        intent.putExtra(getString(R.string.newelectionKey), true);
        intent.setFlags(FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    public void onElectionCardPress(ElectionState electionState, int id){
        //check if this is an onGoingElection or finishedElection

        if(electionState == ElectionState.START_GEN_QR){
            //Call RegistrationActivity
            Intent intent = new Intent(this, RegistrationActivity.class);
            intent.putExtra(getString(R.string.newelectionKey), false);
            intent.putExtra(getString(R.string.electionIDKey), id);
            intent.setFlags(FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }else{
            if (electionState == ElectionState.PRE_VOTING){
                //Call VotingActivity
                Intent intent = new Intent(this, VotingActivity.class);
                intent.putExtra(getString(R.string.newelectionKey), false);
                intent.putExtra(getString(R.string.electionIDKey), id);
                intent.setFlags(FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return;
            }
            if(electionState == ElectionState.POST_VOTING){
                Intent intent = new Intent(this, PostVotingActivity.class);
                intent.putExtra(getString(R.string.electionIDKey), id);
                intent.setFlags(FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return;
            }
            //We should never get here
            ToastWrapper.initiateToast(this, "The election selected has undefined state. ERROR");


        }
    }
}
