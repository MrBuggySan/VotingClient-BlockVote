package com.blockvote.fragments;

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

import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.model.MODEL_ElectionInfo;
import com.blockvote.networking.BlockVoteServerAPI;
import com.blockvote.networking.BlockVoteServerInstance;
import com.blockvote.votingclient.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectCandidateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectCandidateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectCandidateFragment extends Fragment {
    private final String LOG_TAG = SelectCandidateFragment.class.getSimpleName();
    ArrayAdapter<String> mCandidateList;

    private OnFragmentInteractionListener mListener;

    private static final String ARG_PARAM1 = "param1";

    public SelectCandidateFragment() {
        // Required empty public constructor
    }

    public static SelectCandidateFragment newInstance() {
        SelectCandidateFragment fragment = new SelectCandidateFragment();


        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_select_candidate, container, false);

        //get the options from the server
        BlockVoteServerInstance blockVoteServerInstance = new BlockVoteServerInstance();
        BlockVoteServerAPI apiService = blockVoteServerInstance.getAPI();
        Call<MODEL_ElectionInfo> call = apiService.getElectionInfo();

        call.enqueue(new Callback<MODEL_ElectionInfo>() {
            @Override
            public void onResponse(Call<MODEL_ElectionInfo> call, Response<MODEL_ElectionInfo> response) {
                int statusCode = response.code();

                mCandidateList = new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.listentry,
                        R.id.listEntry,
                        new ArrayList<String>()
                );

                List<String> electionOptions = response.body().getResponse().getElectionData().getVoteOptions();
                mCandidateList.add(electionOptions.get(2));
                mCandidateList.add(electionOptions.get(1));
                mCandidateList.add(electionOptions.get(0));

                ListView listView=(ListView) getView().findViewById(R.id.listview_candidatelist);
                listView.setAdapter(mCandidateList);
                //setup the button event when a candidate is pressed
                //Add the event to call BallotConfirmationFragment when candidate is clicked
                listView.setOnItemClickListener( new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                        String choice=mCandidateList.getItem(i);
                        //TODO: get the timestamp
                        mListener.onOptionSelectInteraction(choice, "test hour");
                    }
                });
            }

            @Override
            public void onFailure(Call<MODEL_ElectionInfo> call, Throwable t) {
                Log.e(LOG_TAG, "Downloading th" +
                        "e election options has failed...");
                ToastWrapper.initiateToast(getContext(), "Downloading the election options has failed...");
                //TODO:Restart the connection if failure
            }
        });





        Log.d(LOG_TAG, "Fragment setup done");

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onOptionSelectInteraction(String choice, String timestamp);
    }
}
