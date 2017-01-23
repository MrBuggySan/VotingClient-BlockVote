package com.blockvote.votingclient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class RegistrationStatusFragment extends Fragment {
    private final String LOG_TAG = RegistrationStatusFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";


    private String voterName;


    public RegistrationStatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment RegistrationStatusFragment.
     */
    public static RegistrationStatusFragment newInstance(String param1) {
        RegistrationStatusFragment fragment = new RegistrationStatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            voterName = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_registration_status, container, false);
        TextView textView_message = (TextView)rootView.findViewById(R.id.reg_status_message);
        //TODO: periodic check of the status of the registration
        //TODO: have this periodic check happening in the background
        //Update the message
        textView_message.setText(voterName + " your registration request has been sent, please wait for its confirmation" +
                "by our registrars");

        Log.d(LOG_TAG, "The server has succesfully recieved the registration request");

        return rootView;
    }
}
