package com.florian.nscalarproduct.secret;

import com.florian.nscalarproduct.encryption.AES;
import com.florian.nscalarproduct.encryption.RSA;
import com.florian.nscalarproduct.station.DataStation;
import com.florian.nscalarproduct.util.Util;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SecretTest {

    @Test
    public void testGenerateSecret() {
        List<DataStation> parties = new ArrayList<>();
        int length = 10;
        for (int precision = 0; precision < 10; precision++) {
            for (int i = 0; i < length; i++) {
                parties.add(new DataStation(String.valueOf(i), new BigInteger[length]));
            }
            Secret secret = Secret.generateSecret(parties, precision);

            BigInteger sum = BigInteger.ZERO;
            List<BigInteger[]> diagonals = new ArrayList<>();
            for (SecretPart part : secret.getParts()) {
                assertNotEquals(part.getR(), BigInteger.ZERO);
                sum = sum.add(part.getR());
                diagonals.add(part.getDiagonal());
            }

            assertEquals(sum, Util.matrixDiagonalMultiplication(diagonals, length));
        }

    }

    @Test
    public void testGenerateSecretPrecision() {
        List<DataStation> parties = new ArrayList<>();
        int length = 1;
        for (int precision = 0; precision < 10; precision++) {
            for (int i = 0; i < length; i++) {
                parties.add(new DataStation(String.valueOf(i), new BigInteger[length]));
            }
            Secret secret = Secret.generateSecret(parties, precision);

            BigInteger sum = BigInteger.ZERO;
            List<BigInteger[]> diagonals = new ArrayList<>();


            for (SecretPart part : secret.getParts()) {
                assertNotEquals(part.getR(), BigInteger.ZERO);
                sum = sum.add(part.getR());
                diagonals.add(part.getDiagonal());
            }

            assertEquals(sum, Util.matrixDiagonalMultiplication(diagonals, length));
        }
    }

    @Test
    public void testEncryptingSecretAES()
            throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException {
        List<DataStation> parties = new ArrayList<>();
        int length = 10;
        for (int precision = 0; precision < 10; precision++) {
            for (int i = 0; i < length; i++) {
                parties.add(new DataStation(String.valueOf(i), new BigInteger[length]));
            }
            Secret secret = Secret.generateSecret(parties, precision);

            BigInteger sum = BigInteger.ZERO;
            List<BigInteger[]> diagonals = new ArrayList<>();
            for (SecretPart part : secret.getParts()) {
                assertNotEquals(part.getR(), BigInteger.ZERO);
                sum = sum.add(part.getR());
                diagonals.add(part.getDiagonal());
            }
            assertEquals(sum, Util.matrixDiagonalMultiplication(diagonals, length));

            List<EncryptedSecretPart> encryptedParts = new ArrayList<>();
            List<SecretPart> decryptedParts = new ArrayList<>();
            for (SecretPart part : secret.getParts()) {
                AES aes = new AES();
                EncryptedSecretPart encrypted = new EncryptedSecretPart(part, aes);
                encryptedParts.add(encrypted);
                RSA rsa = new RSA();
                RSA rsa2 = new RSA();
                byte[] key = rsa2.encryptSecretKey(aes.getKey(), rsa.getPublicKey());
                SecretPart decrypted = new SecretPart(encrypted, rsa, key);
                decryptedParts.add(decrypted);
                aes.encrypt(part.getR());
                assertEquals(decrypted.getR(), part.getR());
            }

            BigInteger sumEncDec = BigInteger.ZERO;
            List<BigInteger[]> diagonalsEncDec = new ArrayList<>();
            for (SecretPart part : decryptedParts) {
                assertNotEquals(part.getR(), BigInteger.ZERO);
                sumEncDec = sumEncDec.add(part.getR());
                diagonalsEncDec.add(part.getDiagonal());
            }

            assertEquals(sumEncDec, Util.matrixDiagonalMultiplication(diagonalsEncDec, length));
            assertEquals(sumEncDec, sum);
            for (int i = 0; i < diagonals.size(); i++) {
                for (int j = 0; j < diagonals.get(0).length; j++) {
                    assertEquals(diagonalsEncDec.get(i)[j], diagonals.get(i)[j]);
                }
            }
        }
    }
}