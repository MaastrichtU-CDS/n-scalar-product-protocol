package com.florian.nscalarproduct.secret;

import java.math.BigInteger;

public class SecretPart {
    private BigInteger[] diagonal;
    private BigInteger r;
    private String id;

    public SecretPart() {
    }

    public SecretPart(BigInteger[] diagonal, BigInteger r, String id) {
        this.diagonal = diagonal;
        this.r = r;
        this.id = id;
    }

    public void setDiagonal(BigInteger[] diagonal) {
        this.diagonal = diagonal;
    }

    public void setR(BigInteger r) {
        this.r = r;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public BigInteger[] getDiagonal() {
        return diagonal;
    }

    public BigInteger getR() {
        return r;
    }
}
