package com.blockvote.model;

/**
 * Created by Beast Mode on 3/25/2017.
 */

public class MODEL_readVote {
    private String response;
    private ErrorResponseModel error;

    public String getResponse() {
        return response;
    }

    public void setResponse(String respone) {
        this.response = respone;
    }

    public ErrorResponseModel getError() {
        return error;
    }

    public void setError(ErrorResponseModel error) {
        this.error = error;
    }
}
