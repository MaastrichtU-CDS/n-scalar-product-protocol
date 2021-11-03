package com.florian.webservice.domain;

import com.florian.secret.SecretPart;

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
