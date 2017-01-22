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

import com.blockvote.model.SendDO;
import com.blockvote.model.TestAPIinterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String choice;
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
    public static BallotConfirmationFragment newInstance(String choice, String timestamp) {
        BallotConfirmationFragment fragment = new BallotConfirmationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, choice);
        args.putString(ARG_PARAM2, timestamp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            choice = getArguments().getString(ARG_PARAM1);
            timestamp = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ballot_confirmation, container, false);
        //Update the TextViews with the voter's choice
        TextView textView_choice = (TextView)rootView.findViewById(R.id.confirmation_choice);
        textView_choice.setText(choice);

        TextView textView_timestamp = (TextView)rootView.findViewById(R.id.confirmation_timestamp);
        textView_timestamp.setText(timestamp);

        //TODO: when YES, start the networking code to submit the data to the server
        //Plan for possible delays from the server side when it is submitting data to the chaincode
        Button yesButton = (Button) rootView.findViewById(R.id.confirmation_yes_button);
        yesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mListener != null) {
                    //TODO: send the voter choice and metadata to the server
                    // Trailing slash is needed
                    String BASE_URL = "https://MrBuggyNodeTester.mybluemix.net/";

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    TestAPIinterface apiService =
                            retrofit.create(TestAPIinterface.class);

                    //The Gson converter will automatically be converted to a JSON string
                    Call<SendDO> call = apiService.getHello(new SendDO("I like Android"));
//                    Call<SendDO> call = apiService.getHello();

                    call.enqueue(new Callback<SendDO>() {
                        @Override
                        public void onResponse(Call<SendDO> call, Response<SendDO> response) {
                            int statusCode = response.code();
                            String results =  response.body().getTestString();
                            Log.d(LOG_TAG, "Let our response be: "+ results);
                        }

                        @Override
                        public void onFailure(Call<SendDO> call, Throwable t) {
                            // Log error here since request failed
                        }
                    });

                    //TODO: have ElectionActivity call ReviewBallotFragment
                    mListener.onYesBallotConfirmation();
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
