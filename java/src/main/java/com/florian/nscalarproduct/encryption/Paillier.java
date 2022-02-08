package com.florian.nscalarproduct.encryption;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Paillier {
    private static final int BITS = 128;

    private BigInteger lambda;
    private BigInteger preCalculatedDenominator;

    private PublicPaillierKey publicKey;
    private SecureRandom rng = new SecureRandom();

    public void generateKeyPair() {

        BigInteger p;
        BigInteger q;
        int length = BITS / 2;

        p = BigInteger.probablePrime(length, rng);
        q = BigInteger.probablePrime(length, rng);


        BigInteger n = p.multiply(q);
        BigInteger nSquared = n.multiply(n);

        BigInteger pMinusOne = p.subtract(BigInteger.ONE);
        BigInteger qMinusOne = q.subtract(BigInteger.ONE);

        lambda = this.lcm(pMinusOne, qMinusOne);

        BigInteger g;
        BigInteger helper;

        do {
            g = new BigInteger(BITS, rng);
            helper = calculateL(g.modPow(lambda, nSquared), n);

        } while (!helper.gcd(n).equals(BigInteger.ONE));

        publicKey = new PublicPaillierKey(n, nSquared, g, BITS);
        preCalculatedDenominator = helper.modInverse(n);
    }

    public final BigInteger decrypt(BigInteger c) {

        BigInteger n = publicKey.getN();
        BigInteger nSquare = publicKey.getnSquared();

        BigInteger p = c.modPow(lambda, nSquare).subtract(BigInteger.ONE).divide(n).multiply(preCalculatedDenominator)
                .mod(n);

        return p;
    }

    private BigInteger calculateL(BigInteger u, BigInteger n) {
        BigInteger result = u.subtract(BigInteger.ONE);
        result = result.divide(n);
        return result;
    }

    private BigInteger lcm(BigInteger a, BigInteger b) {
        BigInteger result;
        BigInteger gcd = a.gcd(b);

        result = a.abs().divide(gcd);
        result = result.multiply(b.abs());

        return result;
    }

    public PublicPaillierKey getPublicKey() {
        return publicKey;
    }
}
