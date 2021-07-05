package secret;

import station.DataStation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static util.Util.matrixDiagonalMultiplication;

public class Secret {
    private SecretPart[] parts;
    private static final int MAX = 10;

    public Secret(SecretPart[] parts) {
        this.parts = parts;
    }

    public SecretPart[] getParts() {
        return parts;
    }

    public static Secret generateSecret(List<DataStation> parties) {
        List<BigInteger[][]> matrices = new ArrayList<>();
        int length = parties.get(0).getPopulation();
        int size = parties.size();
        Random random = new Random();
        for (DataStation party : parties) {
            BigInteger[][] matrix = new BigInteger[length][length];
            matrices.add(matrix);
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < length; j++) {
                    if (i == j) {
                        matrix[i][i] = BigInteger.valueOf(random.nextInt(MAX) + 1);
                    } else {
                        matrix[i][j] = BigInteger.ZERO;
                    }
                }
            }
        }
        BigInteger sum = matrixDiagonalMultiplication(matrices, length);

        List<BigInteger> rs = new ArrayList<>();
        BigInteger remainder = sum;
        BigInteger max = sum.divide(BigInteger.valueOf(size));
        for (int i = 0; i < size - 1; i++) {
            BigInteger value = BigInteger.valueOf(
                    random.nextInt(max.intValue()) + 1);
            rs.add(value);
            remainder = remainder.subtract(value);
        }
        rs.add(remainder);
        SecretPart[] parts = new SecretPart[size];
        for (int i = 0; i < size; i++) {
            parts[i] = new SecretPart(matrices.get(i), rs.get(i), parties.get(i).getId());
        }
        return new Secret(parts);
    }
}
