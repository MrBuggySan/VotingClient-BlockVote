package com.blockvote.votingclient;

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
import com.blockvote.model.MODEL_UserAuthorizationStatus;
import com.blockvote.model.POST_BODY_writeVote;
import com.blockvote.networking.BlockVoteServerAPI;
import com.blockvote.networking.BlockVoteServerInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BallotConfirmationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BallotConfirmationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BallotConfirmationFragment extends Fragment {
    private final String LOG_TAG = BallotConfirmationFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private String choice;
    private String voterName;
    private String timestamp;

    private OnFragmentInteractionListener mListener;

    public BallotConfirmationFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance.
     * @param choice
     * @param timestamp
     * @return
     */
    public static BallotConfirmationFragment newInstance(String choice, String timestamp, String voterName) {
        BallotConfirmationFragment fragment = new BallotConfirmationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, choice);
        args.putString(ARG_PARAM2, timestamp);
        args.putString(ARG_PARAM3, voterName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            choice = getArguments().getString(ARG_PARAM1);
            timestamp = getArguments().getString(ARG_PARAM2);
            voterName = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ballot_confirmation, container, false);
        //Update the TextViews with the voter's choice
        TextView textView_choice = (TextView)rootView.findViewById(R.id.confirmation_choice);
        textView_choice.setText(choice);

//        TextView textView_timestamp = (TextView)rootView.findViewById(R.id.confirmation_timestamp);
//        textView_timestamp.setText(timestamp);

        //disable loading circle
        rootView.findViewById(R.id.ballot_confirmation_loadingPanel).setVisibility(View.GONE);

        //TODO: when YES, start the networking code to submit the data to the server
        Button yesButton = (Button) rootView.findViewById(R.id.confirmation_yes_button);
        yesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mListener != null) {

                    String registrarName = getString(R.string.regigstrarName);

                    //send the request of the voter to the server
                    BlockVoteServerInstance blockVoteServerInstance = new BlockVoteServerInstance();
                    BlockVoteServerAPI apiService = blockVoteServerInstance.getAPI();
                    //TODO: hard coded district for now. I'm too lazy to save the data from the registrationFragment
                    Call<MODEL_UserAuthorizationStatus> call = apiService.writeVote(new POST_BODY_writeVote(registrarName, voterName, choice, "edinburgh"));

                    call.enqueue(new Callback<MODEL_UserAuthorizationStatus>() {
                        @Override
                        public void onResponse(Call<MODEL_UserAuthorizationStatus> call, Response<MODEL_UserAuthorizationStatus> response) {

                            MODEL_UserAuthorizationStatus ServerResponse = response.body();
                            if(ServerResponse.getError() != null){
                                //Handle the error
                                ToastWrapper.initiateToast(getContext(), "Server has recieved your vote, " +
                                        "but something wierd happened.");
                                return;
                            }
                            Log.v(LOG_TAG, voterName + " has succesfully voted.");
                            ToastWrapper.initiateToast(getContext(), voterName + " has succesfully voted.");
                            //TODO: have ElectionActivity call ReviewBallotFragment
                            mListener.onYesBallotConfirmation();


                        }

                        @Override
                        public void onFailure(Call<MODEL_UserAuthorizationStatus> call, Throwable t) {
                            String msg = "Failed to send the ballot due to network errors";
                            Log.e(LOG_TAG, msg);
                            Log.e(LOG_TAG, t.getMessage());
                            ToastWrapper.initiateToast(getContext(), msg);

                        }
                    });
                    View rootView_ = getView();
                    //enable the loading circle
                    rootView_.findViewById(R.id.ballot_confirmation_loadingPanel).setVisibility(View.VISIBLE);

                    //disable the rest
                    rootView_.findViewById(R.id.ballot_confirm_screen).setVisibility(View.GONE);

                }
            }
        });
        //TODO: when NO, go back to the SelectCandidateFragment
        Button noButton = (Button) rootView.findViewById(R.id.confirmation_no_button);
        noButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onNoBallotConfirmation();
                }
            }
        });

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
        // TODO: Update argument type and name
        void onYesBallotConfirmation();
        void onNoBallotConfirmation();
    }
}
