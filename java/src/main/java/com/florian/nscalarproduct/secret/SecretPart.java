package com.florian.nscalarproduct.secret;

import com.florian.nscalarproduct.encryption.Elgamal;
import com.florian.nscalarproduct.encryption.EncryptedElgamal;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SecretPart {
    private BigInteger[] diagonal;
    private BigInteger r;
    private String id;

    public SecretPart() {
    }

    public SecretPart(EncryptedSecretPart secretPart, Elgamal elgamal) {
        this.id = secretPart.getId();
        this.r = elgamal.decrypt(secretPart.getR());
        EncryptedElgamal[] encryptdDiagonal = secretPart.getDiagonal();
        diagonal = new BigInteger[encryptdDiagonal.length];
        Arrays.stream(encryptdDiagonal).parallel().map(x -> elgamal.decrypt(x))
                .collect(Collectors.toList()).toArray(diagonal);
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
