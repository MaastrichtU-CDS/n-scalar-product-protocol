package com.florian.nscalarproduct.webservice.domain;

import com.florian.nscalarproduct.secret.EncryptedSecretPart;

public class SecretPartResponse {
    private EncryptedSecretPart secretPart;

    public SecretPartResponse() {
    }

    public EncryptedSecretPart getSecretPart() {
        return secretPart;
    }

    public void setSecretPart(EncryptedSecretPart secretPart) {
        this.secretPart = secretPart;
    }
}
