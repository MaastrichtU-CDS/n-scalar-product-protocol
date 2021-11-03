package com.florian.webservice.domain;

public class AddDataStationRequest extends NPartyRequest {
    private String source;

    public AddDataStationRequest() {
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
