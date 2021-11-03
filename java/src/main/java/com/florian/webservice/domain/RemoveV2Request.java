package com.florian.webservice.domain;

import java.math.BigInteger;

public class RemoveV2Request extends NPartyRequest {
    private BigInteger partial;

    public RemoveV2Request() {
    }

    public BigInteger getPartial() {
        return partial;
    }

    public void setPartial(BigInteger partial) {
        this.partial = partial;
    }
}
