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

import java.util.ArrayList;


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

    private String[] optionList;

    private OnFragmentInteractionListener mListener;

    private static final String ARG_PARAM1 = "param1";

    public SelectCandidateFragment() {
        // Required empty public constructor
    }

    public static SelectCandidateFragment newInstance(String[] optionList) {
        SelectCandidateFragment fragment = new SelectCandidateFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1,optionList);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            optionList = getArguments().getStringArray(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_select_candidate, container, false);
        mCandidateList = new ArrayAdapter<String>(
                getActivity(),
                R.layout.listentry,
                R.id.listEntry,
                new ArrayList<String>()
        );
        for(int i = 0 ; i < optionList.length ; i++){
            mCandidateList.add(optionList[i]);
        }
        ListView listView=(ListView) rootView.findViewById(R.id.listview_candidatelist);
        listView.setAdapter(mCandidateList);
        //setup the button event when a candidate is pressed
        //Add the event to call BallotConfirmationFragment when candidate is clicked
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                String choice=mCandidateList.getItem(i);


                //TODO: get the timestamp

                //TODO: have the root activity call the BallotConfirmationFragment
                mListener.onOptionSelectInteraction(choice, "test hour");


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
