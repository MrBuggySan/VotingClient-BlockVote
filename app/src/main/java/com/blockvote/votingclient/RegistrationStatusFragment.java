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
    private static final String ARG_PARAM2 = "param2";

    private String voterName;
    private boolean sentSuccesfully;

    public RegistrationStatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationStatusFragment.
     */
    public static RegistrationStatusFragment newInstance(String param1, boolean param2) {
        RegistrationStatusFragment fragment = new RegistrationStatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            voterName = getArguments().getString(ARG_PARAM1);
            sentSuccesfully = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_registration_status, container, false);
        TextView textView_message = (TextView)rootView.findViewById(R.id.reg_status_message);
        if(sentSuccesfully){
            //TODO: periodic check of the status of the registration

            //Update the message
            textView_message.setText(voterName + " your registration request has been sent, please wait for its confirmation" +
                    "by our registrars");

            Log.d(LOG_TAG, "The server has succesfully recieved the registration request");
        }else{
            //TODO: Show failure to send registration request

        }

        return rootView;
    }
}
