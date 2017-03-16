package com.blockvote.interfaces;

import android.view.View;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.ElectionState;

/**
 * Created by Beast Mode on 3/12/2017.
 */

public interface RegistrationDefaultInteractions extends DefaultInteractions {
    boolean saveNewElectionInstance(ElectionInstance electionInstance);
    boolean updateElectionInstanceState(ElectionState electionState);
    void setupQRReceiver();
    void setGenQRrootView(View rootView_);

}
