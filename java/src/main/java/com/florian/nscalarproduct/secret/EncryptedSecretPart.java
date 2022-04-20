package com.florian.nscalarproduct.secret;

import com.florian.nscalarproduct.encryption.AES;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.Collectors;

public class EncryptedSecretPart {
    private String[] diagonal;
    private String r;
    private String id;

    public EncryptedSecretPart() {
    }

    public EncryptedSecretPart(SecretPart part, AES aes) {
        this.id = part.getId();
        this.r = aes.encrypt(part.getR());
        diagonal = new String[part.getDiagonal().length];
        BigInteger[] diagonalPart = part.getDiagonal();
        Arrays.stream(diagonalPart).map(x -> aes.encrypt(x))
                .collect(Collectors.toList()).toArray(diagonal);
    }

    public String[] getDiagonal() {
        return diagonal;
    }

    public void setDiagonal(String[] diagonal) {
        this.diagonal = diagonal;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
