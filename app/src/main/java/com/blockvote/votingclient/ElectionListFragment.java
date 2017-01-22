package com.blockvote.votingclient;

import android.content.Context;
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

    private ElectionListFragment.OnFragmentInteractionListener mListener;

    public ElectionListFragment(){

    }
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

                String electionName=mElectionList.getItem(i);

                //Use toast for debugging
                if(electionName.equals("Election Placeholder")){
                    //Toast creates a temporary notification when an item in the ListView is clicked.
                    Context context = adapterView.getContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(context, "Not available!", duration).show();
                    return;
                }

                //have the root activity call the ElectionActivity
                mListener.onElectionOptionClick(electionName);


            }
        });


        Log.d(LOG_TAG,"Election Selection Fragment setup done");

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ElectionListFragment.OnFragmentInteractionListener) {
            mListener = (ElectionListFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onElectionOptionClick(String electionName);
    }

}
