package station;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static station.DataStationTest.createData;
import static station.DataStationTest.createStation;
import static util.Util.matrixDiagonalMultiplication;

public class CentralStationTest {
    private final int POPUlATION = 5;
    private final int N = 3;

    @Test
    public void testCalculateNPartyScalarProduct() {
        List<DataStation> datastations = new ArrayList<>();
        List<BigInteger[][]> datasets = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            BigInteger[][] data = createData(POPUlATION);
            datastations.add(new DataStation(String.valueOf(i), data));
            datasets.add(data);
        }
        CentralStation central = new CentralStation(datastations);
        SecretStation secretStation = new SecretStation();
        secretStation.shareSecret(datastations);

        // calculate expected anwser:
        BigInteger expected = matrixDiagonalMultiplication(datasets, datasets.get(0).length);
        BigInteger result = central.calculateNPartyScalarProduct(datastations, 0);

        assertEquals(expected, result);
    }

    @Test
    public void testDetermineSubprotocols() {
        List<DataStation> datastations = new ArrayList<>();
        for (int i = 0; i < POPUlATION; i++) {
            datastations.add(createStation(String.valueOf(i), POPUlATION));
        }
        CentralStation central = new CentralStation(datastations);
        SecretStation secretStation = new SecretStation();
        secretStation.shareSecret(datastations);

        List<List<DataStation>> subprotocols = central.determineSubprotocols(datastations, secretStation);
        assertEquals(subprotocols.size(), calculateExpectedCombinations(POPUlATION).longValue());
    }

    //calculates  n! / (x!(n−x)!) for every x >= 2 < n
    private BigInteger calculateExpectedCombinations(int n) {
        BigInteger nFactorial = factorial(n);
        BigInteger sum = BigInteger.ZERO;
        for (int x = 2; x < n; x++) {
            BigInteger xFactorial = factorial(x);
            BigInteger nxFactorial = factorial(n - x);
            sum = sum.add(nFactorial.divide(xFactorial.multiply(nxFactorial)));
        }
        return sum;
    }

    private BigInteger factorial(int n) {
        BigInteger nFactorial = BigInteger.ONE;
        for (int i = 1; i <= n; i++) {
            nFactorial = nFactorial.multiply(BigInteger.valueOf(i));
        }
        return nFactorial;
    }

}