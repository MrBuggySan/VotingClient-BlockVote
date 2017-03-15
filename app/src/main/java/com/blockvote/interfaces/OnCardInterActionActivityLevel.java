package com.blockvote.interfaces;

import com.blockvote.auxillary.ElectionInstance;

/**
 * Created by Beast Mode on 3/15/2017.
 */

public interface OnCardInterActionActivityLevel {
    void onNewElectionCardPress();
    void onElectionCardPress(ElectionInstance electionInstance);
}
