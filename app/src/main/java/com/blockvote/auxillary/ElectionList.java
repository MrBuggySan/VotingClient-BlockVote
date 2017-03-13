package com.blockvote.auxillary;

import java.util.ArrayList;

/**
 * Created by Beast Mode on 3/11/2017.
 */

public class ElectionList {
    private ArrayList<String> electionKeysList;

    public ElectionList(){
        electionKeysList = new ArrayList<>();
    }

    public void addElectionKey(String key){
        electionKeysList.add(key);
    }

}
