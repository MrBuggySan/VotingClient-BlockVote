package com.blockvote.votingclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Beast Mode on 12/26/2016.
 */

public class ElectionListFragment extends Fragment {
    private final String LOG_TAG= ElectionListFragment.class.getSimpleName();
    private ArrayAdapter<String> mElectionList;

    @Override
    //Draw the UI elements on the screen
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_election_list, container, false);

        mElectionList = new ArrayAdapter<String>(
                getActivity(),
                R.layout.listentry,
                R.id.listEntry,
                new ArrayList<String>()
        );

        //Rough list of available elections
        //TODO: get the election list from the server
        mElectionList.add(getString(R.string.electionEntry1));
        for(int i = 0; i < 10; i++){
            mElectionList.add("Election Placeholder");
        }

        ListView listView=(ListView) rootView.findViewById(R.id.listview_electionlist);
        listView.setAdapter(mElectionList);

        //Add the event when election is clicked
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){

                String text=mElectionList.getItem(i);

                //Use toast for debugging
                if(text.equals("Election Placeholder")){
                    //Toast creates a temporary notification when an item in the ListView is clicked.
                    Context context = adapterView.getContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(context, "Not available!", duration).show();
                    return;
                }

                //TODO: have the root activity call the ElectionActivity

                //Starting the ElectionActivity
                Intent intent = new Intent(adapterView.getContext(), ElectionActivity.class);
                //Give the name of the election Selected
                //TODO: Pass an election object instead of a String, well I can use this String as a key to the DB
                intent.putExtra(getString(R.string.selectedElectionKey), text);
                startActivity(intent);

            }
        });


        Log.d(LOG_TAG,"Election Selection Fragment setup done");

        return rootView;
    }

}
