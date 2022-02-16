package com.florian.nscalarproduct.webservice.domain;

import com.florian.nscalarproduct.secret.EncryptedSecretPart;

public class SecretPartResponse {
    private EncryptedSecretPart secretPartAES;
    private byte[] aESkey;

    public SecretPartResponse() {
    }

    public EncryptedSecretPart getSecretPartAES() {
        return secretPartAES;
    }

    public void setSecretPartAES(EncryptedSecretPart secretPartAES) {
        this.secretPartAES = secretPartAES;
    }

    public byte[] getAESkey() {
        return aESkey;
    }

    public void setAESkey(byte[] aESkey) {
        this.aESkey = aESkey;
    }
}
