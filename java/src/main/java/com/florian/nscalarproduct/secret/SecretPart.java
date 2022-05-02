package com.florian.nscalarproduct.secret;

import com.florian.nscalarproduct.encryption.AES;
import com.florian.nscalarproduct.encryption.RSA;

import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SecretPart {
    private BigInteger[] diagonal;
    private BigInteger r;
    private String id;

    public SecretPart() {
    }

    public SecretPart(EncryptedSecretPart secretPart, RSA rsa, byte[] key)
            throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.id = secretPart.getId();
        AES aes = new AES(rsa.decryptAESKey(key));
        this.r = aes.decryptBigInteger(secretPart.getR());
        String[] encryptdDiagonal = secretPart.getDiagonal();
        diagonal = new BigInteger[encryptdDiagonal.length];
        Arrays.stream(encryptdDiagonal).map(x -> aes.decryptBigInteger(x))
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
