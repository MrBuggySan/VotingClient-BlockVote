package com.blockvote.auxillary;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.blockvote.votingclient.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Created by andreibuiza on 02/03/17.
 */

public class QRCreatorService extends IntentService {

    public QRCreatorService() {
        super(QRCreatorService.class.getSimpleName());
    }

    private final String LOG_TAG = IntentService.class.getSimpleName();
    private final static int QRcodeWidth = 500 ;
    @Override
    protected void onHandleIntent(Intent workIntent) {
        Bundle extraData = workIntent.getExtras();
        String tokenMsg = extraData.getString(getString(R.string.QRCreatorServiceString));
        Log.v(LOG_TAG, "Starting to create the QR in the background...");

        try{
            Bitmap bitmap= TextToImageEncode(tokenMsg);

            //We have to deliver the result to the UI thread
            Intent localIntent =
                    new Intent(getString(R.string.BackgroundQRAction))
                            // Puts the status into the Intent
                            .putExtra(getString(R.string.GeneratedQR_from_background), bitmap);
            // Broadcasts the Intent to receivers in this app.
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);


        }catch(WriterException writerException){
            Log.e(LOG_TAG, "Failure to create the QR code " + "\n" + writerException.getMessage() );

        }
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        Log.v(LOG_TAG, "QR Width: " + bitMatrixWidth + ", Height: " + bitMatrixHeight);
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {
//                getResources().getColor(R.color.QR)
                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRDarkColor):getResources().getColor(R.color.QRWhite);

            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, bitMatrixWidth, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }



}
