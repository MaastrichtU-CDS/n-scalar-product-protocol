package com.florian.nscalarproduct.notunittests.performance;


import com.florian.nscalarproduct.encryption.Elgamal;
import com.florian.nscalarproduct.encryption.EncryptedElgamal;
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
    private int precision = 0;

    @Test
    public void compareEncryptionVsSSP() {
        //with reltative small populations and high n Encryption is faster
        //With relative large populations and low n SSP is faster
        //E.g. 10 individuals n = 2 encryption is faster
        // 10000 individuals, n = 2, SSP is faster
        // 10000 individuals n= 4 encryption is faster again
        int population = 10000;
        int n = 2;
        List<Server> servers = new ArrayList<>();
        List<ServerEndpoint> endpoints = new ArrayList<>();
        List<BigInteger[]> datasets = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            BigInteger[] data = createData(population);
            for (int j = 0; j < data.length; j++) {
                data[j] = data[j].add(BigInteger.ONE);
            }
            Server server = new Server(String.valueOf(i), data);
            endpoints.add(new ServerEndpoint(server));
            servers.add(server);
            datasets.add(data);
        }

        Server secret = new Server(String.valueOf(n), servers, population, precision);
        ServerEndpoint secretEndpoint = new ServerEndpoint(secret);
        CentralStation central = new CentralStation();

        List<ServerEndpoint> all = new ArrayList<>();
        all.addAll(endpoints);
        all.add(secretEndpoint);
        for (Server s : servers) {
            s.setEndpoints(all);
        }
        secret.setEndpoints(all);

        Protocol prot = new Protocol(endpoints, secretEndpoint, "start", precision);
        long start = System.currentTimeMillis();
        BigInteger result = central.calculateNPartyScalarProduct(prot);
        long end = System.currentTimeMillis();
        System.out.println("Scalar product time: " + (end - start));

        Elgamal main = new Elgamal();
        main.generateKeys();
        Elgamal second = new Elgamal(main.getPublicKey());
        start = System.currentTimeMillis();
        BigInteger res = BigInteger.ZERO;
        for (int i = 0; i < population; i++) {
            EncryptedElgamal temp = main.encrypt(BigInteger.ONE);
            for (int j = 0; j < datasets.size(); j++) {
                EncryptedElgamal eB = second.encrypt(datasets.get(j)[i]);
                temp = EncryptedElgamal.multiply(temp, eB);
            }
            res = res.add(main.decrypt(temp));
        }
        end = System.currentTimeMillis();
        System.out.println("Encrypted time: " + (end - start));
        System.out.println(res + " " + result);

    }


    //TURN THIS OF WHEN BUILDING
    @Test
    public void testCalculateNPartyScalarProduct() {
        int precision = 0;
        for (int n = 2; n < 4; n++) {
            for (int population = 4000; population < 5001; population += 500) {
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
                    Server secret = new Server(String.valueOf(n), servers, population, precision);
                    ServerEndpoint secretEndpoint = new ServerEndpoint(secret);
                    CentralStation central = new CentralStation();

                    List<ServerEndpoint> all = new ArrayList<>();
                    all.addAll(endpoints);
                    all.add(secretEndpoint);
                    for (Server s : servers) {
                        s.setEndpoints(all);
                    }
                    secret.setEndpoints(all);

                    Protocol prot = new Protocol(endpoints, secretEndpoint, "start", precision);
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
