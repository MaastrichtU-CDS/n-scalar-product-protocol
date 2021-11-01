package com.webservice.domain;

import java.math.BigInteger;
import java.util.List;

public class LocalCalculationPartyRequest extends NPartyRequest {
    private List<BigInteger[]> Obfuscated;

    public LocalCalculationPartyRequest() {
    }

    public List<BigInteger[]> getObfuscated() {
        return Obfuscated;
    }

    public void setObfuscated(List<BigInteger[]> obfuscated) {
        Obfuscated = obfuscated;
    }
}
