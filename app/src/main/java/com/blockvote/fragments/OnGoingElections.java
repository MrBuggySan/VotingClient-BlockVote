package com.blockvote.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.RVAdapter;
import com.blockvote.votingclient.R;

import java.util.ArrayList;

/**
 * Created by Beast Mode on 3/11/2017.
 */

public class OnGoingElections extends ElectionListFragment {
    public void EditUI(){
//Fill some random data, for real this would come from the dataStore
        ElectionInstance sample1 = new ElectionInstance();
        sample1.setElectionName("Sample Election 1");
        sample1.setTimeString("Opens in 2 hours and 3 minutes");
        ElectionInstance sample2 = new ElectionInstance();
        sample2.setElectionName("Sample Election 2");
        sample2.setTimeString("Voting ends in 25 minutes");

        ArrayList<ElectionInstance> elections = new ArrayList<>();

        elections.add(sample1);
        elections.add(sample2);

        RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.electionList_recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        RVAdapter adapter = new RVAdapter(onCardInteraction, elections, true);
        recyclerView.setAdapter(adapter);

//        CardView newElectionCard = (CardView) rootView.findViewById(R.id.newElection_Card);
    }
}
