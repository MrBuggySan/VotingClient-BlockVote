package com.blockvote.votingclient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

    private OnFragmentInteractionListener mListener;

    public SelectCandidateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SelectCandidateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectCandidateFragment newInstance() {
        SelectCandidateFragment fragment = new SelectCandidateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_select_candidate, container, false);

        //TODO: get the list of candidates from the server

        //a hardcoded list of selections (this is only for testing purposes)
        mCandidateList = new ArrayAdapter<String>(
                getActivity(),
                R.layout.listentry,
                R.id.listEntry,
                new ArrayList<String>()
        );
        mCandidateList.add("Leave the European Union");
        mCandidateList.add("Remain a member of the European Union");
        ListView listView=(ListView) rootView.findViewById(R.id.listview_candidatelist);
        listView.setAdapter(mCandidateList);
        //TODO: setup the button event when a candidate is pressed
        //Add the event to call BallotConfirmationFragment when candidate is clicked
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                String choice=mCandidateList.getItem(i);
                //TODO: ask the name of the voter (this is only for testing purposes)
                View rootView = getView();
                EditText firstText= (EditText) rootView.findViewById(R.id.firstNameText);
                EditText lastText= (EditText) rootView.findViewById(R.id.lastNameText);

                String firstName = firstText.getText().toString();
                String lastName = lastText.getText().toString();

                //TODO: get the timestamp

                //TODO: have the root activity call the BallotConfirmationFragment
                mListener.onOptionSelectInteraction(firstName, lastName, choice, "test hour");


            }
        });

        //TODO: get the name of the user from the system, have an option to edit the values






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
        void onOptionSelectInteraction(String firstName, String lastName, String choice, String timestamp);
    }
}
