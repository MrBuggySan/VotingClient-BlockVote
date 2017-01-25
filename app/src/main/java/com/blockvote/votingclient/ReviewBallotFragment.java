package com.blockvote.votingclient;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blockvote.auxillary.ToastWrapper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReviewBallotFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReviewBallotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewBallotFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String voterName;
    private OnFragmentInteractionListener mListener;

    public ReviewBallotFragment() {
        // Required empty public constructor
    }


    public static ReviewBallotFragment newInstance(String voterName_) {
        ReviewBallotFragment fragment = new ReviewBallotFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, voterName_);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            voterName = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_review_ballot, container, false);

        TextView textView = (TextView) rootView.findViewById(R.id.review_textblurb);
        textView.setText(voterName + ", you have succesfully voted. You can now view the results of the election and review the ballot you submitted");


        //TODO: setup buttons for viewing the results and reviewing the voter's ballot.
        Button reviewBallotButton = (Button) rootView.findViewById(R.id.Review_show_ballot_button);
        reviewBallotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastWrapper.initiateToast(getContext(), "Showing your ballot for review is still being worked on.");
            }
        });

        Button showResultsButton = (Button) rootView.findViewById(R.id.Review_show_results_button);
        showResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastWrapper.initiateToast(getContext(), "Showing you the results is still being worked on.");
            }
        });


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

    public interface OnFragmentInteractionListener {
        void onReviewBallotButtonPress();
        void onReviewResultsButtonPress();
    }
}
