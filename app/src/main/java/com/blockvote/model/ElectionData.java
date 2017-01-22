package com.blockvote.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Beast Mode on 1/21/2017.
 */

public class ElectionData {
    private String chaincodeID;
    private List<String> districts = null;
    private List<String> answers = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getChaincodeID() {
        return chaincodeID;
    }

    public void setChaincodeID(String chaincodeID) {
        this.chaincodeID = chaincodeID;
    }

    public List<String> getDistricts() {
        return districts;
    }

    public void setDistricts(List<String> districts) {
        this.districts = districts;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
