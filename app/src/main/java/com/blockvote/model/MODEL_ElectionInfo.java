package com.blockvote.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Beast Mode on 1/21/2017.
 */

public class MODEL_ElectionInfo {
    private ElectionInfo_Response response;
    private Object error;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public ElectionInfo_Response getResponse() {
        return response;
    }

    public void setResponse(ElectionInfo_Response response) {
        this.response = response;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
