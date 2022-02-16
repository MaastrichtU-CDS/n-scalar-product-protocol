package com.florian.nscalarproduct.encryption;

import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncryptionTest {
    private static int PRECISION = 10000;


    @Test
    public void testEncryptions() {
        // comparison of various encrytion schemes performance
        // AES + RSA is the fastest, however, it cannot be run in parallel.
        // Under specific conditions Elgamal might become faster because of this
        List<Integer> times = new ArrayList<>();
        for (int j = 0; j < 10000; j++) {
            for (int i = 0; i < 100; i++) {
                times.add(i);
            }
        }
        long elgamalStart = System.currentTimeMillis();
        Elgamal main = new Elgamal();
        main.generateKeys();
        Elgamal second = new Elgamal(main.getPublicKey());
        times.parallelStream().forEach(x -> testElgamal(x, main, second));
        long elgamalEnd = System.currentTimeMillis();

        long paillierStart = System.currentTimeMillis();
        Paillier paillier = new Paillier();
        paillier.generateKeyPair();
        times.parallelStream().forEach(x -> testPaillier(x, paillier));
        long paillierEnd = System.currentTimeMillis();

        long aesStart = System.currentTimeMillis();
        times.parallelStream().forEach(x -> testAES(x));
        long aesEnd = System.currentTimeMillis();

        long rsaStart = System.currentTimeMillis();
        testRSA(times);
        long rsaEnd = System.currentTimeMillis();

        System.out.println("Elgamal: " + (elgamalEnd - elgamalStart) + "ms");
        System.out.println("Paillier: " + (paillierEnd - paillierStart) + "ms");
        System.out.println("AES: " + (aesEnd - aesStart) + "ms");
        System.out.println("RSA COMBO: " + (rsaEnd - rsaStart) + "ms");
    }

    private void testRSA(List<Integer> times) {

        try {
            RSA rsa = new RSA();
            AES aes = new AES();

            byte[] eAES = rsa.encryptSecretKey(aes.getKey());
            AES aes2 = new AES(rsa.decryptAESKey(eAES));
            for (int i : times) {
                BigInteger a = BigInteger.valueOf(2).pow(i);
                String eA = aes.encrypt(a);
                assertEquals(a, aes2.decrypt(eA));
            }
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void testAES(int i, AES aes) {
        BigInteger a = BigInteger.valueOf(2).pow(i);

        String eA = aes.encrypt(a);
        assertEquals(aes.decrypt(eA), a);
    }

    private void testAES(int i) {
        BigInteger a = BigInteger.valueOf(2).pow(i);
        try {
            AES aes = new AES();
            String eA = aes.encrypt(a);
            assertEquals(aes.decrypt(eA), a);
            if (!aes.decrypt(eA).equals(a)) {
                System.out.println("sad");
            }
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void testPaillier(int i, Paillier paillier) {
        BigInteger a = BigInteger.valueOf(2).pow(i);


        PublicPaillierKey p = paillier.getPublicKey();

        BigInteger eA = p.encrypt(a);

        assertEquals(paillier.decrypt(eA), a);
    }

    private void testElgamal(int i, Elgamal main, Elgamal second) {
        BigInteger a = BigInteger.valueOf(2).pow(i);

        EncryptedElgamal eA = second.encrypt(a);

        assertEquals(main.decrypt(eA), a);
    }

    @Test
    public void testElgamal() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                BigInteger a = BigInteger.valueOf(i);
                BigInteger b = BigInteger.valueOf(j);

                Elgamal main = new Elgamal();
                main.generateKeys();
                Elgamal second = new Elgamal(main.getPublicKey());

                EncryptedElgamal eA = main.encrypt(a);
                EncryptedElgamal eB = second.encrypt(b);

                EncryptedElgamal multiple = EncryptedElgamal.multiply(eA, eB);
                assertEquals(main.decrypt(multiple), a.multiply(b));
            }
        }

        for (double i = 0; i < 1; i += 0.1) {
            for (double j = 0; j < 1; j += 0.1) {
                BigInteger a = BigInteger.valueOf((int) (i * PRECISION));
                BigInteger b = BigInteger.valueOf((int) (j * PRECISION));

                Elgamal main = new Elgamal();
                main.generateKeys();
                Elgamal second = new Elgamal(main.getPublicKey());

                EncryptedElgamal eA = main.encrypt(a);
                EncryptedElgamal eB = second.encrypt(b);

                EncryptedElgamal multiple = EncryptedElgamal.multiply(eA, eB);
                double decrypted = main.decrypt(multiple).doubleValue() / (PRECISION * PRECISION);

                assertEquals(decrypted, i * j, 0.001);
            }
        }
    }
}