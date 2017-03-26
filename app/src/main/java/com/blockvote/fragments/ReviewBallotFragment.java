package com.blockvote.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.interfaces.DefaultInteractions;
import com.blockvote.votingclient.R;


public class ReviewBallotFragment extends Fragment {

    private OnReviewPress onReviewPress;
    private DefaultInteractions defaultInteractions;

    public ReviewBallotFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private final String LOG_TAG = ReviewBallotFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_review_ballot, container, false);

        ElectionInstance electionInstance = defaultInteractions.getElectionInstance();

        View reviewBallotButton =  rootView.findViewById(R.id.ReviewBallotCard);
        reviewBallotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "opening ShowBallotFragment");
                onReviewPress.onReviewBallotButtonPress();
            }
        });

        if(electionInstance.liveResultsAvailable()){
            TextView textView = (TextView) rootView.findViewById(R.id.ReviewBallotblurb);
            textView.setText("Thank you for voting. You can now review your ballot and view the " +
                    "results of the election");

            View showResultsButton =  rootView.findViewById(R.id.ViewResultsCard);
            showResultsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onReviewPress.onReviewResultsButtonPress();
                }
            });
        }else{
            rootView.findViewById(R.id.ViewResultsCard).setVisibility(View.GONE);
            TextView textView = (TextView) rootView.findViewById(R.id.ReviewBallotblurb);
            textView.setText("Thank you for voting. You can now review your ballot. The results " +
                    "of the election will be available soon.");
        }

        return rootView;
    }

//     void EditUi();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnReviewPress &&
                context instanceof DefaultInteractions) {
            onReviewPress = (OnReviewPress) context;
            defaultInteractions = (DefaultInteractions) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnReviewPress & DefaultInteractions");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onReviewPress = null;
        defaultInteractions = null;
    }

    public interface OnReviewPress {
        void onReviewBallotButtonPress();
        void onReviewResultsButtonPress();
    }
}
