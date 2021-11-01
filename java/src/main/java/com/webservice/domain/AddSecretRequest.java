package com.webservice.domain;

import java.util.List;

public class AddSecretRequest extends NPartyRequest {
    private int length;
    private List<String> serverIds;

    public AddSecretRequest() {
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
