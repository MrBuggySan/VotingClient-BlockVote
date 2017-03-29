package com.blockvote.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.interfaces.DefaultInteractions;
import com.blockvote.model.MODEL_writeVote;
import com.blockvote.networking.BlockVoteServerAPI;
import com.blockvote.networking.BlockVoteServerInstance;
import com.blockvote.votingclient.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SelectCandidateFragment extends Fragment {
    private final String LOG_TAG = SelectCandidateFragment.class.getSimpleName();

    private OnClickSubmitBallot onClickSubmitBallot;
    private DefaultInteractions defaultInteractions;
    private ElectionInstance electionInstance;
    private View rootView;

    public SelectCandidateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnClickSubmitBallot
                && context instanceof DefaultInteractions) {
            onClickSubmitBallot = (OnClickSubmitBallot) context;
            defaultInteractions = (DefaultInteractions) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnClickSubmitBallot && DefaultInteractions");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        onClickSubmitBallot = null;
        defaultInteractions = null;
    }

    public interface OnClickSubmitBallot {
        void onSuccesfullSubmission();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_select_candidate, container, false);
        electionInstance = defaultInteractions.getElectionInstance();
        rootView.findViewById(R.id.select_candidate_loadingPanel).setVisibility(View.GONE);
        setupElectionQuestion();
        setupballotOptions();
        return rootView;
    }

    private void setupElectionQuestion(){
        TextView qTextView = (TextView) rootView.findViewById(R.id.question_blurb);
        qTextView.setText(electionInstance.getElectionQuestion());
    }


    private void setupballotOptions(){

        List<String> electionOptions = electionInstance.getVoteOptions();
        RadioGroup ballotOptionsGroup = (RadioGroup) rootView.findViewById(R.id.choices_radiogrp);
        for(int i = 0 ; i < electionOptions.size(); i++){
            RadioButton option = new RadioButton(getContext());
            option.setId(3000 + i);
            option.setText(electionOptions.get(i));
            option.setPadding(16,16,16,16);
            ballotOptionsGroup.addView(option);
        }


        //setup the button event when submit is pressed
        Button submitButton = (Button) rootView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(
        new View.OnClickListener() {
            public void onClick(View v) {
                //Check if a choice is selected
                RadioGroup ballotOptionsGroup = (RadioGroup) rootView.findViewById(R.id.choices_radiogrp);
                int selectedID = ballotOptionsGroup.getCheckedRadioButtonId();
                if(selectedID == -1 ){
                    //do nothing
                    return;
                }

                //get the choice
                RadioButton selectedRadio = (RadioButton) rootView.findViewById(selectedID);

                String choice = selectedRadio.getText().toString();
                confirmVote(choice);
            }
        });

    }

    public void confirmVote(String option){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String message = "Please confirm that you selected " + option;
        builder.setMessage(message)
                .setTitle(R.string.dialog_title_Attention);
        final String optionFin = option;


        // Add the buttons
        builder.setPositiveButton(R.string.pos_button_SelecCandi, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                submitVote(optionFin, electionInstance.getElectionURL());
//                onClickSubmitBallot.onYesConfirmCandidateSelect();
            }
        }).setNegativeButton(R.string.neg_button_SelecCandi, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do nothing
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void submitVote(String option, String electionURL){
        BlockVoteServerInstance blockVoteServerInstance = new BlockVoteServerInstance(electionURL);
        BlockVoteServerAPI apiService = blockVoteServerInstance.getAPI();

        String region = "US";
        String signedTokenID = Base64.encodeToString(electionInstance.getSignedTokenID(), Base64.DEFAULT);
        String signedTokenSig = Base64.encodeToString(electionInstance.getSignedTokenSignature(), Base64.DEFAULT);
        String registrarName =  electionInstance.getRegistrarName();

        Log.d(LOG_TAG, "region: " + region);
        Log.d(LOG_TAG, "signedTokenID: " + signedTokenID);
        Log.d(LOG_TAG, "signedTokenSig: " + signedTokenSig);
        Log.d(LOG_TAG, "vote: " + option);
        Log.d(LOG_TAG, "registrarName: " + registrarName);

        if(signedTokenID == null || signedTokenSig == null){
            Log.e(LOG_TAG, "failed to get the tokenID and tokenSig from datastore.");
            throw new RuntimeException("failed to get the tokenID and tokenSig from datastore.");
        }
        if(registrarName == null){
            Log.e(LOG_TAG, "failed to get the registrar's namefrom datastore");
            throw new RuntimeException("failed to get the registrar's namefrom datastore");
        }

        rootView.findViewById(R.id.select_candidate_loadingPanel).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.select_candidate_UI).setVisibility(View.GONE);


        Call<MODEL_writeVote> call = apiService.writeVote(region, signedTokenID, signedTokenSig,
                option ,registrarName);

        call.enqueue(new Callback<MODEL_writeVote>() {
            @Override
            public void onResponse(Call<MODEL_writeVote> call, Response<MODEL_writeVote> response) {
                int statusCode = response.code();
                if(statusCode!= 200){
                    Log.e(LOG_TAG, "Submitting your vote has failed, response code: " + statusCode);
                    ToastWrapper.initiateToast(getContext(),"Submitting your vote has failed, response code: " + statusCode );
                    return;
                }


                if(response.body().getResponse() == null){
                    //something went wrong
                    Log.e(LOG_TAG, response.body().getError().getMessage());
                    Log.e(LOG_TAG, "Submitting your vote has failed, there was an error");
                    ToastWrapper.initiateToast(getContext(),"Submitting your vote has failed, there was an error");
                    return;
                }

                //submit the vote here
                ToastWrapper.initiateToast(getContext(),"Submitting your vote...");

                Log.v(LOG_TAG, response.body().getResponse().getDisclaimer());
//                Log.v(LOG_TAG, "Response code " + response.body().getResponse().getResult());
                onClickSubmitBallot.onSuccesfullSubmission();
            }

            @Override
            public void onFailure(Call<MODEL_writeVote> call, Throwable t) {
                Log.e(LOG_TAG, "Submitting vote has failed");
//                throw new RuntimeException("Could not submit vote do to network issues.");
                ToastWrapper.initiateToast(getContext(), "Could not connect to the internet ");
                getActivity().onBackPressed();
            }
        });


    }




}
