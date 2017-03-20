package com.blockvote.auxillary;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.blockvote.votingclient.R;
import com.google.gson.Gson;

/**
 * Created by Beast Mode on 3/15/2017.
 */

public class DataStore {
    private static final String LOG_TAG = DataStore.class.getSimpleName();

    public static FinishedElectionList getFinishedElectionList(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.globalSharedPrefKey), Context.MODE_PRIVATE);
        String strFinElections = sharedPref.getString(context.getString(R.string.finishedElectionsListKey), null);
        if(strFinElections == null) return null;
        Gson gson = new Gson();
        FinishedElectionList finishedElectionList = gson.fromJson(strFinElections,FinishedElectionList.class);
        return finishedElectionList;
    }

    public static void saveFinishedElectionList(Context context, FinishedElectionList finishedElectionList){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.globalSharedPrefKey), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String strFinishedElectionList = gson.toJson(finishedElectionList);
        editor.putString(context.getString(R.string.finishedElectionsListKey), strFinishedElectionList);
        editor.commit();
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

    public static void saveOngoingElectionList(Context context, OngoingElectionList ongoingElectionList){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.globalSharedPrefKey), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();

        String strOnGoingElections = gson.toJson(ongoingElectionList);

        editor.putString(context.getString(R.string.onGoingElectionsListKey), strOnGoingElections);
        editor.commit();
    }

    //This is a band aid solution
    public static void saveURLandRegistrarFromQR(Context context, String electionURL, String registrarName){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.globalSharedPrefKey), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.electionURLKey), electionURL);
        editor.putString(context.getString(R.string.regigstrarNameKey), registrarName);
        editor.commit();
    }

    public static String[] getURLandRegistrarFromQR(Context context){
        String[] data = new String[2];
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.globalSharedPrefKey), Context.MODE_PRIVATE);
        data[0] = sharedPref.getString(context.getString(R.string.electionURLKey), null);
        data[1] = sharedPref.getString(context.getString(R.string.regigstrarNameKey), null);
        return data;
    }
}
