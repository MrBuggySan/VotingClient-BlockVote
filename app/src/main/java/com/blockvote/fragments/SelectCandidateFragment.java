package com.blockvote.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.model.MODEL_ElectionInfo;
import com.blockvote.model.MODEL_writeVote;
import com.blockvote.networking.BlockVoteServerAPI;
import com.blockvote.networking.BlockVoteServerInstance;
import com.blockvote.votingclient.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectCandidateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectCandidateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectCandidateFragment extends Fragment {
    private final String LOG_TAG = SelectCandidateFragment.class.getSimpleName();
    ArrayAdapter<String> mCandidateList;

    private OnFragmentInteractionListener mListener;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    private String districtName;
    private String signedTokenIDKey;
    private String signedTokenSigKey;
    private String registrarNameKey;

    public SelectCandidateFragment() {
        // Required empty public constructor
    }

    public static SelectCandidateFragment newInstance(String districtName_, String signedTokenIDKey_,
                                                      String signedTokenSigKey_, String registrarNameKey_) {
        SelectCandidateFragment fragment = new SelectCandidateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, districtName_);
        args.putString(ARG_PARAM2, signedTokenIDKey_);
        args.putString(ARG_PARAM3, signedTokenSigKey_);
        args.putString(ARG_PARAM4, registrarNameKey_);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            districtName = getArguments().getString(ARG_PARAM1);
            signedTokenIDKey = getArguments().getString(ARG_PARAM2);
            signedTokenSigKey = getArguments().getString(ARG_PARAM3);
            registrarNameKey = getArguments().getString(ARG_PARAM4);
        }
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
        void onYesConfirmCandidateSelectInteraction();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_select_candidate, container, false);
        TextView blurb = (TextView)rootView.findViewById(R.id.candi_List_blurb);
        blurb.setText("Here are the candidates available for " + districtName);

        //get the options from the server
        getBallotOptions();
        return rootView;
    }

    public void confirmVote(String option){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String message = "Please confirm that you selected " + option;
        builder.setMessage(message)
                .setTitle(R.string.dialog_title_WARNING);
        final String optionFin = option;


        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO: submit the vote here
                ToastWrapper.initiateToast(getContext(),"Submitting your vote...");
                submitVote(optionFin);

            }
        })
        .setNegativeButton(R.string.neg_button_SelecCandi, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do nothing
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void submitVote(String option){
        BlockVoteServerInstance blockVoteServerInstance = new BlockVoteServerInstance();
        BlockVoteServerAPI apiService = blockVoteServerInstance.getAPI();

        String region = "US";
        String signedTokenID = getActivity().getPreferences(Context.MODE_PRIVATE).getString(signedTokenIDKey, null);
        String signedTokenSig = getActivity().getPreferences(Context.MODE_PRIVATE).getString(signedTokenSigKey, null);
        String registrarName =  getActivity().getPreferences(Context.MODE_PRIVATE).getString(registrarNameKey, null);
        if(signedTokenID == null || signedTokenSig == null){
            Log.e(LOG_TAG, "failed to get the tokenID and tokenSig from datastore.");
            throw new RuntimeException("\"failed to get the tokenID and tokenSig from datastore.\"");
        }
        if(registrarName == null){
            Log.e(LOG_TAG, "failed to get the registrar's namefrom datastore");
            throw new RuntimeException("failed to get the registrar's namefrom datastore");
        }
        //TODO: grab registarName from sharedPreferences


        Call<MODEL_writeVote> call = apiService.writeVote(region, signedTokenID, signedTokenSig,
                option ,registrarName);

        call.enqueue(new Callback<MODEL_writeVote>() {
            @Override
            public void onResponse(Call<MODEL_writeVote> call, Response<MODEL_writeVote> response) {
                int statusCode = response.code();
                Log.v(LOG_TAG, "Response code" + statusCode);
                String disclaimer = response.body().getResponse().getDisclaimer();
                ToastWrapper.initiateToast(getContext(), disclaimer);

                //move on to reviewBallotfragment
                mListener.onYesConfirmCandidateSelectInteraction();
            }

            @Override
            public void onFailure(Call<MODEL_writeVote> call, Throwable t) {
                Log.e(LOG_TAG, "Submitting vote has failed");
                throw new RuntimeException("Could not submit vote do to network issues.");
                //TODO:Restart the connection if failure
            }
        });

    }

    public void getBallotOptions(){
        BlockVoteServerInstance blockVoteServerInstance = new BlockVoteServerInstance();
        BlockVoteServerAPI apiService = blockVoteServerInstance.getAPI();
        Call<MODEL_ElectionInfo> call = apiService.getElectionInfo();

        call.enqueue(new Callback<MODEL_ElectionInfo>() {
            @Override
            public void onResponse(Call<MODEL_ElectionInfo> call, Response<MODEL_ElectionInfo> response) {
                int statusCode = response.code();

                mCandidateList = new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.listentry,
                        R.id.listEntry,
                        new ArrayList<String>()
                );

                List<String> electionOptions = response.body().getResponse().getElectionData().getVoteOptions();
                mCandidateList.add(electionOptions.get(2));
                mCandidateList.add(electionOptions.get(1));
                mCandidateList.add(electionOptions.get(0));

                ListView listView=(ListView) getView().findViewById(R.id.listview_candidatelist);
                listView.setAdapter(mCandidateList);
                //setup the button event when a candidate is pressed
                listView.setOnItemClickListener( new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                        String choice=mCandidateList.getItem(i);
                        //TODO: get the timestamp

                        //confirm the vote
                        confirmVote(choice);

                    }
                });
            }

            @Override
            public void onFailure(Call<MODEL_ElectionInfo> call, Throwable t) {
                Log.e(LOG_TAG, "Downloading th" +
                        "e election options has failed...");
                ToastWrapper.initiateToast(getContext(), "Downloading the election options has failed...");
                //TODO:Restart the connection if failure
            }
        });

    }
}
