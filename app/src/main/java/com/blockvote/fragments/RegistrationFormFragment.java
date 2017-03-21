package com.blockvote.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.blockvote.auxillary.ElectionInstance;
import com.blockvote.auxillary.ElectionState;
import com.blockvote.auxillary.ToastWrapper;
import com.blockvote.crypto.BlindedToken;
import com.blockvote.interfaces.RegistrationDefaultInteractions;
import com.blockvote.model.ElectionInfo_ElectionData;
import com.blockvote.model.ElectionInfo_Response;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RegistrationFormFragment extends Fragment implements Step {
    private final String LOG_TAG = RegistrationFormFragment.class.getSimpleName();
    protected View rootView;

    protected RegistrationDefaultInteractions registrationDefaultInteractions;
    protected ElectionInstance electionInstance;
    protected boolean isReadyForNextStep;
    protected boolean hasValidElectionURL;
    protected boolean skipChecks;

    public RegistrationFormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
//        Log.d(LOG_TAG, "onDestroyView called.");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
//        Log.d(LOG_TAG, "onDestroy called.");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_registration_form, container, false);
        if(rootView == null){
            Log.d(LOG_TAG, "rootView is not supposed to be null");
        }
        Log.d(LOG_TAG, "onCreate called.");
        //Determine if a new electionInstance is to be made or not
        electionInstance = registrationDefaultInteractions.getElectionInstance();
        if(electionInstance != null){
            stopLoadingAnimOnSuccess();
            prefillEditableViews(electionInstance);
            disableEditableViews();
//            isReadyForNextStep = true;
//            hasValidElectionURL = true;
            skipChecks = true;

        }else{
            skipChecks = false;
            electionInstance = new ElectionInstance();
            //make the children determine the components of the UI that will show up
            EditUI();
            isReadyForNextStep = false;
            hasValidElectionURL = false;
        }



        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegistrationDefaultInteractions) {
            registrationDefaultInteractions = (RegistrationDefaultInteractions) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DefaultInteractions");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        registrationDefaultInteractions = null;
    }

    abstract void EditUI();
    abstract void stopLoadingAnimOnSuccess();
    abstract void stopLoadingAnimOnFail();
    abstract void disableEditableViews();
    abstract void prefillEditableViews(ElectionInstance electionInstance);
    abstract void registarSpinnerSetup();

    private String respJSONStr;

    protected void PingThenGetElectionInfo(final String electionURL){
        BlockVoteServerInstance blockVoteServerInstance = new BlockVoteServerInstance(electionURL);
        BlockVoteServerAPI apiService = blockVoteServerInstance.getAPI();
        Call<ResponseBody> call = apiService.getAuthPing();
        call.enqueue((new Callback<ResponseBody>() {

            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               if(response.code() != 200){
                   ToastWrapper.initiateToast(getContext(), "The election you entered is not available in BlockVote.");
                   stopLoadingAnimOnFail();
                   return;
               }
               getElectionInfo(electionURL);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ToastWrapper.initiateToast(getContext(), "Either you entered the wrong URL or" +
                        " the Election is not available at this time. ");
                stopLoadingAnimOnFail();
            }
        }));


    }

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
                    stopLoadingAnimOnFail();
                    return;
                }
                electionInstance.setElectionURL(electionURL);
                ElectionInfo_Response electionInfo_response = response.body().getResponse();
                ElectionInfo_ElectionData electionInfo_electionData = electionInfo_response.getElectionData();
                String temp_electionName = electionInfo_response.getId();
                electionInstance.setElectionName(temp_electionName);

                electionInstance.setStartTime(electionInfo_electionData.getElectionStart());
                electionInstance.setEndTime(electionInfo_electionData.getElectionEnd());

                electionInstance.setElectionQuestion(electionInfo_electionData.getElectionQuestion());
                electionInstance.setElectionFlagURL(electionInfo_electionData.getElectionFlagURL());
                electionInstance.setDistrictAlias(electionInfo_electionData.getDistrictAlias());

                if(electionInfo_electionData.getLiveResults().equals("yes")){
                    electionInstance.setLiveResults(true);
                }else{
                    electionInstance.setLiveResults(false);
                }
                electionInstance.setVoteOptions(electionInfo_electionData.getVoteOptions());

                //change the name of the toolbar
                registrationDefaultInteractions.changeTitleBarName(temp_electionName);

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

    protected ArrayList<JSONObject> registrarInfoList;

    public void getRegistrarInfo(String electionURL){
        BlockVoteServerInstance blockVoteServerInstance = new BlockVoteServerInstance(electionURL);
        BlockVoteServerAPI apiService = blockVoteServerInstance.getAPI();
        Call<MODEL_getRegistrarInfo> call = apiService.getRegistrarInfo();

        call.enqueue(new Callback<MODEL_getRegistrarInfo>() {
            @Override
            public void onResponse(Call<MODEL_getRegistrarInfo> call, Response<MODEL_getRegistrarInfo> response) {
                respJSONStr = response.body().getResponse();
                if(respJSONStr == null){
                    Log.e(LOG_TAG, "failed to get the registrar JSON string");
                    ToastWrapper.initiateToast(getContext(), "failed to get the registrar JSON string");
                }


                displayDistrictsonSpinner(districtList);


                registrarInfoList = new ArrayList<JSONObject>();

                //get the JSON info of each registrar
                try{
                    JSONArray regisListJSONstr = new JSONArray(respJSONStr);
                    for(int i = 0 ; i < regisListJSONstr.length(); i++){
                        JSONObject registrarInfo = regisListJSONstr.getJSONObject(i).getJSONObject("Registrar");
                        registrarInfoList.add(registrarInfo);
                    }
                }catch(JSONException e){
                    Log.e(LOG_TAG, "could not find the JSONObject inside regisListJSONstr");
                }

                registarSpinnerSetup();
                //stop the loading & setup the specifics
                stopLoadingAnimOnSuccess();

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
        Spinner spinner = (Spinner) rootView.findViewById(R.id.register_districtspinner);
        spinner.setAdapter(mDistrictList);

        rootView.findViewById(R.id.regform_UI).setVisibility(View.VISIBLE);

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
        Spinner spinner = (Spinner) rootView.findViewById(R.id.register_registrar_spinner);
        spinner.setAdapter(mRegistrarList);
    }


    public void saveElectionInstance(){

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

            //The user should not be able to edit their data anymore
            disableEditableViews();
            //Add the electionInstance to RegistrationActivity
            if(registrationDefaultInteractions.saveNewElectionInstance(electionInstance)){
                //setup the state of this electionInstance
                registrationDefaultInteractions.updateElectionInstanceState(ElectionState.START_GEN_QR);
                // skipChecks only if saveNewElectionInstance is true.
                skipChecks = true;
            }else{
                ToastWrapper.initiateToast(getContext(), "This election is already active.");
            }

        }catch(JSONException e){
            Log.e(LOG_TAG, "Could not find the respJSONstr");
        }

        //We are now ready for the next step
        isReadyForNextStep = true;
    }

}
