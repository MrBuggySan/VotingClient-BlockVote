package com.blockvote.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.model.MODEL_RequestToVote;
import com.blockvote.model.POST_BODY_RegistrationRequest;
import com.blockvote.networking.BlockVoteServerAPI;
import com.blockvote.networking.BlockVoteServerInstance;
import com.blockvote.votingclient.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrationConfirmationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrationConfirmationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationConfirmationFragment extends Fragment {
    private final String LOG_TAG = RegistrationConfirmationFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private String firstName;
    private String lastName;
    private String districtName;

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
    public static RegistrationConfirmationFragment newInstance(String firstName, String lastName, String districtName) {
        RegistrationConfirmationFragment fragment = new RegistrationConfirmationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, firstName);
        args.putString(ARG_PARAM2, lastName);
        args.putString(ARG_PARAM3, districtName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            firstName = getArguments().getString(ARG_PARAM1);
            lastName = getArguments().getString(ARG_PARAM2);
            districtName = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration_confirmation, container, false);

        //Update the TextViews with the voter information
        TextView textView_VoterName = (TextView)rootView.findViewById(R.id.reg_confirmation_votername);
        textView_VoterName.setText(firstName + " " + lastName  +" from "+ districtName);

        //disable loading circle
        rootView.findViewById(R.id.reg_confirmation_loadingPanel).setVisibility(View.GONE);

        Button yesButton = (Button) rootView.findViewById(R.id.reg_confirmation_yes_button);
        yesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mListener != null) {
                    final String voterName = firstName + " " + lastName;
                    String registrarName = getString(R.string.regigstrarName);

                    //send the request of the voter to the server
                    BlockVoteServerInstance blockVoteServerInstance = new BlockVoteServerInstance();
                    BlockVoteServerAPI apiService = blockVoteServerInstance.getAPI();
                    Call<MODEL_RequestToVote> call = apiService.sendRegistrationRequest(new POST_BODY_RegistrationRequest(registrarName, voterName));

                    call.enqueue(new Callback<MODEL_RequestToVote>() {
                        @Override
                        public void onResponse(Call<MODEL_RequestToVote> call, Response<MODEL_RequestToVote> response) {

                            MODEL_RequestToVote ServerResponse = response.body();
                            if(ServerResponse.getError() != null){
                                //Handle the error
                                String msg = ServerResponse.getError().getMessage();
                                Log.e(LOG_TAG, voterName + " " + msg);
                                ToastWrapper.initiateToast(getContext(), msg);
                                return;
                            }
                            //TODO: do something with the response?
                            Log.v(LOG_TAG, voterName + " has succesfully sent the registration request.");
                            mListener.onYesRegistrationInteraction(voterName);


                        }

                        @Override
                        public void onFailure(Call<MODEL_RequestToVote> call, Throwable t) {
                            String msg = "Failed to send the registration request due to network errors";
                            Log.e(LOG_TAG, msg);
                            Log.e(LOG_TAG, t.getMessage());
                            ToastWrapper.initiateToast(getContext(), msg);

                        }
                    });
                    View rootView_ = getView();
                    //enable the loading circle
                    rootView_.findViewById(R.id.reg_confirmation_loadingPanel).setVisibility(View.VISIBLE);

                    //disable the rest
                    rootView_.findViewById(R.id.reg_confirmation_screen).setVisibility(View.GONE);

                }
            }
        });

        // when NO, go back to the RegisterFragment
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
        void onYesRegistrationInteraction(String voterName);
        void onNoRegistrationInteraction();
    }
}
