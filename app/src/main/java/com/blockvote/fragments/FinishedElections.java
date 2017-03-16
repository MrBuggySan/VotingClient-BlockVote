package com.blockvote.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blockvote.auxillary.DataStore;
import com.blockvote.auxillary.FinishedElectionList;
import com.blockvote.auxillary.RVAdapter;
import com.blockvote.votingclient.R;

/**
 * Created by Beast Mode on 3/11/2017.
 */

public class FinishedElections extends ElectionListFragment {
    private FinishedElectionList finishedElectionList;
    public void EditUI(){
        //TODO: handle the refreshing since we might come back from...

        finishedElectionList = DataStore.getFinishedElectionList(getContext());

        if(finishedElectionList == null){
            finishedElectionList = new FinishedElectionList();
            DataStore.saveFinishedElectionList(getContext(), finishedElectionList);
        }
        RecyclerView rv = (RecyclerView)rootView.findViewById(R.id.electionList_recyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        RVAdapter adapter = new RVAdapter(getContext(), this, finishedElectionList, false);
        rv.setAdapter(adapter);
    }

    @Override
    public void onElectionCardPress(int position){
        //Pass off the electionInstance that was selected
        onCardInterActionActivityLevel.onElectionCardPress(finishedElectionList.getElectionAt(position).getElectionState(), position);
    }
}
