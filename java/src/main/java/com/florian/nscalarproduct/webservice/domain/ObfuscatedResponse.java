package com.florian.nscalarproduct.webservice.domain;

import java.math.BigInteger;

public class ObfuscatedResponse {
    private BigInteger[] obfuscated;

    public ObfuscatedResponse() {
    }

    public BigInteger[] getObfuscated() {
        return obfuscated;
    }

    public void setObfuscated(BigInteger[] obfuscated) {
        this.obfuscated = obfuscated;
    }
}
