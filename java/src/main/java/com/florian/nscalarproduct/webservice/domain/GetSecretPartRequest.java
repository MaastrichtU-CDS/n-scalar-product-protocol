package com.florian.nscalarproduct.webservice.domain;

import com.florian.nscalarproduct.encryption.PublicElgamalKey;

import java.security.PublicKey;

public class GetSecretPartRequest extends NPartyRequest {
    private String serverId;
    private PublicElgamalKey publicKey;
    private PublicKey rsaKey;

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

    public PublicKey getRsaKey() {
        return rsaKey;
    }

    public void setRsaKey(PublicKey rsaKey) {
        this.rsaKey = rsaKey;
    }
}
