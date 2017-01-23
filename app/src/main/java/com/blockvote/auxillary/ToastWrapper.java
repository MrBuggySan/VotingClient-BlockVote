package com.blockvote.auxillary;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by andreibuiza on 23/01/17.
 */

public class ToastWrapper {

    public static void initiateToast(Context context, String msg){
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, msg, duration).show();

    }
}
