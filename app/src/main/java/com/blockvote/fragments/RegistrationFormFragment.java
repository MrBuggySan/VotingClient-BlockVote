package com.blockvote.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.ElectionState;
import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.crypto.BlindedToken;
import com.blockvote.interfaces.DefaultInteractions;
import com.blockvote.model.MODEL_ElectionInfo;
import com.blockvote.model.MODEL_getRegistrarInfo;
import com.blockvote.networking.BlockVoteServerAPI;
import com.blockvote.networking.BlockVoteServerInstance;
import com.blockvote.votingclient.R;
import com.stepstone.stepper.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.spongycastle.crypto.params.RSAKeyParameters;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.blockvote.votingclient.R.id.register_registrar_spinner;

public abstract class RegistrationFormFragment extends Fragment implements Step {
    private final String LOG_TAG = RegistrationFormFragment.class.getSimpleName();
    protected View rootView;

    private DefaultInteractions defaultInteractions;
    private ElectionInstance electionInstance;
    protected boolean isReadyForNextStep;
    protected boolean hasValidElectionURL;

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

        electionInstance = new ElectionInstance();
        //make the children determine the components of the UI
        EditUI(defaultInteractions);
        isReadyForNextStep = false;
        hasValidElectionURL = false;
        return rootView;
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

    abstract void EditUI(DefaultInteractions defaultInteractions);
    abstract void stopLoadingAnimOnSuccess();
    abstract void stopLoadingAnimOnFail();

    private String respJSONStr;

    protected void getElectionInfo(final String electionURL){
        isReadyForNextStep = false;
        hasValidElectionURL = false;
        BlockVoteServerInstance blockVoteServerInstance = new BlockVoteServerInstance(electionURL);
        BlockVoteServerAPI apiService = blockVoteServerInstance.getAPI();
        Call<MODEL_ElectionInfo> call = apiService.getElectionInfo();

        call.enqueue(new Callback<MODEL_ElectionInfo>() {
            @Override
            public void onResponse(Call<MODEL_ElectionInfo> call, Response<MODEL_ElectionInfo> response) {
                int statusCode = response.code();

                if(statusCode != 200){
                    ToastWrapper.initiateToast(getContext(), "The election you entered is not available in BlockVote.");
                    return;
                }
                electionInstance.setElectionURL(electionURL);
                String temp_electionName = response.body().getResponse().getId();
                electionInstance.setElectionName(temp_electionName);
                //change the name of the toolbar
                defaultInteractions.changeTitleBarName(temp_electionName);

                districtList = response.body().getResponse().getElectionData().getDistricts();
                //download the registrar list
                hasValidElectionURL = true;
                getRegistrarInfo(electionURL);

            }

            @Override
            public void onFailure(Call<MODEL_ElectionInfo> call, Throwable t) {
                Log.e(LOG_TAG, "Downloading the district list has failed...");
//                throw new RuntimeException("Could not download the election list");
                ToastWrapper.initiateToast(getContext(), "The election you entered is not available in BlockVote.");
                stopLoadingAnimOnFail();
                return;
            }
        });

    }

    public void getRegistrarInfo(String electionURL){
        BlockVoteServerInstance blockVoteServerInstance = new BlockVoteServerInstance(electionURL);
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
                //TODO: stop the loading
                stopLoadingAnimOnSuccess();

                displayDistrictsonSpinner(districtList);
                Spinner spinner = (Spinner) getView().findViewById(R.id.register_districtspinner);

                //set up an event to change the registrarlist available when a district is chosen.
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        TextView textView = (TextView) selectedItemView;
                        String selectedDistrict = textView.getText().toString();
                        Log.v(LOG_TAG, "District selected : " + selectedDistrict);

                        ArrayList<String>  registrarList = new ArrayList<String>();
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

    private List<String> districtList;




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
        isReadyForNextStep = false;
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


    public void saveElectionInstance(){
        //TODO: this check can be taken out for the demo at the Olympic Oval.
        //TODO: check if the electionInstance is already in the list

        //setup the state of this electionInstance
        electionInstance.setElectionState(ElectionState.GEN_QR);

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
            ToastWrapper.initiateToast(getContext(), "Please enter your district and your registrar");
            return;
        }

        //save the registrar name and the district name
        electionInstance.setRegistrarName(registrarName);
        electionInstance.setDistrictName(districtName);

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

            if(keyModulus == null || keyExponent == null){
                Log.e(LOG_TAG, "The registrar was not found in the downloaded respJSONStr");
                return;
            }

            RSAKeyParameters rsaKeyParameters = new RSAKeyParameters(false,
                    new BigInteger(Base64.decode(keyModulus, Base64.DEFAULT)),
                    new BigInteger(Base64.decode(keyExponent, Base64.DEFAULT)));
            BlindedToken blindedToken = new BlindedToken( rsaKeyParameters );

            electionInstance.setBlindedToken(blindedToken);
            electionInstance.setrSAkeyParams(rsaKeyParameters);

            //TODO: Store the electionInstance inside dataStore
            //The key is the election's URL

            //mListener.onDistrictListNextInteraction( districtName, registrarName, keyModulus, keyExponent);
        }catch(JSONException e){
            Log.e(LOG_TAG, "Could not find the respJSONstr");
        }

        //We are now ready for the next step
        isReadyForNextStep = true;
    }

}
