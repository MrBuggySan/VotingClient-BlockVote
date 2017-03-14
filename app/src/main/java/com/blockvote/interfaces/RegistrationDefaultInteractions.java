package com.blockvote.interfaces;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.ElectionState;

/**
 * Created by Beast Mode on 3/12/2017.
 */

public interface RegistrationDefaultInteractions extends DefaultInteractions {
    boolean saveElectionInstance(ElectionInstance electionInstance);
    boolean updateElectionInstanceState(ElectionState electionState);
    void setupQRReceiver();

}
