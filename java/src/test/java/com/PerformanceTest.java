package com;

import com.station.CentralStation;
import com.webservice.Protocol;
import com.webservice.Server;
import com.webservice.ServerEndpoint;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.station.DataStationTest.createData;
import static com.util.Util.matrixDiagonalMultiplication;

public class PerformanceTest {

    @Test
    public void testCalculateNPartyScalarProduct() {
        for (int n = 2; n < 5; n++) {
            long sum = 0;
            for (int population = 4000; population < 4001; population++) {
                List<Server> servers = new ArrayList<>();
                List<ServerEndpoint> endpoints = new ArrayList<>();
                List<BigInteger[]> datasets = new ArrayList<>();

                for (int i = 0; i < n; i++) {
                    BigInteger[] data = createData(population);
                    Server server = new Server(String.valueOf(i), data);
                    endpoints.add(new ServerEndpoint(server));
                    servers.add(server);
                    datasets.add(data);
                }
                Server secret = new Server(String.valueOf(n), servers, population);
                ServerEndpoint secretEndpoint = new ServerEndpoint(secret);
                CentralStation central = new CentralStation();

                List<ServerEndpoint> all = new ArrayList<>();
                all.addAll(endpoints);
                all.add(secretEndpoint);
                for (Server s : servers) {
                    s.setEndpoints(all);
                }
                secret.setEndpoints(all);

                // calculate expected anwser:
                BigInteger expected = matrixDiagonalMultiplication(datasets, datasets.get(0).length);

                Protocol prot = new Protocol(endpoints, secretEndpoint, "start");
                long start = System.currentTimeMillis();
                BigInteger result = central.calculateNPartyScalarProduct(prot);
                long end = System.currentTimeMillis();
                System.out.println("N: " + n + " pop: " + population + " time: " + (end - start));
            }

        }
    }
}
