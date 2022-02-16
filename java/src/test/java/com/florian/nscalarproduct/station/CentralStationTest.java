package com.florian.nscalarproduct.station;

import com.florian.nscalarproduct.util.Util;
import com.florian.nscalarproduct.webservice.Protocol;
import com.florian.nscalarproduct.webservice.Server;
import com.florian.nscalarproduct.webservice.ServerEndpoint;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CentralStationTest {

    @Test
    public void testCalculateNPartyScalarProduct() {
        for (int n = 2; n < 5; n++) {
            for (int population = 2; population < 10; population++) {
                List<Server> servers = new ArrayList<>();
                List<ServerEndpoint> endpoints = new ArrayList<>();
                List<BigInteger[]> datasets = new ArrayList<>();
                ;
                for (int i = 0; i < n; i++) {
                    BigInteger[] data = DataStationTest.createData(population);
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
                BigInteger expected = Util.matrixDiagonalMultiplication(datasets, datasets.get(0).length);

                Protocol prot = new Protocol(endpoints, secretEndpoint, "start");
                BigInteger result = central.calculateNPartyScalarProduct(prot);
                assertEquals(expected, result);
            }
        }
    }

    @Test
    public void testDetermineSubprotocols() {
        for (int population = 1; population < 10; population++) {
            List<Server> servers = new ArrayList<>();
            List<ServerEndpoint> endpoints = new ArrayList<>();
            List<BigInteger[]> datasets = new ArrayList<>();
            ;
            for (int i = 0; i < population; i++) {
                BigInteger[] data = DataStationTest.createData(population);
                Server server = new Server(String.valueOf(i), data);
                endpoints.add(new ServerEndpoint(server));
                servers.add(server);
                datasets.add(data);
            }
            Server secret = new Server(String.valueOf(population), servers, population);
            ServerEndpoint secretEndpoint = new ServerEndpoint(secret);
            CentralStation central = new CentralStation();

            List<ServerEndpoint> all = new ArrayList<>();
            all.addAll(endpoints);
            all.add(secretEndpoint);
            for (Server s : servers) {
                s.setEndpoints(all);
            }
            secret.setEndpoints(all);

            List<Protocol> subprotocols = central.determineSubprotocols(endpoints, secretEndpoint, "start");
            assertEquals(subprotocols.size(), calculateExpectedCombinations(population).longValue());
        }
    }

    @Test
    public void scaling() {
        for (int n = 2; n < 10; n++) {
            Res r = new Res();
            r.messages = calMessages(n);
            r.protocols = 1;

            List<Server> servers = new ArrayList<>();
            List<ServerEndpoint> endpoints = new ArrayList<>();
            List<BigInteger[]> datasets = new ArrayList<>();
            ;
            for (int i = 0; i < n; i++) {
                BigInteger[] data = DataStationTest.createData(n);
                Server server = new Server(String.valueOf(i), data);
                endpoints.add(new ServerEndpoint(server));
                servers.add(server);
                datasets.add(data);
            }

            Server secret = new Server(String.valueOf(n), servers, n);
            ServerEndpoint secretEndpoint = new ServerEndpoint(secret);

            List<ServerEndpoint> all = new ArrayList<>();
            all.addAll(endpoints);
            all.add(secretEndpoint);
            for (Server s : servers) {
                s.setEndpoints(all);
            }
            secret.setEndpoints(all);

            determineSubProtocols(endpoints, secretEndpoint, "start", r);

            System.out.println("N: " + n + " protocols: " + r.protocols + " messages: " + r.messages);

        }
    }

    private void determineSubProtocols(List<ServerEndpoint> servers, ServerEndpoint
            secretServer, String source, Res r) {
        CentralStation central = new CentralStation();
        List<Protocol> subprotocols = central.determineSubprotocols(servers, secretServer, source);
        for (Protocol p : subprotocols) {
            r.protocols++;
            r.messages += calMessages(p.getServers().size());
            determineSubProtocols(p.getServers(), p.getSecretServer(), p.getId(), r);
        }
    }

    private class Res {
        int protocols;
        int messages;
    }

    private int calMessages(int n) {
        return n + n * n;
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