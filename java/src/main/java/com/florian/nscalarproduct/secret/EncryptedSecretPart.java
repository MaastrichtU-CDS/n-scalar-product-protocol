package com.florian.nscalarproduct.secret;

import com.florian.nscalarproduct.encryption.Elgamal;
import com.florian.nscalarproduct.encryption.EncryptedElgamal;
import com.florian.nscalarproduct.encryption.PublicElgamalKey;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.Collectors;

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
        Arrays.stream(diagonalPart).parallel().map(x -> elgamal.encrypt(x))
                .collect(Collectors.toList()).toArray(diagonal);
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
