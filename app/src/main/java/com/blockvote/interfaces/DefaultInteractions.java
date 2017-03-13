package com.blockvote.interfaces;

import com.blockvote.auxillary.ElectionInstance;

/**
 * Created by Beast Mode on 3/11/2017.
 */

public interface DefaultInteractions {
    void changeTitleBarName(String name);
    ElectionInstance getElectionInstance();
}
