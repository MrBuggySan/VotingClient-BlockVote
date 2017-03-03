package com.blockvote.auxillary;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by andreibuiza on 02/03/17.
 */

public class QRCreatorService extends IntentService {

    @Override
    protected void onHandleIntent(Intent workIntent) {
        String dataString = workIntent.getDataString();

    }

}
