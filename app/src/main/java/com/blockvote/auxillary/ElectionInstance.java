package com.blockvote.auxillary;

import com.blockvote.crypto.BlindedToken;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.ISODateTimeFormat;
import org.spongycastle.crypto.params.RSAKeyParameters;

import java.util.List;

/**
 * Created by Beast Mode on 3/11/2017.
 */

public class ElectionInstance {
    private int id;
    private boolean isOpenForVoting;
    private boolean hasEnded;
    private ElectionState electionState;
    private String electionName;
    private String electionURL;

    private String timeString;
    private String startTime;
    private String endTime;

    private String districtName;
    private String registrarName;

    //TODO: determine which ones I can cache, for now I cache all of them
    private BlindedToken blindedToken;
    private RSAKeyParameters rSAkeyParams;
    private byte[] signedTokenID;
    private byte[] signedTokenSignature;
    private static int count = 0;

    private boolean liveResultsAvailable;

    private List<String> voteOptions;

    private String electionQuestion;
    private String electionFlagURL;
    private String districtAlias;

    public String getElectionQuestion() {
        return electionQuestion;
    }

    public void setElectionQuestion(String electionQuestion) {
        this.electionQuestion = electionQuestion;
    }

    public String getElectionFlagURL() {
        return electionFlagURL;
    }

    public void setElectionFlagURL(String electionFlagURL) {
        this.electionFlagURL = electionFlagURL;
    }

    public String getDistrictAlias() {
        return districtAlias;
    }

    public void setDistrictAlias(String districtAlias) {
        this.districtAlias = districtAlias;
    }

    public boolean liveResultsAvailable() {
        return liveResultsAvailable;
    }

    public void setLiveResults(boolean liveResults) {
        this.liveResultsAvailable = liveResults;
    }

    public List<String> getVoteOptions() {
        return voteOptions;
    }

    public void setVoteOptions(List<String> voteOptions) {
        this.voteOptions = voteOptions;
    }

    public static int currentCount(){
        return count++;
    }

    public boolean isOpenForVoting() {
        return isOpenForVoting;
    }



    public boolean isHasEnded() {
        return hasEnded;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void calculateTimeToElection(){
        //TODO: clean this up to cover more scenarios.
        isOpenForVoting = false;
        hasEnded = false;
        //Get the UTC time now
        DateTime timeNow = DateTime.now();
        DateTime electionStart = ISODateTimeFormat.dateTimeParser().parseDateTime(startTime);
        DateTime electionEnd = ISODateTimeFormat.dateTimeParser().parseDateTime(endTime);

        Duration durBeforeStart = new Duration(timeNow, electionStart);
        if(durBeforeStart.getStandardSeconds() > 0 ){
            long days = durBeforeStart.getStandardDays();
            long hours = durBeforeStart.getStandardHours() % 24;
            //election has not started yet
            if(days == 0){
                timeString = "Opens in " + hours + " hours.";
            }else{
                timeString = "Opens in " + days + " days and " + hours + " hours";
            }
        }else{
            Duration durBeforeEnd = new Duration(timeNow, electionEnd);
            if(durBeforeEnd.getStandardSeconds() > 0){
                //The election is happening now
                isOpenForVoting = true;
                long days = durBeforeEnd.getStandardDays();
                long hours = durBeforeEnd.getStandardHours() % 24;
                //election has not started yet
                if(days == 0){
                    timeString = "Voting ends in " + hours + " hours.";
                }else{
                    timeString = "Voting ends in " + days + " days and " + hours + " hours";
                }
            }else{
                //The election has ended
                //TODO: deal with this!
                isOpenForVoting = false;
                hasEnded = true;
            }
        }


    }

    public String getTimeString() {
        calculateTimeToElection();
        return timeString;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public ElectionInstance(){

    }

    public ElectionState getElectionState() {
        return electionState;
    }

    public void setElectionState(ElectionState electionState) {
        this.electionState = electionState;
    }

    public String getElectionNameRaw(){
        return electionName;
    }

    public String getElectionName() {

        if (HACKVERSION.forDemo) {
            return electionName + " " + id;
        } else {
            return electionName;
        }
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

    public byte[] getSignedTokenID() {
        return signedTokenID;
    }

    public void setSignedTokenID(byte[] signedTokenID) {
        this.signedTokenID = signedTokenID;
    }

    public byte[] getSignedTokenSignature() {
        return signedTokenSignature;
    }

    public void setSignedTokenSignature(byte[] signedTokenSignature) {
        this.signedTokenSignature = signedTokenSignature;
    }
}
