package com.blockvote.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.auxillary.simpleDialog;
import com.blockvote.model.MODEL_ElectionInfo;
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
 * {@link RegistrationFormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.

 */
public class RegistrationFormFragment extends Fragment {
    private final String LOG_TAG = RegistrationFormFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    public RegistrationFormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_registration_form, container, false);

        //TODO: setup the popup so it only shows up the very first time the user opens the app
        //idea: have a don't show this again popup.
        //create a dialog
        new simpleDialog(getContext(), R.string.dialog_title_DLFrag, R.string.dialog_message_DLFrag);

        //TODO: get the name of the user from the system, have an option to edit the values


        //TODO: plan up a better UI for this fragment (use pictures to for the districts?)


        rootView.findViewById(R.id.reg_firstNameText).setVisibility(View.GONE);
        rootView.findViewById(R.id.reg_lastNameText).setVisibility(View.GONE);
        rootView.findViewById(R.id.register_districtspinner).setVisibility(View.GONE);
        rootView.findViewById(R.id.reg_register_button).setVisibility(View.GONE);
//
//        String[] mProjection = new String[]
//                {
//                        ContactsContract.Profile.DISPLAY_NAME_PRIMARY
//                };
//        ContentResolver mProfileCursor = getContentResolver().query(
//                ContactsContract.Profile.CONTENT_URI,
//                mProjection ,
//                null,
//                null,
//                null);


        //get the districts from the server
        getElectionInfo();



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
        void onDistrictListNextInteraction(String firstName, String lastName, String districtName, String registrarName);
    }

    public void displayDistrictsonSpinner(List<String> districtList){
        ArrayAdapter<String> mDistrictList = new ArrayAdapter<String>(
                getActivity(),
                R.layout.listentry,
                R.id.listEntry,
                new ArrayList<String>()
        );

        for (int i = 0; i < districtList.size(); i++) {
            Log.d(LOG_TAG, districtList.get(i) + " available.");
            mDistrictList.add(districtList.get(i));
        }

        //Setup the spinner showing the different districts available
        Spinner spinner = (Spinner) getView().findViewById(R.id.register_districtspinner);
        spinner.setAdapter(mDistrictList);

        //TODO: get the registrar list from the server
        ArrayList<String> registrarList = new ArrayList<String>();
        registrarList.add("jose");
        displayRegistrarSpinner(registrarList);

    }

    public void displayRegistrarSpinner(List<String> registrarList){
        ArrayAdapter<String> mRegistrarList = new ArrayAdapter<String>(
                getActivity(),
                R.layout.listentry,
                R.id.listEntry,
                new ArrayList<String>()
        );

        for (int i = 0; i < registrarList.size(); i++) {
            Log.d(LOG_TAG, registrarList.get(i) + " available.");
            mRegistrarList.add(registrarList.get(i));
        }

        //Setup the spinner showing the different districts available
        Spinner spinner = (Spinner) getView().findViewById(R.id.register_registrar_spinner);
        spinner.setAdapter(mRegistrarList);
    }

    public void onNextClick(){
        View rootView = getView();
        EditText firstText = (EditText) rootView.findViewById(R.id.reg_firstNameText);
        EditText lastText = (EditText) rootView.findViewById(R.id.reg_lastNameText);

        String firstName = firstText.getText().toString();
        String lastName = lastText.getText().toString();
        if (firstName.equals("") || lastName.equals("")) {
            ToastWrapper.initiateToast(getContext(), "Please enter your name");

            return;
        }
        Spinner districtSpinner = (Spinner) rootView.findViewById(R.id.register_districtspinner);
        Spinner registrarSpinner = (Spinner) rootView.findViewById(R.id.register_districtspinner);
        String districtName = districtSpinner.getSelectedItem().toString();
        String registrarName = registrarSpinner.getSelectedItem().toString();

        mListener.onDistrictListNextInteraction(firstName, lastName, districtName, registrarName);
    }

    public void getElectionInfo(){
        BlockVoteServerInstance blockVoteServerInstance = new BlockVoteServerInstance();
        BlockVoteServerAPI apiService = blockVoteServerInstance.getAPI();
        Call<MODEL_ElectionInfo> call = apiService.getElectionInfo();

        call.enqueue(new Callback<MODEL_ElectionInfo>() {
            @Override
            public void onResponse(Call<MODEL_ElectionInfo> call, Response<MODEL_ElectionInfo> response) {
                int statusCode = response.code();

                //TODO: This response is GET, next time it will be a POST so that i can query for specific election
                List<String> districtList = response.body().getResponse().getElectionData().getDistricts();
                //apply the results to the UI
                View rootView_ = getView();
                if(rootView_ != null){
                    Log.v(LOG_TAG, "RegistrationFormFragment marker.");

                    //TODO: change this to a better UI (possible a radio button)
                    displayDistrictsonSpinner(districtList);

                    //Setup the button
                    Button registerButton = (Button) rootView_.findViewById(R.id.reg_register_button);
                    //Register button callback
                    registerButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (mListener != null) {
                                onNextClick();
                            }
                        }
                    });

                    //disable the loading screen
                    rootView_.findViewById(R.id.registration_loadingPanel).setVisibility(View.GONE);

                    //Show the Views again
                    rootView_.findViewById(R.id.reg_firstNameText).setVisibility(View.VISIBLE);
                    rootView_.findViewById(R.id.reg_lastNameText).setVisibility(View.VISIBLE);
                    rootView_.findViewById(R.id.reg_register_button).setVisibility(View.VISIBLE);
                    rootView_.findViewById(R.id.register_districtspinner).setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<MODEL_ElectionInfo> call, Throwable t) {
                Log.e(LOG_TAG, "Downloading the district list has failed...");
                throw new RuntimeException("Could not download the election list");
                //TODO:Restart the connection if failure
            }
        });

    }
}
