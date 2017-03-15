package com.blockvote.auxillary;

import android.content.Context;
import android.content.SharedPreferences;

import com.blockvote.votingclient.R;
import com.google.gson.Gson;

/**
 * Created by Beast Mode on 3/15/2017.
 */

public class DataStore {
    public static FinishedElectionList getFinishedElectionList(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.globalSharedPrefKey), Context.MODE_PRIVATE);
        String strFinElections = sharedPref.getString(context.getString(R.string.finishedElectionsListKey), null);
        if(strFinElections == null) return null;
        Gson gson = new Gson();
        FinishedElectionList finishedElectionList = gson.fromJson(strFinElections,FinishedElectionList.class);
        return finishedElectionList;
    }

    public static OngoingElectionList getOngoingElectionList(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.globalSharedPrefKey), Context.MODE_PRIVATE);
        String strOnGoingElections = sharedPref.getString(context.getString(R.string.onGoingElectionsListKey), null);
        if(strOnGoingElections == null) return null;
        Gson gson = new Gson();
        OngoingElectionList ongoingElectionList = gson.fromJson(strOnGoingElections,OngoingElectionList.class);
        return ongoingElectionList;
    }
}
