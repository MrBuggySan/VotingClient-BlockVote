package com.blockvote.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockvote.interfaces.DefaultInteractions;
import com.blockvote.votingclient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewElectionFragment extends Fragment {
    private DefaultInteractions defaultInteractions;

    public NewElectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_election, container, false);
        defaultInteractions.changeTitleBarName("New Election");
        return rootView;
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
}
