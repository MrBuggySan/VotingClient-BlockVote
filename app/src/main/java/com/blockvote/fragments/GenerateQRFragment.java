package com.blockvote.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.blockvote.auxillary.simpleDialog;
import com.blockvote.votingclient.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GenerateQRFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GenerateQRFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenerateQRFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private final static int QRcodeWidth = 1000 ;


    public GenerateQRFragment() {
        // Required empty public constructor
    }

    public static GenerateQRFragment newInstance() {
        GenerateQRFragment fragment = new GenerateQRFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_generate_qr, container, false);

        //hide the rest of the layouts
        rootView.findViewById(R.id.image_QRCode).setVisibility(View.GONE);
        rootView.findViewById(R.id.generateQR_nextbutton).setVisibility(View.GONE);

        //create a dialog
        new simpleDialog(getContext(), R.string.dialog_title_DLFrag, R.string.dialog_message_GenQR);

        //TODO: get the blindedToken
        //fake 2048 bit key, or 256 bytes
        String fakeToken ="MIICXAIBAAKBgQCqGKukO1De7zhZj6+H0qtjTkVxwTCpvKe4eCZ0FPqri0cb2JZfXJ/DgYSF6vUp\n" +
                "wmJG8wVQZKjeGcjDOL5UlsuusFncCzWBQ7RKNUSesmQRMSGkVb1/3j+skZ6UtW+5u09lHNsj6tQ5\n" +
                "1s1SPrCBkedbNf0Tp0GbMJDyR4e9T04ZZwIDAQABAoGAFijko56+qGyN8M0RVyaRAXz++xTqHBLh\n" +
                "3tx4VgMtrQ+WEgCjhoTwo23KMBAuJGSYnRmoBZM3lMfTKevIkAidPExvYCdm5dYq3XToLkkLv5L2\n" +
                "pIIVOFMDG+KESnAFV7l2c+cnzRMW0+b6f8mR1CJzZuxVLL6Q02fvLi55/mbSYxECQQDeAw6fiIQX\n" +
                "GukBI4eMZZt4nscy2o12KyYner3VpoeE+Np2q+Z3pvAMd/aNzQ/W9WaI+NRfcxUJrmfPwIGm63il\n" +
                "AkEAxCL5HQb2bQr4ByorcMWm/hEP2MZzROV73yF41hPsRC9m66KrheO9HPTJuo3/9s5p+sqGxOlF\n" +
                "L0NDt4SkosjgGwJAFklyR1uZ/wPJjj611cdBcztlPdqoxssQGnh85BzCj/u3WqBpE2vjvyyvyI5k\n" +
                "X6zk7S0ljKtt2jny2+00VsBerQJBAJGC1Mg5Oydo5NwD6BiROrPxGo2bpTbu/fhrT8ebHkTz2epl\n" +
                "U9VQQSQzY1oZMVX8i1m5WUTLPz2yLJIBQVdXqhMCQBGoiuSoSjafUhV7i1cEGpb88h5NBYZzWXGZ\n" +
                "37sJ5QsW+sJyoNde3xH8vdXhzU7eT82D6X/scw9RZz+/6rCJ4p0=MIICXAIBAAKBgQCqGKukO1De7zhZj6+H0qtjTkVxwTCpvKe4eCZ0FPqri0cb2JZfXJ/DgYSF6vUp\n" +
                "wmJG8wVQZKjeGcjDOL5UlsuusFncCzWBQ7RKNUSesmQRMSGkVb1/3j+skZ6UtW+5u09lHNsj6tQ5\n" +
                "1s1SPrCBkedbNf0Tp0GbMJDyR4e9T04ZZwIDAQABAoGAFijko56+qGyN8M0RVyaRAXz++xTqHBLh\n" +
                "3tx4VgMtrQ+WEgCjhoTwo23KMBAuJGSYnRmoBZM3lMfTKevIkAidPExvYCdm5dYq3XToLkkLv5L2\n" +
                "pIIVOFMDG+KESnAFV7l2c+cnzRMW0+b6f8mR1CJzZuxVLL6Q02fvLi55/mbSYxECQQDeAw6fiIQX\n" +
                "GukBI4eMZZt4nscy2o12KyYner3VpoeE+Np2q+Z3pvAMd/aNzQ/W9WaI+NRfcxUJrmfPwIGm63il\n" +
                "AkEAxCL5HQb2bQr4ByorcMWm/hEP2MZzROV73yF41hPsRC9m66KrheO9HPTJuo3/9s5p+sqGxOlF\n" +
                "L0NDt4SkosjgGwJAFklyR1uZ/wPJjj611cdBcztlPdqoxssQGnh85BzCj/u3WqBpE2vjvyyvyI5k\n" +
                "X6zk7S0ljKtt2jny2+00VsBerQJBAJGC1Mg5Oydo5NwD6BiROrPxGo2bpTbu/fhrT8ebHkTz2epl\n" +
                "U9VQQSQzY1oZMVX8i1m5WUTLPz2yLJIBQVdXqhMCQBGoiuSoSjafUhV7i1cEGpb88h5NBYZzWXGZ\n" +
                "37sJ5QsW+sJyoNde3xH8vdXhzU7eT82D6X/scw9RZz+/6rCJ4p0=";

        //start generating the QR
        new QRGenerator(rootView).execute(fakeToken);

        //TODO: I must somehow cache the QR so I don't have to generate it everytime.

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onBackGenQRSelected();
        void onNextGenQRSelected();
    }


    public class QRGenerator extends AsyncTask<String, Void, Bitmap> {
        private final String LOG_TAG= GenerateQRFragment.class.getSimpleName();
        private View rootView;

        public QRGenerator(View rootView){
            this.rootView=rootView;

        }

        @Override
        public Bitmap doInBackground (String... params) {
            try{
                Bitmap bitmap= TextToImageEncode(params[0]);
                return bitmap;
            }catch(WriterException writerException){
                Log.e(LOG_TAG, "QR generation failed... " + "\n" + writerException.getMessage() );
                return null;
            }

        }

        @Override
        public void onPostExecute(Bitmap bitmap){
            Log.v(LOG_TAG, "Displaying the QR now");
            ImageView imageView = (ImageView) rootView.findViewById(R.id.image_QRCode);
            imageView.setImageBitmap(bitmap);
            Button button = (Button)rootView.findViewById(R.id.generateQR_nextbutton);
            button.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      //TODO:go to scanQRFragment

                  }
              }
            );
            rootView.findViewById(R.id.image_QRCode).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.generateQR_nextbutton).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.QR_animation_view).setVisibility(View.GONE);
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
}
