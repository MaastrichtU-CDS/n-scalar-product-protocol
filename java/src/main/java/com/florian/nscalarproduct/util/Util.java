package com.florian.nscalarproduct.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public final class Util {

    private Util() {
    }

    /**
     * Multiply the diagonals of a list of square matrices and sum the resulting diagonal
     *
     * @param list       of matrices
     * @param matrixSize size of any given matrix
     * @return
     */
    public static BigInteger matrixDiagonalMultiplication(List<BigInteger[]> list, int matrixSize) {
        BigInteger res = BigInteger.ZERO;
        for (int i = 0; i < matrixSize; i++) {
            final int j = i;
            res = res.add(list.stream().map(x -> x[j]).reduce(BigInteger.ONE, BigInteger::multiply));
        }
        return res;
    }

    public static BigDecimal matrixDiagonalMultiplicationDecimal(List<BigDecimal[]> list, int matrixSize) {
        BigDecimal res = BigDecimal.ZERO;
        for (int i = 0; i < matrixSize; i++) {
            final int j = i;
            res = res.add(list.stream().map(x -> x[j]).reduce(BigDecimal.ONE, BigDecimal::multiply));
        }
        return res;
    }


    public static BigInteger[] matrixMultiplication(List<BigInteger[]> list) {
        BigInteger[] res = matrixMultiplication(list.get(0), list.get(1));
        for (int i = 2; i < list.size(); i++) {
            res = matrixMultiplication(list.get(i), res);
        }
        return res;
    }

    public static BigInteger[] matrixMultiplication(BigInteger[] a, BigInteger[] b) {
        BigInteger[] res = new BigInteger[a.length];
        for (int i = 0; i < a.length; i++) {
            res[i] = a[i].multiply(b[i]);
        }
        return res;
    }
}
