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

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
