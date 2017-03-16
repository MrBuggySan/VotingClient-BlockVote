package com.blockvote.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.blockvote.auxillary.DataStore;
import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.OngoingElectionList;
import com.blockvote.auxillary.RVAdapter;
import com.blockvote.votingclient.R;

/**
 * Created by Beast Mode on 3/15/2017.
 */

public class OnGoingElections extends ElectionListFragment {
    private static String LOG_TAG = ElectionListFragment.class.getSimpleName();
    private OngoingElectionList ongoingElectionList;
    public void EditUI(){
        Log.d(LOG_TAG, "EditUI called");
        ongoingElectionList = DataStore.getOngoingElectionList(getContext());

        if(ongoingElectionList == null){
            ongoingElectionList = new OngoingElectionList();
            DataStore.saveOngoingElectionList(getContext(), ongoingElectionList);
        }

        RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.electionList_recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        RVAdapter adapter = new RVAdapter(getContext(), this, ongoingElectionList, true);
        recyclerView.setAdapter(adapter);

//        CardView newElectionCard = (CardView) rootView.findViewById(R.id.newElection_Card);
    }

    @Override
    public void onElectionCardPress(int position){
        //Pass off the electionInstance that was selected
        ElectionInstance electionInstance = ongoingElectionList.getElectionAt(position);
        Log.d(LOG_TAG, electionInstance.getElectionName() + " selected with state " + electionInstance.getElectionState());
        onCardInterActionActivityLevel.onElectionCardPress(ongoingElectionList.getElectionAt(position).getElectionState(), position);
    }
}
