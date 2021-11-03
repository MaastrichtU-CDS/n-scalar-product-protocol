package com.florian.webservice.domain;

public class GetSecretPartRequest extends NPartyRequest {
    private String serverId;

    public GetSecretPartRequest() {
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
}
