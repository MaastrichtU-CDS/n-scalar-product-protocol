package com.florian.nscalarproduct.webservice.domain;

import com.florian.nscalarproduct.secret.SecretPart;

public class SecretPartResponse {
    private SecretPart secretPart;

    public SecretPartResponse() {
    }

    public SecretPart getSecretPart() {
        return secretPart;
    }

    public void setSecretPart(SecretPart secretPart) {
        this.secretPart = secretPart;
    }
}
