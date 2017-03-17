package com.blockvote.fragments;

import android.view.View;
import android.widget.TextView;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.votingclient.R;

/**
 * Created by Beast Mode on 3/16/2017.
 */

public class VoteLater extends VoteNowOrLaterFragment{

    @Override
    public void EditUi(){
        rootView.findViewById(R.id.VoteNow).setVisibility(View.GONE);
        TextView textViewblurb = (TextView) rootView.findViewById(R.id.voteNowOrLater_blurb1);
        TextView timeRemainingBlurb = (TextView) rootView.findViewById(R.id.voteNowOrLater_blurb2);
        ElectionInstance electionInstance = defaultInteractions.getElectionInstance();
        textViewblurb.setText("You are now elgible to vote in " + electionInstance.getElectionName() + ".");
        timeRemainingBlurb.setText(electionInstance.getTimeString());
    }
}
