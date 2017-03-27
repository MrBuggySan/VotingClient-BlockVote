package com.blockvote.fragments;

import android.widget.TextView;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.votingclient.R;

/**
 * Created by Beast Mode on 3/16/2017.
 */

public class VoteNow extends VoteNowOrLaterFragment {

    @Override
    public void EditUi(){
        TextView textViewblurb = (TextView) rootView.findViewById(R.id.voteNowOrLater_blurb1);
        TextView timeRemainingBlurb = (TextView) rootView.findViewById(R.id.voteNowOrLater_blurb2);
        ElectionInstance electionInstance = defaultInteractions.getElectionInstance();
        textViewblurb.setText("You are now elgible to vote in " + electionInstance.getElectionNameRaw() + ". " +
                "The election is now open.");
        timeRemainingBlurb.setText(electionInstance.getTimeString());
    }
}
