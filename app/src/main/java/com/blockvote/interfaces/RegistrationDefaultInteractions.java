package com.blockvote.interfaces;

import com.blockvote.auxillary.ElectionInstance;

/**
 * Created by Beast Mode on 3/12/2017.
 */

public interface RegistrationDefaultInteractions extends DefaultInteractions {
    void savetElectionInstance(ElectionInstance electionInstance);
    void setupQRReceiver();

}
