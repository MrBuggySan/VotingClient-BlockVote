package com.blockvote.interfaces;

import com.blockvote.auxillary.ElectionState;

/**
 * Created by Beast Mode on 3/15/2017.
 */

public interface OnCardInterActionActivityLevel {
    void onNewElectionCardPress();
    void onElectionCardPress(ElectionState electionInstance, int indext);
}
