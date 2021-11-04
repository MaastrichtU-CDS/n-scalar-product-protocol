package com.florian.nscalarproduct.webservice.domain;

public class RetrieveSecretRequest extends NPartyRequest {
    private String source;

    public RetrieveSecretRequest() {
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
