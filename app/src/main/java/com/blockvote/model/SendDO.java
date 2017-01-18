package com.blockvote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by andreibuiza on 17/01/17.
 */

public class SendDO {
    @SerializedName("testString")
    @Expose
    String testString;

    public SendDO(String temp){
        this.testString = temp;

    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }


}
