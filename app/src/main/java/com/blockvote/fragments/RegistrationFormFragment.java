package com.blockvote.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.model.MODEL_ElectionInfo;
import com.blockvote.model.MODEL_getRegistrarInfo;
import com.blockvote.networking.BlockVoteServerAPI;
import com.blockvote.networking.BlockVoteServerInstance;
import com.blockvote.votingclient.R;
import com.stepstone.stepper.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.blockvote.votingclient.R.id.register_registrar_spinner;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrationFormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.

 */
public abstract class RegistrationFormFragment extends Fragment implements Step {
    private final String LOG_TAG = RegistrationFormFragment.class.getSimpleName();
    protected View rootView;

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
        rootView = inflater.inflate(R.layout.fragment_registration_form, container, false);

        //Hide the registration form
        rootView.findViewById(R.id.regform_UI).setVisibility(View.GONE);

        //TODO: make the child classes determine the components of the UI
        EditUI();

        //get the districts from the server
        //getElectionInfo();
        //get the registrars from the server
        //getRegistrarInfo();
        return rootView;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    abstract void EditUI();

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
        void onDistrictListNextInteraction( String districtName,
                                           String registrarName,String keyModulus, String keyExponent);
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

        getView().findViewById(R.id.regform_UI).setVisibility(View.VISIBLE);


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
        Spinner spinner = (Spinner) getView().findViewById(register_registrar_spinner);
        spinner.setAdapter(mRegistrarList);
    }

    public void onNextClick(){
        View rootView = getView();

        //test if the user has selected a district and a registrar.

        Spinner districtSpinner = (Spinner) rootView.findViewById(R.id.register_districtspinner);
        Spinner registrarSpinner = (Spinner) rootView.findViewById(R.id.register_registrar_spinner);

        String districtName = districtSpinner.getSelectedItem().toString();

        if(registrarSpinner.getSelectedItem() == null ){
            ToastWrapper.initiateToast(getContext(), "Please enter your registrar");
            return;
        }
        String registrarName = registrarSpinner.getSelectedItem().toString();

        //TODO: fix this error checking
        if (registrarName == null || districtName == null) {
            ToastWrapper.initiateToast(getContext(), "Please your district and your registrar");
            return;
        }

        //extract the registrar key's modulus and exponent
        try{
            JSONArray regisListJSONstr = new JSONArray(respJSONStr);
            String keyModulus = null;
            String keyExponent = null;
            String regName ="";
            for(int i = 0 ; i < regisListJSONstr.length(); i++){
                JSONObject registrarInfo = regisListJSONstr.getJSONObject(i).getJSONObject("Registrar");
                regName = registrarInfo.getString("RegistrarName");
                if( regName.equals(registrarName)){
                    keyModulus = registrarInfo.getString("KeyModulus");
                    keyExponent = registrarInfo.getString("KeyExponent");

                }
            }
//            Log.v(LOG_TAG, regName + " is the selected registrar");
//            Log.v(LOG_TAG, keyModulus);
//            Log.v(LOG_TAG, keyExponent);
            //TODO: fix this error checking
            if(keyModulus == null || keyExponent == null){
                Log.e(LOG_TAG, "The registrar was not found in the downloaded respJSONStr");
            }
            mListener.onDistrictListNextInteraction( districtName, registrarName, keyModulus, keyExponent);
        }catch(JSONException e){
            Log.e(LOG_TAG, "Could not find the respJSONstr");
        }


    }

    private String respJSONStr;

    public void getRegistrarInfo(){
        BlockVoteServerInstance blockVoteServerInstance = new BlockVoteServerInstance();
        BlockVoteServerAPI apiService = blockVoteServerInstance.getAPI();
        Call<MODEL_getRegistrarInfo> call = apiService.getRegistrarInfo();

        call.enqueue(new Callback<MODEL_getRegistrarInfo>() {
            @Override
            public void onResponse(Call<MODEL_getRegistrarInfo> call, Response<MODEL_getRegistrarInfo> response) {
                respJSONStr = response.body().getResponse();
                if(respJSONStr.equals(null)){
                    Log.e(LOG_TAG, "failed to get the registrar JSON string");
                    ToastWrapper.initiateToast(getContext(), "failed to get the registrar JSON string");
                }
                SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
                editor.putString(getString(R.string.registrarListKey),respJSONStr );
                editor.commit();
                Spinner spinner = (Spinner) getView().findViewById(R.id.register_districtspinner);


                //set up an event to change the registrarlist available when a district is chosen.
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        TextView textView = (TextView) selectedItemView;
                        String selectedDistrict = textView.getText().toString();
                        Log.v(LOG_TAG, "District selected : " + selectedDistrict);

                        ArrayList<String> registrarList = new ArrayList<String>();
                        try{
                            JSONArray regisListJSONstr = new JSONArray(respJSONStr);
                            for(int i = 0 ; i < regisListJSONstr.length(); i++){
                                JSONObject registrarInfo = regisListJSONstr.getJSONObject(i).getJSONObject("Registrar");
                                if(registrarInfo.getString("RegistrationDistrict").equals(selectedDistrict)){
                                    String registrarName = registrarInfo.getString("RegistrarName");
                                    Log.v(LOG_TAG, registrarName + " is a registrar in " + selectedDistrict);
                                    registrarList.add(registrarName);
                                }
                            }
                            displayRegistrarSpinner(registrarList);

                        }catch(JSONException e){
                            Log.e(LOG_TAG, "could not find the JSONObject inside regisListJSONstr");
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {

                    }

                });
            }

            @Override
            public void onFailure(Call<MODEL_getRegistrarInfo> call, Throwable t) {
                Log.e(LOG_TAG, "Downloading the registrar information has failed...");
                throw new RuntimeException("Could not download the registrar list");
                //TODO:Restart the connection if failure
            }
        });
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
//                    Button registerButton = (Button) rootView_.findViewById(R.id.reg_register_button);
//                    //Register button callback
//                    registerButton.setOnClickListener(new View.OnClickListener() {
//                        public void onClick(View v) {
//                            if (mListener != null) {
//                                onNextClick();
//                            }
//                        }
//                    });

                    //disable the loading screen
                    rootView_.findViewById(R.id.registration_loadingPanel).setVisibility(View.GONE);



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
