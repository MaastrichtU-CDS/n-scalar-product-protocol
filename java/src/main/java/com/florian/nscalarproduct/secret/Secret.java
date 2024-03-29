package com.florian.nscalarproduct.secret;

import com.florian.nscalarproduct.station.DataStation;
import com.florian.nscalarproduct.util.Util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Secret {
    private SecretPart[] parts;
    private static final int MAX = 512;
    private static final int TEN = 10;

    public Secret(SecretPart[] parts) {
        this.parts = parts;
    }

    public SecretPart[] getParts() {
        return parts;
    }

    public static Secret generateSecret(List<DataStation> parties, int precision) {
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
        BigInteger sum = Util.matrixDiagonalMultiplication(diagonals, length);

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
            setPrecision(parts[i], precision, size);
        }
        return new Secret(parts);
    }

    public static Secret generateSecret(List<String> serverIds, int length, int precision) {
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
        BigInteger sum = Util.matrixDiagonalMultiplication(diagonals, length);

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
            setPrecision(parts[i], precision, size);
        }
        return new Secret(parts);
    }

    private static void setPrecision(SecretPart part, int precision, int size) {
        BigInteger mulitplier = BigInteger.valueOf(TEN).pow(precision);
        for (int i = 0; i < part.getDiagonal().length; i++) {
            part.getDiagonal()[i] = part.getDiagonal()[i].multiply(mulitplier);
        }
        part.setR(part.getR().multiply(mulitplier.pow(size)));
    }
}
