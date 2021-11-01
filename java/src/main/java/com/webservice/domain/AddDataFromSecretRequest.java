package com.webservice.domain;

import java.util.List;

public class AddDataFromSecretRequest extends AddDataStationRequest {
    private List<String> subset;

    public AddDataFromSecretRequest() {
    }


    public List<String> getSubset() {
        return subset;
    }

    public void setSubset(List<String> subset) {
        this.subset = subset;
    }
}
