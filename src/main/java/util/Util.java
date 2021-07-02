package util;

import cern.colt.matrix.linalg.Algebra;

import java.math.BigInteger;
import java.util.List;

public final class Util {
    private static Algebra algebra = new Algebra();

    private Util() {
    }

    /**
     * Multiply the diagonals of a list of square matrices and sum the resulting diagonal
     *
     * @param list       of matrices
     * @param matrixSize size of any given matrix
     * @return
     */
    public static BigInteger matrixDiagonalMultiplication(List<BigInteger[][]> list, int matrixSize) {
        BigInteger res = BigInteger.ZERO;
        for (int i = 0; i < matrixSize; i++) {
            final int j = i;
            res = res.add(list.stream().map(x -> x[j][j]).reduce(BigInteger.ONE, BigInteger::multiply));
        }
        return res;
    }
}
