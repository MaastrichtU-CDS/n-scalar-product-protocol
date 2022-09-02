package com.florian.nscalarproduct.webservice.domain;

import java.util.List;

public class AddSecretRequest extends NPartyRequest {
    private int length;
    private List<String> serverIds;
    private int precision;

    public AddSecretRequest() {
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<String> getServerIds() {
        return serverIds;
    }

    public void setServerIds(List<String> serverIds) {
        this.serverIds = serverIds;
    }
}
