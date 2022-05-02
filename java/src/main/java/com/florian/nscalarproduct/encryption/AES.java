package com.florian.nscalarproduct.encryption;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class AES {
    private SecretKey key;
    private static final int SIZE = 256;
    private Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    private static final IvParameterSpec IV = new IvParameterSpec("RandomInitVector".getBytes(StandardCharsets.UTF_8));

    public AES() throws NoSuchPaddingException, NoSuchAlgorithmException {
        generateKey();
    }

    public AES(SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.key = key;
    }

    private void generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(SIZE);
        key = keyGenerator.generateKey();
    }

    public SecretKey getKey() {
        return key;
    }


    public String encrypt(BigInteger input) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, IV);
            byte[] encrypted = cipher.doFinal(input.toString().getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String encrypt(BigDecimal input) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, IV);
            byte[] encrypted = cipher.doFinal(input.toString().getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BigDecimal decryptBigDecimal(String input) {
        byte[] original = null;
        try {

            cipher.init(Cipher.DECRYPT_MODE, key, IV);
            original = cipher.doFinal(Base64.decodeBase64(input));
            return new BigDecimal(new String(original));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BigInteger decryptBigInteger(String input) {
        byte[] original = null;
        try {

            cipher.init(Cipher.DECRYPT_MODE, key, IV);
            original = cipher.doFinal(Base64.decodeBase64(input));
            return new BigInteger(new String(original));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
