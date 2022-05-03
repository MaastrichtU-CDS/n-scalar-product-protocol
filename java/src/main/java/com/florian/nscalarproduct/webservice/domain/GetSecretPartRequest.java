package com.florian.nscalarproduct.webservice.domain;

public class GetSecretPartRequest extends NPartyRequest {
    private String serverId;
    private byte[] rsaKey;

    public GetSecretPartRequest() {
    }


    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public byte[] getRsaKey() {
        return rsaKey;
    }

    public void setRsaKey(byte[] rsaKey) {
        this.rsaKey = rsaKey;
    }
}
