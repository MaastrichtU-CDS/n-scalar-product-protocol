package util;

import junit.framework.TestCase;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static util.Util.matrixDiagonalMultiplication;

public class UtilTest extends TestCase {

    @Test
    public void testMatrixDiagonalMultiplication() {
        // test various sizes of matrix, with various amounts of parties
        // max value calculated is in surplus of 100 ^ 100
        for (int size = 1; size < 100; size++) {
            List<BigInteger[][]> matrices = new ArrayList<>();
            for (int parties = 0; parties < 100; parties++) {
                matrices.add(new BigInteger[size][size]);
            }
            BigInteger anwser = BigInteger.ZERO;
            for (int i = 0; i < size; i++) {
                for (BigInteger[][] matrix : matrices) {
                    matrix[i][i] = BigInteger.valueOf(i);
                }
                anwser = anwser.add(BigInteger.valueOf(i).pow(matrices.size()));
            }
            assertEquals(anwser, matrixDiagonalMultiplication(matrices, size));
        }
    }
}