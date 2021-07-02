package secret;

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

    public static Secret generateSecret(int length, List<String> parties) {
        List<BigInteger[][]> matrices = new ArrayList<>();
        int size = parties.size();
        Random random = new Random();
        for (String party : parties) {
            BigInteger[][] matrix = new BigInteger[length][length];
            matrices.add(matrix);
            for (int i = 0; i < length; i++) {
                matrix[i][i] = BigInteger.valueOf(random.nextInt(MAX));
            }
        }
        BigInteger sum = matrixDiagonalMultiplication(matrices, length);

        List<BigInteger> rs = new ArrayList<>();
        BigInteger substep = sum;
        for (int i = 0; i < size - 1; i++) {
            BigInteger value = BigInteger.valueOf(
                    random.nextInt(substep.add(BigInteger.valueOf(-1)).intValue()));
            rs.add(value);
            substep = substep.subtract(value);
        }
        rs.add(substep);
        SecretPart[] parts = new SecretPart[size];
        for (int i = 0; i < size; i++) {
            parts[i] = new SecretPart(matrices.get(i), rs.get(i), parties.get(i));
        }
        return new Secret(parts);
    }
}
