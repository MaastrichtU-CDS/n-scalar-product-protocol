package com.florian.nscalarproduct.webservice.domain;

import com.florian.nscalarproduct.encryption.PublicElgamalKey;

public class GetSecretPartRequest extends NPartyRequest {
    private String serverId;
    private PublicElgamalKey publicKey;

    public GetSecretPartRequest() {
    }

    public PublicElgamalKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicElgamalKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
}
