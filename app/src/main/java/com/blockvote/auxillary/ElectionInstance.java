package com.blockvote.auxillary;

import android.graphics.Bitmap;

import com.blockvote.crypto.BlindedToken;

import org.spongycastle.crypto.params.RSAKeyParameters;

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
    private BlindedToken blindedToken;
    private RSAKeyParameters rSAkeyParams;
    private String signedTokenID;
    private String signedTokenSignature;

    private Bitmap QR_code;

    public Bitmap getQR_code() {
        return QR_code;
    }

    public void setQR_code(Bitmap QR_code) {
        this.QR_code = QR_code;
    }

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

    public BlindedToken getBlindedToken() {
        return blindedToken;
    }

    public void setBlindedToken(BlindedToken blindedToken) {
        this.blindedToken = blindedToken;
    }

    public RSAKeyParameters getrSAkeyParams() {
        return rSAkeyParams;
    }

    public void setrSAkeyParams(RSAKeyParameters rSAkeyParams) {
        this.rSAkeyParams = rSAkeyParams;
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
