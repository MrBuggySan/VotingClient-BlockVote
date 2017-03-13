package com.blockvote.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.RVAdapter;
import com.blockvote.votingclient.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class ElectionListFragment extends Fragment {
    protected View rootView;


    public ElectionListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_election_list, container, false);


        //Fill some random data, for real this would come from the dataStore
        ElectionInstance sample1 = new ElectionInstance();
        sample1.setElectionName("Sample 1");
        sample1.setElectionURL("sample1.com");
        ElectionInstance sample2 = new ElectionInstance();
        sample2.setElectionName("Sample 2");
        sample2.setElectionURL("sample2.com");

        ArrayList<ElectionInstance> elections = new ArrayList<>();

        elections.add(sample1);
        elections.add(sample2);

        elections.add(sample1);
        elections.add(sample2);


        RecyclerView rv = (RecyclerView)rootView.findViewById(R.id.electionList_recyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        RVAdapter adapter = new RVAdapter(elections);
        rv.setAdapter(adapter);


        return rootView;
    }

}
