package com.florian.nscalarproduct.webservice.domain;

import java.security.PublicKey;

public class GetSecretPartRequest extends NPartyRequest {
    private String serverId;
    private PublicKey rsaKey;

    public GetSecretPartRequest() {
    }


    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public PublicKey getRsaKey() {
        return rsaKey;
    }

    public void setRsaKey(PublicKey rsaKey) {
        this.rsaKey = rsaKey;
    }
}
