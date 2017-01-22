package com.blockvote.votingclient;

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
import android.widget.Toast;

import com.blockvote.model.FullElectionInfoModel;
import com.blockvote.networking.BlockVoteServerAPI;
import com.blockvote.networking.BlockVoteServerInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.

 */
public class RegisterFragment extends Fragment {
    private final String LOG_TAG = RegisterFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        //TODO: get the name of the user from the system, have an option to edit the values

        //Hide the other views
        rootView.findViewById(R.id.reg_firstNameText).setVisibility(View.GONE);
        rootView.findViewById(R.id.reg_lastNameText).setVisibility(View.GONE);
        rootView.findViewById(R.id.register_districtspinner).setVisibility(View.GONE);
        rootView.findViewById(R.id.reg_register_button).setVisibility(View.GONE);

        //get the districts from the server
        BlockVoteServerInstance blockVoteServerInstance = new BlockVoteServerInstance();
        BlockVoteServerAPI apiService = blockVoteServerInstance.getAPI();
        Call<FullElectionInfoModel> call = apiService.getElectionInfo();

        call.enqueue(new Callback<FullElectionInfoModel>() {
            @Override
            public void onResponse(Call<FullElectionInfoModel> call, Response<FullElectionInfoModel> response) {
                int statusCode = response.code();
                List<String> districtList = response.body().getElectionData().getDistricts();
                //apply the results to the UI
                View rootView_ = getView();

                //Setup the spinner
                Spinner spinner = (Spinner) rootView_.findViewById(R.id.register_districtspinner);
                ArrayAdapter<String> mDistrictList = new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.listentry,
                        R.id.listEntry,
                        new ArrayList<String>()
                );

                for(int i = 0; i < districtList.size(); i++){
                    Log.d(LOG_TAG, districtList.get(i) + " available.");
                    mDistrictList.add(districtList.get(i));
                }
                spinner.setAdapter(mDistrictList);


                //Setup the button
                Button registerButton = (Button) rootView_.findViewById(R.id.reg_register_button);
                //Register button callback
                registerButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (mListener != null) {
                            View rootView = getView();
                            EditText firstText= (EditText) rootView.findViewById(R.id.reg_firstNameText);
                            EditText lastText= (EditText) rootView.findViewById(R.id.reg_lastNameText);

                            String firstName = firstText.getText().toString();
                            String lastName = lastText.getText().toString();
                            if(firstName.equals("") || lastName.equals("")){
                                Context context = getContext();
                                int duration = Toast.LENGTH_SHORT;
                                Toast.makeText(context, "Please enter your name", duration).show();

                                return;
                            }
                            Spinner districtSpinner = (Spinner) rootView.findViewById(R.id.register_districtspinner);
                            String districtName = districtSpinner.getSelectedItem().toString();

                            mListener.onRegisterButtonInteraction(firstName, lastName, districtName);
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

            @Override
            public void onFailure(Call<FullElectionInfoModel> call, Throwable t) {
                Log.e(LOG_TAG,"Downloading the election list has failed...");
                throw new RuntimeException("Could not download the election list");
                //TODO:Restart the connection if failure
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
        void onRegisterButtonInteraction(String firstName, String lastName, String districtName);
    }
}
