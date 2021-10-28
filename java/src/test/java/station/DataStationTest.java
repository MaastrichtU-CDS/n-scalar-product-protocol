package station;

import org.junit.jupiter.api.Test;
import secret.Secret;
import secret.SecretPart;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static secret.Secret.generateSecret;
import static util.Util.matrixDiagonalMultiplication;

public class DataStationTest {
    private final int POPUlATION = 10;

    @Test
    public void test2PartyCalculation() {
        BigInteger[] A_data = createData(POPUlATION);
        DataStation stationA = new DataStation("1", A_data);

        BigInteger[] B_data = createData(POPUlATION);
        DataStation stationB = new DataStation("2", B_data);

        Secret secret = generateSecret(Arrays.asList(stationA, stationB));

        stationA.setLocalSecret(secret.getParts()[0]);
        stationB.setLocalSecret(secret.getParts()[1]);

        BigInteger ra = secret.getParts()[0].getR();
        BigInteger rb = secret.getParts()[1].getR();
        BigInteger[] B_hat = stationB.getObfuscated();
        BigInteger[] A_hat = stationA.getObfuscated();
        BigInteger[] Ra = secret.getParts()[0].getDiagonal();

        // manually run the calculation: A_hat * B + rb - B_hat * Ra + ra to determine expected result
        BigInteger expectedresult =
                matrixDiagonalMultiplication(Arrays.asList(A_hat, B_data), POPUlATION).add(rb)
                        .subtract(matrixDiagonalMultiplication(Arrays.asList(B_hat, Ra), POPUlATION)).add(ra);

        // run 2-party calculation
        List<BigInteger[]> list = new ArrayList<>();
        list.add(stationA.getObfuscated());
        BigInteger partial = stationB.localCalculationFirstParty(list);
        List<BigInteger[]> list2 = new ArrayList<>();
        list2.add(stationB.getObfuscated());
        partial = partial.add(stationA.localCalculationNthParty(list2));
        BigInteger result = stationB.removeV2(partial);

        assertEquals(expectedresult, result);
    }

    @Test
    public void testGetObfuscated() {
        BigInteger[] data = createData(POPUlATION);

        DataStation station = new DataStation("1", data);
        SecretPart secret = generateSecret(Arrays.asList(station)).getParts()[0];

        station.setLocalSecret(secret);

        BigInteger[] obfuscated = station.getObfuscated();
        for (int i = 0; i < POPUlATION; i++) {
            assertEquals(data[i].add(secret.getDiagonal()[i]), obfuscated[i]);
        }

    }

    public static DataStation createStation(String id, int population) {
        return new DataStation(id, createData(population));
    }

    public static BigInteger[] createData(int population) {
        BigInteger[] data = new BigInteger[population];

        BigInteger[] secretDiagonal = new BigInteger[population];
        Random random = new Random();
        for (int i = 0; i < population; i++) {
            data[i] = BigInteger.valueOf(random.nextInt(2));
        }
        return data;
    }

}