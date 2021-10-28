package Performance;

import org.junit.jupiter.api.Test;
import station.CentralStation;
import station.DataStation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static station.DataStationTest.createData;

public class PerformanceTest {

    @Test
    public void testPerformanceInN() {
        //int population = 500;
        int iterations = 100;
        int n = 3;
        //for (int n = 2; n < 8; n++) {
        for (int population = 7000; population < 10500; population += 500) {
            ArrayList<Result> results = new ArrayList<>();
            for (int i = 0; i < iterations; i++) {
                List<DataStation> datastations = new ArrayList<>();
                List<BigInteger[][]> datasets = new ArrayList<>();
                for (int j = 0; j < n; j++) {
                    BigInteger[][] data = createData(population);
                    datastations.add(new DataStation(String.valueOf(j), data));
                    datasets.add(data);
                }
                CentralStation central = new CentralStation();

                // calculate expected anwser:
                long start = System.currentTimeMillis();
                BigInteger result = central.calculateNPartyScalarProduct(datastations);
                long end = System.currentTimeMillis();
                results.add(new Result(n, end - start));
            }
            long average = Result.average(results);
            double std = Result.std(average, results);
            System.out.println(
                    "N: " + n + " population: " + population + " average: " + average + "ms std: " + std + "ms");
        }
    }
}
