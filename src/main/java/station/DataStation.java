package station;

import secret.SecretPart;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import static util.Util.matrixDiagonalMultiplication;

public class DataStation extends Station {
    private SecretPart secret;
    private BigInteger[][] localData;
    private int population;
    private String id;
    private BigInteger[][] obfuscated;
    private BigInteger V2;

    private static final int MAX = 10;


    public DataStation(String id, BigInteger[][] localData) {
        this.id = id;
        this.localData = localData;
        this.population = localData.length;
    }

    public void setLocalSecret(SecretPart secret) {
        this.secret = secret;
        obfuscated = new BigInteger[localData.length][localData.length];
        BigInteger[][] secretMatrix = secret.getMatrix();
        for (int i = 0; i < localData.length; i++) {
            obfuscated[i][i] = localData[i][i].add(secretMatrix[i][i]);
        }
    }

    public BigInteger localCalculationFirstParty(List<BigInteger[][]> obfuscated) {
        List<BigInteger[][]> fullList = obfuscated;
        fullList.add(localData);
        Random random = new Random();

        this.V2 = BigInteger.valueOf(random.nextInt(MAX));
        return BigInteger.valueOf(obfuscated.size() - 1).multiply(secret.getR())
                .add(matrixDiagonalMultiplication(fullList, population)).subtract(V2);
    }

    public BigInteger removeV2(BigInteger partial) {
        return partial.add(V2);
    }

    public BigInteger localCalculationNthParty(List<BigInteger[][]> obfuscated, BigInteger partial) {
        List<BigInteger[][]> fullList = obfuscated;
        fullList.add(secret.getMatrix());
        return partial.subtract(matrixDiagonalMultiplication(fullList, population))
                .add(BigInteger.valueOf(obfuscated.size() - 1).multiply(secret.getR()));
    }

    public String getId() {
        return id;
    }

    public int getPopulation() {
        return population;
    }

    public BigInteger[][] getObfuscated() {
        return obfuscated;
    }
}
