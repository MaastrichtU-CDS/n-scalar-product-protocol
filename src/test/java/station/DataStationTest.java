package station;

import junit.framework.TestCase;
import secret.Secret;
import secret.SecretPart;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static secret.Secret.generateSecret;
import static util.Util.matrixDiagonalMultiplication;

public class DataStationTest extends TestCase {
    private final int POPUlATION = 10;

    public void test2PartyCalculation() {
        BigInteger[][] A_data = createData(POPUlATION);
        DataStation stationA = new DataStation("1", A_data);

        BigInteger[][] B_data = createData(POPUlATION);
        DataStation stationB = new DataStation("2", B_data);

        Secret secret = generateSecret(Arrays.asList(stationA, stationB));

        stationA.setLocalSecret(secret.getParts()[0]);
        stationB.setLocalSecret(secret.getParts()[1]);

        BigInteger ra = secret.getParts()[0].getR();
        BigInteger rb = secret.getParts()[1].getR();
        BigInteger[][] B_hat = stationB.getObfuscated();
        BigInteger[][] A_hat = stationA.getObfuscated();
        BigInteger[][] Ra = secret.getParts()[0].getMatrix();

        // manually run the calculation: A_hat * B + rb - B_hat * Ra + ra to determine expected result
        BigInteger expectedresult =
                matrixDiagonalMultiplication(Arrays.asList(A_hat, B_data), POPUlATION).add(rb)
                        .subtract(matrixDiagonalMultiplication(Arrays.asList(B_hat, Ra), POPUlATION)).add(ra);

        // run 2-party calculation
        List<BigInteger[][]> list = new ArrayList<>();
        list.add(stationA.getObfuscated());
        BigInteger partial = stationB.localCalculationFirstParty(list);
        List<BigInteger[][]> list2 = new ArrayList<>();
        list2.add(stationB.getObfuscated());
        partial = stationA.localCalculationNthParty(list2, partial);
        BigInteger result = stationB.removeV2(partial);

        assertEquals(expectedresult, result);
    }

    public void testGetObfuscated() {
        BigInteger[][] data = createData(POPUlATION);

        DataStation station = new DataStation("1", data);
        SecretPart secret = generateSecret(Arrays.asList(station)).getParts()[0];

        station.setLocalSecret(secret);

        BigInteger[][] obfuscated = station.getObfuscated();
        for (int i = 0; i < POPUlATION; i++) {
            assertEquals(data[i][i].add(secret.getMatrix()[i][i]), obfuscated[i][i]);
        }

    }

    public static DataStation createStation(String id, int population) {
        return new DataStation(id, createData(population));
    }

    private static BigInteger[][] createData(int population) {
        BigInteger[][] data = new BigInteger[population][population];

        BigInteger[][] secretMatrix = new BigInteger[population][population];
        Random random = new Random();
        for (int i = 0; i < population; i++) {
            for (int j = 0; j < population; j++) {
                data[i][j] = BigInteger.valueOf(random.nextInt(2));
            }
        }
        return data;
    }

}