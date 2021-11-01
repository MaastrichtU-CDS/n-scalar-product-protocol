package secret;

import station.DataStation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static util.Util.matrixDiagonalMultiplication;

public class Secret {
    private SecretPart[] parts;
    private static final int MAX = 512;

    public Secret(SecretPart[] parts) {
        this.parts = parts;
    }

    public SecretPart[] getParts() {
        return parts;
    }

    public static Secret generateSecret(List<DataStation> parties) {
        List<BigInteger[]> diagonals = new ArrayList<>();
        int length = parties.get(0).getPopulation();
        int size = parties.size();
        Random random = new Random();
        for (DataStation party : parties) {
            BigInteger[] diagonal = new BigInteger[length];
            diagonals.add(diagonal);

            for (int i = 0; i < length; i++) {
                diagonal[i] = BigInteger.valueOf(random.nextInt(MAX) + 1);
            }
        }
        BigInteger sum = matrixDiagonalMultiplication(diagonals, length);

        List<BigInteger> rs = new ArrayList<>();
        BigInteger remainder = sum;
        BigInteger max = sum.divide(BigInteger.valueOf(size));
        for (int i = 0; i < size - 1; i++) {
            BigInteger value = BigInteger.ONE;
            if (max.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                value = BigInteger.valueOf(
                        random.nextInt(Integer.MAX_VALUE) + 1);
            } else {
                try {
                    value = BigInteger.valueOf(
                            random.nextInt(max.intValue()) + 1);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            rs.add(value);
            remainder = remainder.subtract(value);
        }
        rs.add(remainder);
        SecretPart[] parts = new SecretPart[size];
        for (int i = 0; i < size; i++) {
            parts[i] = new SecretPart(diagonals.get(i), rs.get(i), parties.get(i).getId());
        }
        return new Secret(parts);
    }

    public static Secret generateSecret(List<String> serverIds, int length) {
        List<BigInteger[]> diagonals = new ArrayList<>();
        int size = serverIds.size();
        Random random = new Random();
        for (String party : serverIds) {
            BigInteger[] diagonal = new BigInteger[length];
            diagonals.add(diagonal);

            for (int i = 0; i < length; i++) {
                diagonal[i] = BigInteger.valueOf(random.nextInt(MAX) + 1);
            }
        }
        BigInteger sum = matrixDiagonalMultiplication(diagonals, length);

        List<BigInteger> rs = new ArrayList<>();
        BigInteger remainder = sum;
        BigInteger max = sum.divide(BigInteger.valueOf(size));
        for (int i = 0; i < size - 1; i++) {
            BigInteger value = BigInteger.ONE;
            if (max.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                value = BigInteger.valueOf(
                        random.nextInt(Integer.MAX_VALUE) + 1);
            } else {
                try {
                    value = BigInteger.valueOf(
                            random.nextInt(max.intValue()) + 1);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            rs.add(value);
            remainder = remainder.subtract(value);
        }
        rs.add(remainder);
        SecretPart[] parts = new SecretPart[size];
        for (int i = 0; i < size; i++) {
            parts[i] = new SecretPart(diagonals.get(i), rs.get(i), serverIds.get(i));
        }
        return new Secret(parts);
    }
}
