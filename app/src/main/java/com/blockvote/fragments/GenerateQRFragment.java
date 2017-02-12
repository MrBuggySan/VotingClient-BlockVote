package com.blockvote.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


import com.blockvote.auxillary.simpleDialog;
import com.blockvote.security.BlindedToken;
import com.blockvote.security.TokenRequest;
import com.blockvote.votingclient.R;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.spongycastle.crypto.CryptoException;
import org.spongycastle.crypto.params.RSAKeyParameters;

import java.math.BigInteger;

import static android.content.Context.MODE_PRIVATE;

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
    private final String LOG_TAG = GenerateQRFragment.class.getSimpleName();

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
        rootView.findViewById(R.id.generateQR_backbutton).setVisibility(View.GONE);
        rootView.findViewById(R.id.GenQR_textBlurb).setVisibility(View.GONE);

        //create a dialog
        new simpleDialog(getContext(), R.string.dialog_title_DLFrag, R.string.dialog_message_GenQR);

        //TODO: get the public key of the Registrar




        String sampleModulus = "AKBDi814o+/Ujo8bF1qjMnyotruZHLv5FWl/xYYFpKLgU4jmeXxhJKY36kmJFK+Kxt6anqmmVBKVZityfp+2lUzojYEJJ9Jzv4qQQQ4BcHijlrrBSvvWZ6KOVB30n2Lgxj99g6B1Eopyq4h+6TC3Sr/DBIkZ0tAH5a3+RG3Q8OEcYGpCQu7v4MIOgF+bFikeu6gk0Mob71TlPGauAIFpc4q3UVBjhbEIyc6vv76Z+RtNd3FZZzsLphzrJB4s6b6TwKpUsIWJ7dXBpkCSVv/sDtB4PeOrzHTH5UHGYkTLbF4o1ie23mbjhIWcSJryJrS+3VMaNuB+waImz/nlJEh/qy0=";

        String sampleExponent = "AQAB";
        RSAKeyParameters rsaKeyParameters = new RSAKeyParameters(false,
                new BigInteger(Base64.decode(sampleModulus, Base64.DEFAULT)),
                new BigInteger(Base64.decode(sampleExponent, Base64.DEFAULT)));
        //Create BlindedToken
        BlindedToken blindedToken = new BlindedToken( rsaKeyParameters );
        //Store the blindedToken in Shared Preferences
        SharedPreferences mPrefs = getActivity().getPreferences(MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(blindedToken);
        String jsonRSAKeyParams = gson.toJson(rsaKeyParameters);
        prefsEditor.putString(getString(R.string.blindedTokenkey), json);
        prefsEditor.putString(getString(R.string.rsaKeyPramKey), jsonRSAKeyParams);
        prefsEditor.commit();

        //Create tokenRequest
        try{
            TokenRequest tokenRequest = blindedToken.generateTokenRequest();
            byte[] tokenMsg = tokenRequest.getMessage();

            //start generating the QRcccc

            new QRGenerator(rootView).execute(Base64.encodeToString(tokenMsg, Base64.DEFAULT));
            Log.v(LOG_TAG, "Creating the sample blindedToken");


        }catch(CryptoException cryptoException){
            //TODO: handle this
        }

        //TODO: I must somehow cache the QR so I don't have to generate it everytime.


        //Button Events

        Button nextButton = (Button)rootView.findViewById(R.id.generateQR_nextbutton);
        nextButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  confirmNext();
              }
          }
        );

        Button backButton = (Button)rootView.findViewById(R.id.generateQR_backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              //create a dialog
              mListener.onBackGenQRSelected();

          }
}
        );

        return rootView;
    }

    public void confirmNext(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.dialog_message_scanQR)
                .setTitle(R.string.dialog_title_WARNING);

        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onNextGenQRSelected();

            }
        }).setNegativeButton(R.string.neg_button_SelecCandi, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do nothing
                    }
                });


        AlertDialog dialog = builder.create();
        dialog.show();
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

            rootView.findViewById(R.id.image_QRCode).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.generateQR_nextbutton).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.generateQR_backbutton).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.GenQR_textBlurb).setVisibility(View.VISIBLE);
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
