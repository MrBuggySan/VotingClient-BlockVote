package com.blockvote.auxillary;

/**
 * Created by Beast Mode on 3/11/2017.
 */

public class ElectionInstance {
    private ElectionState electionState;
    private String electionName;
    private String electionURL;

    private String districtName;
    private String registrarName;

    //TODO: determine which ones I can cache, for now I cache all of them
    private String keyExponent;
    private String keyModulus;
    private String jsonBlindedToken;
    private String jsonRSAkeyParams;
    private String signedTokenID;
    private String signedTokenSignature;

    public ElectionInstance(){

    }

    public ElectionState getElectionState() {
        return electionState;
    }

    public void setElectionState(ElectionState electionState) {
        this.electionState = electionState;
    }

    public String getElectionName() {
        return electionName;
    }

    public void setElectionName(String electionName) {
        this.electionName = electionName;
    }

    public String getElectionURL() {
        return electionURL;
    }

    public void setElectionURL(String electionURL) {
        this.electionURL = electionURL;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getRegistrarName() {
        return registrarName;
    }

    public void setRegistrarName(String registrarName) {
        this.registrarName = registrarName;
    }

    public String getKeyExponent() {
        return keyExponent;
    }

    public void setKeyExponent(String keyExponent) {
        this.keyExponent = keyExponent;
    }

    public String getKeyModulus() {
        return keyModulus;
    }

    public void setKeyModulus(String keyModulus) {
        this.keyModulus = keyModulus;
    }

    public String getJsonBlindedToken() {
        return jsonBlindedToken;
    }

    public void setJsonBlindedToken(String jsonBlindedToken) {
        this.jsonBlindedToken = jsonBlindedToken;
    }

    public String getJsonRSAkeyParams() {
        return jsonRSAkeyParams;
    }

    public void setJsonRSAkeyParams(String jsonRSAkeyParams) {
        this.jsonRSAkeyParams = jsonRSAkeyParams;
    }

    public String getSignedTokenID() {
        return signedTokenID;
    }

    public void setSignedTokenID(String signedTokenID) {
        this.signedTokenID = signedTokenID;
    }

    public String getSignedTokenSignature() {
        return signedTokenSignature;
    }

    public void setSignedTokenSignature(String signedTokenSignature) {
        this.signedTokenSignature = signedTokenSignature;
    }
}
