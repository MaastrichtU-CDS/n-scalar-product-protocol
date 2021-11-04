package com.florian.nscalarproduct.webservice.domain;

import java.math.BigInteger;
import java.util.List;

public class LocalCalculationPartyRequest extends NPartyRequest {
    private List<BigInteger[]> obfuscated;

    public LocalCalculationPartyRequest() {
    }

    public List<BigInteger[]> getObfuscated() {
        return obfuscated;
    }

    public void setObfuscated(List<BigInteger[]> obfuscated) {
        this.obfuscated = obfuscated;
    }
}
