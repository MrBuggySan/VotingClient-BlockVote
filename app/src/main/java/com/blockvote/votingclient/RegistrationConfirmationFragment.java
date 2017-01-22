package com.blockvote.votingclient;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrationConfirmationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrationConfirmationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationConfirmationFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String firstName;
    private String lastName;

    private OnFragmentInteractionListener mListener;

    public RegistrationConfirmationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param firstName Parameter 1.
     * @param lastName Parameter 2.
     * @return A new instance of fragment RegistrationConfirmationFragment.
     */
    public static RegistrationConfirmationFragment newInstance(String firstName, String lastName) {
        RegistrationConfirmationFragment fragment = new RegistrationConfirmationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, firstName);
        args.putString(ARG_PARAM2, lastName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            firstName = getArguments().getString(ARG_PARAM1);
            lastName = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration_confirmation, container, false);

        //Update the TextViews with the voter information
        TextView textView_VoterName = (TextView)rootView.findViewById(R.id.reg_confirmation_votername);
        textView_VoterName.setText(firstName + " " + lastName);

        Button yesButton = (Button) rootView.findViewById(R.id.reg_confirmation_yes_button);
        yesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mListener != null) {
                    String voterName = firstName + " " + lastName;
                    //TODO: start the networking code to submit the data to the server


                    //TODO: setup the loading circle
                    //TODO: Have the callback call onYesRegistrationInteraction to start the RegistrationStatusFragment
                    mListener.onYesRegistrationInteraction(voterName,true);
                }
            }
        });

        //TODO: when NO, go back to the RegisterFragment
        Button noButton = (Button) rootView.findViewById(R.id.reg_confirmation_no_button);
        noButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onNoRegistrationInteraction();
                }
            }
        });

        // Inflate the layout for this fragment
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onYesRegistrationInteraction(String voterName, boolean sentSuccessfully);
        void onNoRegistrationInteraction();
    }
}
