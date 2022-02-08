package com.florian.nscalarproduct.encryption;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncryptionTest {
    private static int PRECISION = 10000;


    @Test
    public void testEncryptions() {
        List<Integer> times = new ArrayList<>();
        for (int j = 0; j < 10000; j++) {
            for (int i = 0; i < 100; i++) {
                times.add(i);
            }
        }
        long elgamalStart = System.currentTimeMillis();
        times.parallelStream().forEach(x -> testElgamal(x));
        long elgamalEnd = System.currentTimeMillis();

        long paillierStart = System.currentTimeMillis();
        times.parallelStream().forEach(x -> testPaillier(x));
        long paillierEnd = System.currentTimeMillis();

        System.out.println("Elgamal: " + (elgamalEnd - elgamalStart) + "ms");
        System.out.println("Paillier: " + (paillierEnd - paillierStart) + "ms");
    }

    private void testPaillier(int i) {
        BigInteger a = BigInteger.valueOf(2).pow(i);

        Paillier paillier = new Paillier();
        paillier.generateKeyPair();
        PublicPaillierKey p = paillier.getPublicKey();

        BigInteger eA = p.encrypt(a);

        assertEquals(paillier.decrypt(eA), a);
    }

    private void testElgamal(int i) {
        BigInteger a = BigInteger.valueOf(2).pow(i);

        Elgamal main = new Elgamal();
        main.generateKeys();
        Elgamal second = new Elgamal(main.getPublicKey());

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