package com.blockvote.auxillary;

import java.util.ArrayList;

/**
 * Created by Beast Mode on 3/13/2017.
 */

public class ElectionList {

    protected ArrayList<ElectionInstance> electionList;

    public boolean addElection(ElectionInstance electionInstance){
        //This check can be taken out for the demos so we can have the same instances
        if(!HACKVERSION.forDemo && !electionList.isEmpty() && this.hasElection(electionInstance)){
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

    public void updateElection(ElectionInstance electionInstance){
        if(HACKVERSION.forDemo){
            int electionID = electionInstance.getId();
            for(int i = 0; i < getSize(); i++){
                if(electionList.get(i).getId() == electionID){
                    electionList.set(i, electionInstance);
                    break;
                }
            }
        }else{
            String newURL = electionInstance.getElectionURL();
            for(int i = 0; i < getSize(); i++){
                if(electionList.get(i).getElectionURL().equals(newURL)){
                    electionList.set(i, electionInstance);
                    break;
                }
            }
        }


    }

    public int getSize(){
        return electionList.size();
    }

    public ElectionInstance getElectionAt(int i){
        return electionList.get(i);
    }

    public void deleteAllElections(){
        electionList.clear();
    }
}
