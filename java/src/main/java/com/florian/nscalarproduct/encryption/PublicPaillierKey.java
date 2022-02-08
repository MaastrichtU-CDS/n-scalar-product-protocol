package com.florian.nscalarproduct.encryption;


import java.math.BigInteger;
import java.util.Random;

public class PublicPaillierKey {
    private final int bits;
    private final BigInteger n;
    private final BigInteger nSquared;
    private final BigInteger g;

    public PublicPaillierKey(BigInteger n, BigInteger nSquared, BigInteger g, int bits) {
        this.n = n;
        this.nSquared = nSquared;
        this.bits = bits;
        this.g = g;
    }

    public int getBits() {
        return bits;
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getnSquared() {
        return nSquared;
    }

    public BigInteger getG() {
        return g;
    }

    /**
     * Encrypts the given plaintext.
     *
     * @param m The plaintext that should be encrypted.
     * @return The corresponding ciphertext.
     */
    public final BigInteger encrypt(BigInteger m) {

        BigInteger r;
        do {
            r = new BigInteger(bits, new Random());
        } while (r.compareTo(n) >= 0);

        BigInteger result = g.modPow(m, nSquared);
        BigInteger x = r.modPow(n, nSquared);

        result = result.multiply(x);
        result = result.mod(nSquared);

        return result;
    }
}
