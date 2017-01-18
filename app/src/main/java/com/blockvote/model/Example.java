package com.blockvote.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andreibuiza on 17/01/17.
 */

public class Example {

    private String testString;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}