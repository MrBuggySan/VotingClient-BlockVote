package com.blockvote.fragments;


import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockvote.interfaces.DefaultInteractions;
import com.blockvote.votingclient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewElectionFragment extends Fragment {
    private DefaultInteractions defaultInteractions;
    private NewElectionOnClick newElectionOnClick;

    public NewElectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_election, container, false);
        defaultInteractions.changeTitleBarName("New Election");
        TextView textView = (TextView) rootView.findViewById(R.id.New_election_manual_add);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               newElectionOnClick.onManualOptionClick();
            }

        });

        View scanQRCard = rootView.findViewById(R.id.newElection_Card);
        scanQRCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newElectionOnClick.onScanQRCodeClick();
            }
        });


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DefaultInteractions && context instanceof NewElectionOnClick) {
            defaultInteractions = (DefaultInteractions) context;
            newElectionOnClick = (NewElectionOnClick) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DefaultInteractions & NewElectionOnClick ");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        defaultInteractions = null;
    }

    public interface NewElectionOnClick {
        void onScanQRCodeClick();
        void onManualOptionClick();
    }
}
