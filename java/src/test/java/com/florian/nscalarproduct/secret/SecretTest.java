package com.florian.nscalarproduct.secret;

import com.florian.nscalarproduct.station.DataStation;
import com.florian.nscalarproduct.util.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SecretTest {

    @Test
    public void testGenerateSecret() {
        List<DataStation> parties = new ArrayList<>();
        int length = 10;
        for (int i = 0; i < length; i++) {
            parties.add(new DataStation(String.valueOf(i), new BigInteger[length]));
        }
        Secret secret = Secret.generateSecret(parties);

        BigInteger sum = BigInteger.ZERO;
        List<BigInteger[]> diagonals = new ArrayList<>();
        for (SecretPart part : secret.getParts()) {
            assertNotEquals(part.getR(), BigInteger.ZERO);
            sum = sum.add(part.getR());
            diagonals.add(part.getDiagonal());
        }
        Assertions.assertEquals(sum, Util.matrixDiagonalMultiplication(diagonals, length));

    }
}