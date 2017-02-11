package com.blockvote.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andreibuiza on 23/01/17.
 */

public class MODEL_writeVote {

    private writeVote_response response;
    private ErrorResponseModel error;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public writeVote_response getResponse() {
        return response;
    }

    public void setResponse(writeVote_response response) {
        this.response = response;
    }

    public ErrorResponseModel getError() {
        return error;
    }

    public void setError(ErrorResponseModel error) {
        this.error = error;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
