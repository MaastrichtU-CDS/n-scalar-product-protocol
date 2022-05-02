package com.florian.nscalarproduct.encryption;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RSATest {
    private static int PRECISION = 10000;

    @Test
    public void testRSA() {

        try {
            RSA rsa = new RSA();
            AES aes = new AES();

            byte[] eAES = rsa.encryptSecretKey(aes.getKey());
            AES aes2 = new AES(rsa.decryptAESKey(eAES));
            for (int i = 0; i < aes2.getKey().getEncoded().length; i++) {
                assertEquals(aes.getKey().getEncoded()[i], aes2.getKey().getEncoded()[i]);
            }

            BigInteger one = BigInteger.ONE;
            String e = aes.encrypt(one);
            assertEquals(one, aes2.decryptBigInteger(e));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}