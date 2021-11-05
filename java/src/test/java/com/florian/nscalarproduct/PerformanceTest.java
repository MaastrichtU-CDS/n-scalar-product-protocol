package com.florian.nscalarproduct;


import com.florian.nscalarproduct.station.CentralStation;
import com.florian.nscalarproduct.webservice.Protocol;
import com.florian.nscalarproduct.webservice.Server;
import com.florian.nscalarproduct.webservice.ServerEndpoint;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.florian.nscalarproduct.station.DataStationTest.createData;


public class PerformanceTest {

    //TURN THIS OF WHEN BUILDING
    @Test
    public void testCalculateNPartyScalarProduct() {
        for (int n = 2; n < 3; n++) {
            for (int population = 2500; population < 5001; population += 500) {
                List<Long> duration = new ArrayList<>();
                for (int j = 0; j < 100; j++) {
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

                    Protocol prot = new Protocol(endpoints, secretEndpoint, "start");
                    long start = System.currentTimeMillis();
                    BigInteger result = central.calculateNPartyScalarProduct(prot);
                    long end = System.currentTimeMillis();

                    duration.add(end - start);

                }
                long average = duration.stream().reduce(Long::sum).get() / duration.size();
                double std = Math.sqrt((double) duration.stream().map(x -> ((x - average) * (x - average)))
                        .collect(Collectors.toList())
                        .stream()
                        .reduce(Long::sum).get() / duration.size());
                System.out.println("N: " + n + " pop: " + population + " time: " + average + " std: " + std);
            }
        }
    }

}
