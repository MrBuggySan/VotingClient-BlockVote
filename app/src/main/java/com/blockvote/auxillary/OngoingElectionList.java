package com.blockvote.auxillary;

import java.util.ArrayList;

/**
 * Created by Beast Mode on 3/11/2017.
 */

public class OngoingElectionList extends ElectionList{
    public OngoingElectionList(){
        electionList = new ArrayList<>();
    }

    public void finishedElection(ElectionInstance electionInstance, FinishedElectionList finishedElectionList){
        //TODO: design this so that it safely transfers the correct electionInstance to finishedElectionList
    }
}
