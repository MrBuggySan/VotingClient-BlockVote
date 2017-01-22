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

import com.blockvote.model.ElectionListModel;
import com.blockvote.networking.BlockVoteServerAPI;
import com.blockvote.networking.BlockVoteServerInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        final View rootView = inflater.inflate(R.layout.fragment_election_list, container, false);

        //disable the electionlist
        rootView.findViewById(R.id.listview_electionlist).setVisibility(View.GONE);

        //TODO: the fragment must redownload the list even if its coming from the backstack.

        //get the election list from the server
        BlockVoteServerInstance blockVoteServerInstance = new BlockVoteServerInstance();
        BlockVoteServerAPI apiService = blockVoteServerInstance.getAPI();
        Call<ElectionListModel> call = apiService.getElectionList();

        call.enqueue(new Callback<ElectionListModel>() {
            @Override
            public void onResponse(Call<ElectionListModel> call, Response<ElectionListModel> response) {
                int statusCode = response.code();
                List<String> electionList = response.body().getResponse();


                mElectionList = new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.listentry,
                        R.id.listEntry,
                        new ArrayList<String>()
                );

                for(int i = 0; i < electionList.size(); i++){
                    Log.d(LOG_TAG, electionList.get(i) + " available.");
                    mElectionList.add(electionList.get(i));
                }
                //TODO: apply the results to the UI
                View rootView_ = getView();
                ListView listView=(ListView) rootView_.findViewById(R.id.listview_electionlist);
                listView.setAdapter(mElectionList);

                //disable the loading screen
                rootView_.findViewById(R.id.electionlist_loadingPanel).setVisibility(View.GONE);

                //Add the event when election is clicked
                listView.setOnItemClickListener( new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                        String electionName=mElectionList.getItem(i);
                        //have the root activity call the ElectionActivity
                        mListener.onElectionOptionClick(electionName);
                    }
                });

                //Show the election list
                rootView.findViewById(R.id.listview_electionlist).setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ElectionListModel> call, Throwable t) {
                Log.e(LOG_TAG,"Downloading the election list has failed...");
                throw new RuntimeException("Could not download the election list");
                //TODO:Restart the connection
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
