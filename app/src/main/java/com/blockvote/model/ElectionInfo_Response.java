package com.blockvote.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andreibuiza on 23/01/17.
 */

public class ElectionInfo_Response   {
    private String id;

    private ElectionInfo_ElectionData electionData;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public ElectionInfo_ElectionData getElectionData() {
        return electionData;
    }

    public void setElectionData(ElectionInfo_ElectionData electionData) {
        this.electionData = electionData;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
