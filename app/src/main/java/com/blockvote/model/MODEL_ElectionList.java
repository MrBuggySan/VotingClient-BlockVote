package com.blockvote.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Beast Mode on 1/21/2017.
 */

public class MODEL_ElectionList {

    private List<String> response = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<String> getResponse() {
        return response;
    }

    public void setResponse(List<String> response) {
        this.response = response;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
