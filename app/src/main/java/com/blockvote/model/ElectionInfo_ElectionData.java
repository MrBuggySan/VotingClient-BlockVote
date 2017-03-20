package com.blockvote.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Beast Mode on 1/21/2017.
 */

public class ElectionInfo_ElectionData {
    private List<String> districts = null;
    private List<String> voteOptions = null;
    private String electionStart;
    private String electionEnd;
    private String liveResults;
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

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<String> getDistricts() {
        return districts;
    }

    public void setDistricts(List<String> districts) {
        this.districts = districts;
    }

    public List<String> getVoteOptions() {
        return voteOptions;
    }

    public void setVoteOptions(List<String> voteOptions) {
        this.voteOptions = voteOptions;
    }

    public String getElectionStart() {
        return electionStart;
    }

    public void setElectionStart(String electionStart) {
        this.electionStart = electionStart;
    }

    public String getElectionEnd() {
        return electionEnd;
    }

    public void setElectionEnd(String electionEnd) {
        this.electionEnd = electionEnd;
    }

    public String getLiveResults() {
        return liveResults;
    }

    public void setLiveResults(String liveResults) {
        this.liveResults = liveResults;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
