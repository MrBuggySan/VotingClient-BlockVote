package com.blockvote.votingclient;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BallotConfirmationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BallotConfirmationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BallotConfirmationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private String firstName;
    private String lastName;
    private String choice;
    private String timestamp;

    private OnFragmentInteractionListener mListener;

    public BallotConfirmationFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance.
     * @param firstName
     * @param lastName
     * @param choice
     * @param timestamp
     * @return
     */
    public static BallotConfirmationFragment newInstance(String firstName, String lastName, String choice, String timestamp) {
        BallotConfirmationFragment fragment = new BallotConfirmationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, firstName);
        args.putString(ARG_PARAM2, lastName);
        args.putString(ARG_PARAM3, choice);
        args.putString(ARG_PARAM4, timestamp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            firstName = getArguments().getString(ARG_PARAM1);
            lastName = getArguments().getString(ARG_PARAM2);
            choice = getArguments().getString(ARG_PARAM3);
            timestamp = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ballot_confirmation, container, false);
        //TODO: modify the texts to reflect the data put in by the voter.

        //TODO: when YES, start the networking code to submit the data to the server
        //TODO: Plan for possible delays from the server side when it is submitting data to the chaincode

        //TODO: when YES, submit the data to the server. A response of success or error must be met

        //TODO: when NO, go back to the SelectCandidateFragment

        // Inflate the layout for this fragment
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
