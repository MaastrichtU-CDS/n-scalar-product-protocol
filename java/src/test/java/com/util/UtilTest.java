package com.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class UtilTest {

    @Test
    public void testMatrixDiagonalMultiplication() {
        // test various sizes of matrix, with various amounts of parties
        // max value calculated is in surplus of 100 ^ 100
        // Diagonal of the matrices is filled according to the size of the matrix. so X[0][0] = 0, X[1][1] = 1 etc.
        for (int size = 1; size < 100; size++) {
            List<BigInteger[]> diagonals = new ArrayList<>();
            for (int parties = 0; parties < 100; parties++) {
                diagonals.add(new BigInteger[size]);
            }
            BigInteger anwser = BigInteger.ZERO;
            for (int i = 0; i < size; i++) {
                for (BigInteger[] diagonal : diagonals) {
                    diagonal[i] = BigInteger.valueOf(i);
                }
                anwser = anwser.add(BigInteger.valueOf(i).pow(diagonals.size()));
            }
            Assertions.assertEquals(anwser, Util.matrixDiagonalMultiplication(diagonals, size));
        }
    }

}