package com.blockvote.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.interfaces.DefaultInteractions;
import com.blockvote.model.MODEL_readVote;
import com.blockvote.networking.BlockVoteServerAPI;
import com.blockvote.networking.BlockVoteServerInstance;
import com.blockvote.votingclient.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowBallotFragment extends Fragment {

    private View rootView;
    private DefaultInteractions defaultInteractions;
    private ElectionInstance electionInstance;
    private String LOG_TAG = ShowBallotFragment.class.getSimpleName();

    public ShowBallotFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DefaultInteractions) {
            defaultInteractions = (DefaultInteractions) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DefaultInteractions");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        defaultInteractions = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_select_candidate, container, false);
        rootView.findViewById(R.id.select_candidate_UI).setVisibility(View.GONE);
        electionInstance = defaultInteractions.getElectionInstance();
        Log.d(LOG_TAG, "onCreateView");

        getBallot();
        return rootView;
    }

    public void getBallot(){
        //get the ballot of the voter
        String signedTokenID = Base64.encodeToString(electionInstance.getSignedTokenID(), Base64.DEFAULT);
        String signedTokenSig = Base64.encodeToString(electionInstance.getSignedTokenSignature(), Base64.DEFAULT);
        Log.d(LOG_TAG,"signedTokenID: " + signedTokenID);
        Log.d(LOG_TAG,"signedTokenSig: " + signedTokenSig);

        BlockVoteServerInstance blockVoteServerInstance = new BlockVoteServerInstance(electionInstance.getElectionURL());
        BlockVoteServerAPI apiService = blockVoteServerInstance.getAPI();
        Call<MODEL_readVote> call = apiService.readVote("US", signedTokenID, signedTokenSig);

        call.enqueue(new Callback<MODEL_readVote>() {
            @Override
            public void onResponse(Call<MODEL_readVote> call, Response<MODEL_readVote> response) {
                int statusCode = response.code();
                if(statusCode!= 200){
                    Log.e(LOG_TAG, "Requesting your ballot has failed, response code: " + statusCode);
                    ToastWrapper.initiateToast(getContext(),"Requesting your ballot has failed, response code: " + statusCode );
                    return;
                }


                if(response.body().getResponse() == null){
                    //something went wrong
                    Log.e(LOG_TAG, response.body().getError().getMessage());
                    Log.e(LOG_TAG, "Requesting your ballot has failed, there was an error");
                    ToastWrapper.initiateToast(getContext(),"Requesting your ballot has failed there was an error");
                    return;
                }


                String respStr = response.body().getResponse();

                Log.v(LOG_TAG, respStr);
                try{
                    JSONObject resp = new JSONObject(respStr);
                    String choice;
                    choice = resp.getString("Vote");
                    displayBallot(choice);

                }catch(JSONException e){

                }

            }

            @Override
            public void onFailure(Call<MODEL_readVote> call, Throwable t) {
                Log.e(LOG_TAG, "Submitting vote has failed");
                ToastWrapper.initiateToast(getContext(), "Could not connect to the internet ");
                getActivity().onBackPressed();
            }
        });
    }

    public void displayBallot(String choice){
        rootView.findViewById(R.id.select_candidate_UI).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.select_candidate_loadingPanel).setVisibility(View.GONE);
        rootView.findViewById(R.id.submitButton).setVisibility(View.GONE);

        TextView qTextView = (TextView) rootView.findViewById(R.id.question_blurb);
        qTextView.setText(electionInstance.getElectionQuestion());


        List<String> electionOptions = electionInstance.getVoteOptions();
        RadioGroup ballotOptionsGroup = (RadioGroup) rootView.findViewById(R.id.choices_radiogrp);
        for(int i = 0 ; i < electionOptions.size(); i++){
            String optionStr = electionOptions.get(i);
            RadioButton option = new RadioButton(getContext());
            option.setId(3000 + i);
            option.setText(optionStr);
            option.setPadding(16,16,16,16);
            ballotOptionsGroup.addView(option);
            if(choice.equals(optionStr)){
                option.setChecked(true);
                continue;
            }
            option.setClickable(false);
        }


    }



}
