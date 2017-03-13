package com.blockvote.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.RVAdapter;
import com.blockvote.votingclient.R;

import java.util.ArrayList;

/**
 * Created by Beast Mode on 3/11/2017.
 */

public class FinishedElections extends ElectionListFragment {
    public void EditUI(){
        //Fill some random data, for real this would come from the dataStore
        ElectionInstance sample1 = new ElectionInstance();
        sample1.setElectionName("Sample Election 4");
        sample1.setTimeString("Results available now");
        ElectionInstance sample2 = new ElectionInstance();
        sample2.setElectionName("Sample Election5");
        sample2.setTimeString("No results avaiable yet");

        ArrayList<ElectionInstance> elections = new ArrayList<>();

        elections.add(sample1);
        elections.add(sample2);

        RecyclerView rv = (RecyclerView)rootView.findViewById(R.id.electionList_recyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        RVAdapter adapter = new RVAdapter(elections, false);
        rv.setAdapter(adapter);
    }
}
