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

    @Test
    public void testCalculateNPartyScalarProduct() {
        for (int n = 2; n < 8; n++) {
            for (int population = 2; population < 10; population++) {
                List<DataStation> datastations = new ArrayList<>();
                List<BigInteger[][]> datasets = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    BigInteger[][] data = createData(population);
                    datastations.add(new DataStation(String.valueOf(i), data));
                    datasets.add(data);
                }
                CentralStation central = new CentralStation();

                // calculate expected anwser:
                BigInteger expected = matrixDiagonalMultiplication(datasets, datasets.get(0).length);
                BigInteger result = central.calculateNPartyScalarProduct(datastations);

                assertEquals(expected, result);
            }
        }
    }

    @Test
    public void testCalculate3PartyScalarProduct() {
        final int POPUlATION = 5;
        final int N = 3;

        List<DataStation> datastations = new ArrayList<>();
        List<BigInteger[][]> datasets = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            BigInteger[][] data = createData(POPUlATION);
            datastations.add(new DataStation(String.valueOf(i), data));
            datasets.add(data);
        }
        CentralStation central = new CentralStation();

        // calculate expected anwser:
        BigInteger expected = matrixDiagonalMultiplication(datasets, datasets.get(0).length);
        BigInteger result = central.calculateNPartyScalarProduct(datastations);

        assertEquals(expected, result);
    }

    @Test
    public void testDetermineSubprotocols() {
        for (int population = 1; population < 10; population++) {
            List<DataStation> datastations = new ArrayList<>();
            for (int i = 0; i < population; i++) {
                datastations.add(createStation(String.valueOf(i), population));
            }
            CentralStation central = new CentralStation();
            SecretStation secretStation = new SecretStation();
            secretStation.shareSecret(datastations);

            List<List<DataStation>> subprotocols = central.determineSubprotocols(datastations, secretStation);
            assertEquals(subprotocols.size(), calculateExpectedCombinations(population).longValue());
        }
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