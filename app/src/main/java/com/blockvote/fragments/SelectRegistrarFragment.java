package com.blockvote.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.blockvote.auxillary.simpleDialog;
import com.blockvote.votingclient.ElectionActivity;
import com.blockvote.votingclient.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectRegistrarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectRegistrarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectRegistrarFragment extends Fragment {

    private final String LOG_TAG = ElectionActivity.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    public SelectRegistrarFragment() {
        // Required empty public constructor
    }

    public static SelectRegistrarFragment newInstance() {
        SelectRegistrarFragment fragment = new SelectRegistrarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(LOG_TAG, "Entering registrat select fragment...");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_select_registrar, container, false);

        //create a dialog
        new simpleDialog(getContext(), R.string.dialog_title_DLFrag, R.string.dialog_message_SelecReg);
        //TODO: get the registrar list from the server

        //fake registrar list


        //Setup the buttons
        Button nextButton = (Button) rootView.findViewById(R.id.select_registrar_next);
        //Register button callback
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mListener != null) {
                    onNextClick();
                }
            }
        });

        Button backButton = (Button) rootView.findViewById(R.id.select_registrar_back);
        //Register button callback
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mListener != null) {
                    onBackClick();
                }
            }
        });
        return rootView;
    }

    public void onNextClick(){
        //get the selected registrar

//        mListener.onNextSelected();
    }

    public void onBackClick(){
        mListener.onBackSelected();
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

        void onNextSelected(String registrarName);
        void onBackSelected();
    }
}
