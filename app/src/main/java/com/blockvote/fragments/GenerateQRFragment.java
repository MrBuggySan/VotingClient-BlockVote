package com.blockvote.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.blockvote.auxillary.simpleDialog;
import com.blockvote.crypto.BlindedToken;
import com.blockvote.crypto.TokenRequest;
import com.blockvote.votingclient.R;
import com.google.gson.Gson;

import org.spongycastle.crypto.CryptoException;
import org.spongycastle.crypto.params.RSAKeyParameters;

import java.math.BigInteger;

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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String keyModulus;
    private String keyExponent;

    private View rootView;

    public GenerateQRFragment() {
        // Required empty public constructor
    }

    public static GenerateQRFragment newInstance(String keyMod , String keyExp ) {

        GenerateQRFragment fragment = new GenerateQRFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,keyMod );
        args.putString(ARG_PARAM2, keyExp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            keyModulus = getArguments().getString(ARG_PARAM1);
            keyExponent = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_generate_qr, container, false);

        //hide the rest of the layouts
        rootView.findViewById(R.id.genQR_UI).setVisibility(View.GONE);

        //create a dialog
        new simpleDialog(getContext(), R.string.dialog_title_Information, R.string.dialog_message_GenQR);

        RSAKeyParameters rsaKeyParameters = new RSAKeyParameters(false,
                new BigInteger(Base64.decode(keyModulus, Base64.DEFAULT)),
                new BigInteger(Base64.decode(keyExponent, Base64.DEFAULT)));
        //Create BlindedToken
        BlindedToken blindedToken = new BlindedToken( rsaKeyParameters );


        Gson gson = new Gson();
        String jsonBlindedToken = gson.toJson(blindedToken);
        String jsonRSAKeyParams = gson.toJson(rsaKeyParameters);
        //store these values in the ElectionActivity
        mListener.store_BlindedKey_RSAKeyParam(jsonBlindedToken, jsonRSAKeyParams);

        //Create tokenRequest
        try{
            TokenRequest tokenRequest = blindedToken.generateTokenRequest();
            byte[] tokenMsg = tokenRequest.getMessage();

            //start generating the QR
            //TODO: Create the background service
//            Intent mServiceIntent = new Intent(this, QRCreatorService.class);
//            mServiceIntent.putExtra(getString(R.string.QRCreatorServiceString), Base64.encodeToString(tokenMsg, Base64.DEFAULT));
//            this.startService(mServiceIntent);



            //TODO: When back is pressed, cancel the IntentService


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
                //Scan the registrar's QR code.
                mListener.onNextGenQRSelected();

            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
        void store_BlindedKey_RSAKeyParam(String jsonBlindedToken, String jsonRSAKeyParams);
    }


}
