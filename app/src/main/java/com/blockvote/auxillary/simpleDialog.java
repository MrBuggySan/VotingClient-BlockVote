package com.blockvote.auxillary;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.blockvote.votingclient.R;

/**
 * Created by Beast Mode on 2/8/2017.
 */

public class simpleDialog {

    public simpleDialog(Context context, int title, int message){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title);

        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                //Do nothing
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
