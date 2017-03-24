package com.blockvote.auxillary;

import java.util.ArrayList;

/**
 * Created by Beast Mode on 3/13/2017.
 */

public class ElectionList {

    protected ArrayList<ElectionInstance> electionList;

    public boolean addElection(ElectionInstance electionInstance){
        if(!HACKVERSION.forDemo && !electionList.isEmpty() && this.hasElection(electionInstance)){
            return false;
        }
        //try to add the election
        electionList.add(electionInstance);
        return true;
    }

    public boolean hasElection(ElectionInstance electionInstance){
        if(HACKVERSION.forDemo) return false;
        String newURL = electionInstance.getElectionURL();
        for(ElectionInstance x : electionList){
            if(newURL.equals(x.getElectionURL())){
                return true;
            }
        }
        return false;
    }

    public void updateElection(ElectionInstance electionInstance){

            int electionID = electionInstance.getId();
            for(int i = 0; i < getSize(); i++){
                if(electionList.get(i).getId() == electionID){
                    electionList.set(i, electionInstance);
                    break;
                }
            }
    }

    public int getSize(){
        return electionList.size();
    }

    public ElectionInstance getElectionAt(int i){
        return electionList.get(i);
    }

    public ElectionInstance getElectionWithID(int id){
        for(int i = 0; i < getSize(); i++){
            if(electionList.get(i).getId() == id){
                return electionList.get(i);
            }
        }
        return null;
    }

    public void deleteAllElections(){
        electionList.clear();
    }
}
