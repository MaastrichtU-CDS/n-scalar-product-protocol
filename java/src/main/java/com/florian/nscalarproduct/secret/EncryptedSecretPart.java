package com.florian.nscalarproduct.secret;

import com.florian.nscalarproduct.encryption.Elgamal;
import com.florian.nscalarproduct.encryption.EncryptedElgamal;
import com.florian.nscalarproduct.encryption.PublicElgamalKey;

import java.math.BigInteger;

public class EncryptedSecretPart {
    private EncryptedElgamal[] diagonal;
    private EncryptedElgamal r;
    private String id;

    public EncryptedSecretPart() {
    }

    public EncryptedSecretPart(SecretPart part, PublicElgamalKey publicKey) {
        Elgamal elgamal = new Elgamal(publicKey);

        this.id = part.getId();
        this.r = elgamal.encrypt(part.getR());
        diagonal = new EncryptedElgamal[part.getDiagonal().length];
        BigInteger[] diagonalPart = part.getDiagonal();
        for (int i = 0; i < diagonalPart.length; i++) {
            diagonal[i] = elgamal.encrypt(part.getDiagonal()[i]);
        }
    }

    public EncryptedElgamal[] getDiagonal() {
        return diagonal;
    }

    public void setDiagonal(EncryptedElgamal[] diagonal) {
        this.diagonal = diagonal;
    }

    public EncryptedElgamal getR() {
        return r;
    }

    public void setR(EncryptedElgamal r) {
        this.r = r;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
