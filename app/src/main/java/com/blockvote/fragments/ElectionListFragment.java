package com.blockvote.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockvote.interfaces.OnCardInteraction;
import com.blockvote.votingclient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class ElectionListFragment extends Fragment  {
    protected View rootView;
    protected OnCardInteraction onCardInteraction;

    public ElectionListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_election_list, container, false);

        EditUI();
        return rootView;
    }

    abstract void EditUI();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCardInteraction) {
            onCardInteraction = (OnCardInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCardInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onCardInteraction = null;
    }

}
