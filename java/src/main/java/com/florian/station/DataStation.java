package com.florian.station;


import com.florian.secret.SecretPart;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.florian.util.Util.matrixDiagonalMultiplication;

public class DataStation extends Station {
    private SecretPart secret;
    private BigInteger[] localData;
    private int population;
    private String id;
    private BigInteger[] obfuscated;
    private BigInteger v2;

    private static final int MAX = 10;


    public DataStation(String id, BigInteger[] localData) {
        this.id = id;
        this.localData = localData;
        this.population = localData.length;
    }

    public DataStation(DataStation station) {
        this.id = station.getId();
        this.localData = station.localData;
        this.population = localData.length;
    }

    public void setLocalSecret(SecretPart secret) {
        this.secret = secret;
        obfuscated = new BigInteger[localData.length];
        BigInteger[] secretMatrix = secret.getDiagonal();
        for (int i = 0; i < localData.length; i++) {
            obfuscated[i] = localData[i].add(secretMatrix[i]);
        }
    }

    public BigInteger localCalculationFirstParty(List<BigInteger[]> obfuscated) {
        List<BigInteger[]> fullList = new ArrayList<>(obfuscated);
        fullList.add(localData);
        Random random = new Random();

        this.v2 = BigInteger.valueOf(random.nextInt(MAX));
        return BigInteger.valueOf(obfuscated.size()).multiply(secret.getR())
                .add(matrixDiagonalMultiplication(fullList, population)).subtract(v2);
    }

    public BigInteger removeV2(BigInteger partial) {
        return partial.add(v2);
    }

    public BigInteger localCalculationNthParty(List<BigInteger[]> obfuscated) {
        List<BigInteger[]> fullList = new ArrayList<>(obfuscated);
        fullList.add(secret.getDiagonal());
        return BigInteger.ZERO.subtract(matrixDiagonalMultiplication(fullList, population))
                .add(BigInteger.valueOf(obfuscated.size()).multiply(secret.getR()));
    }


    public String getId() {
        return id;
    }

    public int getPopulation() {
        return population;
    }

    public BigInteger[] getObfuscated() {
        return obfuscated;
    }
}
