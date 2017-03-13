package com.blockvote.auxillary;

import java.util.ArrayList;

/**
 * Created by Beast Mode on 3/13/2017.
 */

public abstract class ElectionList {

    protected ArrayList<ElectionInstance> electionList;

    public boolean addElection(ElectionInstance electionInstance){
        //This check can be taken out for the demos so we can have the same instances
        if(this.hasElection(electionInstance)){
            return false;
        }
        electionList.add(electionInstance);
        return true;
    }

    private boolean hasElection(ElectionInstance electionInstance){
        String newURL = electionInstance.getElectionURL();
        for(ElectionInstance x : electionList){
            if(newURL.equals(x.getElectionURL())){
                return true;
            }
        }
        return false;
    }

    public void deleteAllElections(){
        electionList.clear();
    }
}
