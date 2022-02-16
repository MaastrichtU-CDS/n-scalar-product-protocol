package com.florian.nscalarproduct.encryption;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class RSA {
    private KeyPair key;
    private static final int SIZE = 1024;
    private Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

    public RSA() throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException {
        generateKey();
    }

    private void generateKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        keyGenerator.initialize(SIZE);
        key = keyGenerator.generateKeyPair();
    }

    public PublicKey getPublicKey() {
        return key.getPublic();
    }

    public byte[] encryptSecretKey(SecretKey aes) {
        byte[] result = null;

        try {
            // initialize the cipher with the user's public key
            cipher.init(Cipher.ENCRYPT_MODE, key.getPublic());
            result = cipher.doFinal(aes.getEncoded());
        } catch (Exception e) {
            System.out.println("exception encoding key: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public byte[] encryptSecretKey(SecretKey aes, PublicKey key) {
        byte[] result = null;

        try {
            // initialize the cipher with the user's public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            result = cipher.doFinal(aes.getEncoded());
        } catch (Exception e) {
            System.out.println("exception encoding key: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public SecretKey decryptAESKey(byte[] data) {
        SecretKey result = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());

            // generate the aes key!
            result = new SecretKeySpec(cipher.doFinal(data), "AES");
        } catch (Exception e) {
            System.out.println("exception decrypting the aes key: "
                                       + e.getMessage());
            return null;
        }
        return result;
    }
}
