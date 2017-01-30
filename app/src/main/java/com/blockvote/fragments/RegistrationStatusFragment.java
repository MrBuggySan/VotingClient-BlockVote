package com.blockvote.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.model.MODEL_UserAuthorizationStatus;
import com.blockvote.model.POST_BODY_RegistrationRequest;
import com.blockvote.networking.BlockVoteServerAPI;
import com.blockvote.networking.BlockVoteServerInstance;
import com.blockvote.votingclient.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistrationStatusFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final String LOG_TAG = RegistrationStatusFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";

    private RegistrationStatusFragment.OnFragmentInteractionListener mListener;

    private String voterName;
    private String registrarName;


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
        registrarName = getString(R.string.regigstrarName);
        final SwipeRefreshLayout myRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.reg_status_swiperefresh);
        myRefresh.setOnRefreshListener(this);


        TextView textView_message = (TextView)rootView.findViewById(R.id.reg_status_message);

        //Update the message
        textView_message.setText(voterName + " your registration request has been sent, please wait for its confirmation" +
                "by our registrars");

        Log.d(LOG_TAG, "The server has succesfully recieved the registration request");

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegistrationStatusFragment.OnFragmentInteractionListener) {
            mListener = (RegistrationStatusFragment.OnFragmentInteractionListener) context;
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
        void onVoterAuthorized();
    }

    @Override
    public void onRefresh() {

        //ask for the status of the voter
        BlockVoteServerInstance blockVoteServerInstance = new BlockVoteServerInstance();
        BlockVoteServerAPI apiService = blockVoteServerInstance.getAPI();
        Call<MODEL_UserAuthorizationStatus> call = apiService.statusRegistrationRequest(new POST_BODY_RegistrationRequest(registrarName, voterName));

        call.enqueue(new Callback<MODEL_UserAuthorizationStatus>() {
            @Override
            public void onResponse(Call<MODEL_UserAuthorizationStatus> call, Response<MODEL_UserAuthorizationStatus> response) {
                final SwipeRefreshLayout myRefresh = (SwipeRefreshLayout) getView().findViewById(R.id.reg_status_swiperefresh);
                //end the refresh animation
                myRefresh.setRefreshing(false);

                MODEL_UserAuthorizationStatus serverResponse = response.body();

                //server error occured
                if(serverResponse.getError() != null){

                    Log.e(LOG_TAG, voterName + " authorization status request error ");
                    ToastWrapper.initiateToast(getContext(), voterName + "request sent but there were errors");

                    return;
                }
                Log.v(LOG_TAG, voterName + " has succesfully sent the authorization status request.");
                //voter can now vote
                if(serverResponse.getResponse().equals("yes")){
                    Log.v(LOG_TAG, voterName + " is now allowed to vote.");
                    ToastWrapper.initiateToast(getContext(), voterName + " is now allowed to vote.");

                    //TODO: call the VoteButtonFragment
                    mListener.onVoterAuthorized();
                    return;
                }
                //voter cannot vote yet
                if(serverResponse.getResponse().equals("no")){
                    Log.v(LOG_TAG, voterName + " is not authorized to vote yet.");
                    //stay in the RegistrationStatusFragment
                    ToastWrapper.initiateToast(getContext(), "You are not allowed to vote yet.");
                    return;
                }

                Log.e(LOG_TAG, voterName + " response other than yes/no from server ");
                ToastWrapper.initiateToast(getContext(), voterName + ", a response other than yes/no from server has been received." +
                        " What do we do?");
                return;

            }

            @Override
            public void onFailure(Call<MODEL_UserAuthorizationStatus> call, Throwable t) {
                String msg = "Failed to send the authorization status request to the server due to network errors";
                Log.e(LOG_TAG, msg);
                Log.e(LOG_TAG, t.getMessage());
                ToastWrapper.initiateToast(getContext(), voterName + ", " + msg);

            }
        });
    }

}
