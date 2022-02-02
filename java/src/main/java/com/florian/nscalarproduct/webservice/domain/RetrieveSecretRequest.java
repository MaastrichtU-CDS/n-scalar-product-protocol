package com.florian.nscalarproduct.webservice.domain;

import com.florian.nscalarproduct.encryption.PublicElgamalKey;

public class RetrieveSecretRequest extends NPartyRequest {
    private String source;
    private PublicElgamalKey publicKey;

    public RetrieveSecretRequest() {
    }

    public PublicElgamalKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicElgamalKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
