package com.blockvote.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Beast Mode on 2/12/2017.
 */

public class MODEL_getRegistrarInfo {

        private String response;
        private Object error;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
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
