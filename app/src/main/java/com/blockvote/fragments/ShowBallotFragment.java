package com.blockvote.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockvote.interfaces.DefaultInteractions;
import com.blockvote.votingclient.R;

public class ShowBallotFragment extends Fragment {

    private View rootView;
    private DefaultInteractions defaultInteractions;


    public ShowBallotFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DefaultInteractions) {
            defaultInteractions = (DefaultInteractions) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DefaultInteractions");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        defaultInteractions = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_select_candidate, container, false);
        rootView.findViewById(R.id.select_candidate_UI).setVisibility(View.GONE);
        getBallot();
        return rootView;
    }

    public void getBallot(){
        //TODO: get the ballot of the voter

        displayBallot();
    }

    public void displayBallot(){

    }



}
