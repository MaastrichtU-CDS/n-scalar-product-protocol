package com.florian.nscalarproduct.station;

import com.florian.nscalarproduct.encryption.Paillier;
import com.florian.nscalarproduct.encryption.PublicPaillierKey;
import com.florian.nscalarproduct.util.Util;
import com.florian.nscalarproduct.webservice.Protocol;
import com.florian.nscalarproduct.webservice.Server;
import com.florian.nscalarproduct.webservice.ServerEndpoint;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CentralStationTest {
    int precision = 0;
    int count = 30;

    @Test
    public void testCalculateNPartyScalarProduct() {

        for (int n = 2; n <= 5; n++) {
            for (int population = 10; population < 10000; population *= 10) {
                long nRun = 0;
                long pallierRun = 0;
                for (int j = 0; j < count; j++) {
                    List<Server> servers = new ArrayList<>();
                    List<ServerEndpoint> endpoints = new ArrayList<>();
                    List<BigInteger[]> datasets = new ArrayList<>();

                    for (int i = 0; i < n; i++) {
                        BigInteger[] data = DataStationTest.createData(population);
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

                    // calculate expected anwser:
                    BigInteger expected = Util.matrixDiagonalMultiplication(datasets, datasets.get(0).length);

                    long start = System.currentTimeMillis();
                    Protocol prot = new Protocol(endpoints, secretEndpoint, "start", precision);
                    BigInteger result = central.calculateNPartyScalarProduct(prot);
                    assertEquals(expected, result);
                    nRun += (System.currentTimeMillis() - start);
                    start = System.currentTimeMillis();

                    // use pallier
                    Paillier paillier = new Paillier();
                    paillier.generateKeyPair();
                    PublicPaillierKey p = paillier.getPublicKey();
                    List<BigInteger> first = firstPallierCalculation(p, datasets.get(0));

                    for (int i = 1; i < datasets.size(); i++) {
                        List<BigInteger> temp = localPallierCalculation(first, p, datasets.get(i));
                        first = temp;
                    }

                    BigInteger x = BigInteger.ONE;
                    for (int i = 0; i < first.size(); i++) {
                        x = x.multiply(first.get(i));
                    }
                    BigInteger res = paillier.decrypt(x);
                    assertEquals(expected, paillier.decrypt(x));
                    pallierRun += (System.currentTimeMillis() - start);
                }
                System.out.println("Parties: " + n + " population: " + population);
                System.out.println("N-party: " + nRun / count);
                System.out.println("Paillier: " + pallierRun / count);
            }
        }
    }

    @Test
    public void testCompareBiggerNumbers() {
        for (int max = 7; max <= 11; max++) {
            for (int n = 2; n <= 5; n++) {
                for (int population = 1000; population < 10000; population *= 10) {
                    long nRun = 0;
                    long pallierRun = 0;
                    for (int j = 0; j < count; j++) {
                        List<Server> servers = new ArrayList<>();
                        List<ServerEndpoint> endpoints = new ArrayList<>();
                        List<BigInteger[]> datasets = new ArrayList<>();

                        for (int i = 0; i < n; i++) {
                            BigInteger[] data = DataStationTest.createDataFrom2ToMax(population, max);
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

                        // calculate expected anwser:
                        BigInteger expected = Util.matrixDiagonalMultiplication(datasets, datasets.get(0).length);

                        long start = System.currentTimeMillis();
                        Protocol prot = new Protocol(endpoints, secretEndpoint, "start", precision);
                        BigInteger result = central.calculateNPartyScalarProduct(prot);
                        assertEquals(expected, result);
                        nRun += (System.currentTimeMillis() - start);
                        start = System.currentTimeMillis();

                        // use pallier
                        Paillier paillier = new Paillier();
                        paillier.generateKeyPair();
                        PublicPaillierKey p = paillier.getPublicKey();
                        List<BigInteger> first = firstPallierCalculation(p, datasets.get(0));

                        for (int i = 1; i < datasets.size(); i++) {
                            List<BigInteger> temp = localPallierCalculation(first, p, datasets.get(i));
                            first = temp;
                        }

                        BigInteger x = BigInteger.ONE;
                        for (int i = 0; i < first.size(); i++) {
                            x = x.multiply(first.get(i));
                        }
                        BigInteger res = paillier.decrypt(x);
                        assertEquals(expected, paillier.decrypt(x));
                        pallierRun += (System.currentTimeMillis() - start);
                    }

                    System.out.println("Parties: " + n + " population: " + population + " Number " + (max - 1));
                    System.out.println("N-party: " + nRun / count);
                    System.out.println("Paillier: " + pallierRun / count);
                }
            }
        }
    }


    @Test
    public void testCalculateNPartyScalarProductDecimalValues() {
        for (int precision = 1; precision <= 5; precision++) {
            for (int n = 2; n <= 5; n++) {
                double multiplier = Math.pow(10, precision);
                double combinedMultiplier = Math.pow(multiplier, n);
                //multiplier = precision ^ n
                for (int population = 10; population < 100000; population *= 10) {
                    long runtimeNparty = 0;
                    long runtimePaillier = 0;
                    for (int z = 0; z < count; z++) {

                        List<Server> servers = new ArrayList<>();
                        List<ServerEndpoint> endpoints = new ArrayList<>();
                        List<BigDecimal[]> datasets = new ArrayList<>();
                        ;
                        for (int i = 0; i < n; i++) {
                            BigDecimal[] data = DataStationTest.createDoubleData(precision, population);
                            BigInteger[] dataIntegers = new BigInteger[population];
                            for (int j = 0; j < data.length; j++) {
                                dataIntegers[j] = BigInteger.valueOf(
                                        data[j].multiply(new BigDecimal(multiplier)).longValue());
                            }
                            Server server = new Server(String.valueOf(i), dataIntegers);
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
                        // calculate expected anwser:
                        BigDecimal expected = Util.matrixDiagonalMultiplicationDecimal(datasets,
                                                                                       datasets.get(0).length);

                        Protocol prot = new Protocol(endpoints, secretEndpoint, "start", precision);

                        long start = System.currentTimeMillis();
                        BigInteger result = central.calculateNPartyScalarProduct(prot);
                        BigDecimal resultDec = new BigDecimal(result.toString()).divide(
                                BigDecimal.valueOf(combinedMultiplier));

                        //Check if the difference between expected and result is small enough
                        //Do not directly check double and long values cuz there's weird stuff that can happen there
                        assertEquals(0, resultDec.subtract(expected).doubleValue(), Math.pow(10, -precision));
                        runtimeNparty += System.currentTimeMillis() - start;

                        // use pallier
                        start = System.currentTimeMillis();
                        Paillier paillier = new Paillier();
                        paillier.generateKeyPair();
                        PublicPaillierKey p = paillier.getPublicKey();
                        BigInteger[] dataIntegers = new BigInteger[population];
                        for (int j = 0; j < datasets.get(0).length; j++) {
                            dataIntegers[j] = BigInteger.valueOf(
                                    datasets.get(0)[j].multiply(new BigDecimal(multiplier)).longValue());
                        }
                        List<BigInteger> first = firstPallierCalculation(p, dataIntegers);

                        for (int i = 1; i < datasets.size(); i++) {
                            for (int j = 0; j < datasets.get(0).length; j++) {
                                dataIntegers[j] = BigInteger.valueOf(
                                        datasets.get(i)[j].multiply(new BigDecimal(multiplier)).longValue());
                            }
                            List<BigInteger> temp = localPallierCalculation(first, p, dataIntegers);
                            first = temp;
                        }

                        BigInteger x = BigInteger.ONE;
                        for (int i = 0; i < first.size(); i++) {
                            x = x.multiply(first.get(i));
                        }
                        BigInteger res = paillier.decrypt(x);
                        BigDecimal resultDecPallier = new BigDecimal(res).divide(
                                BigDecimal.valueOf(combinedMultiplier));
                        assertEquals(resultDec, resultDecPallier);
                        runtimePaillier += System.currentTimeMillis() - start;
                    }
                    System.out.println("Parties: " + n + " population: " + population + " precision " + precision);
                    System.out.println("N-party: " + runtimeNparty / count);
                    System.out.println("Paillier: " + runtimePaillier / count);
                }
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
            Server secret = new Server(String.valueOf(population), servers, population, precision);
            ServerEndpoint secretEndpoint = new ServerEndpoint(secret);
            CentralStation central = new CentralStation();

            List<ServerEndpoint> all = new ArrayList<>();
            all.addAll(endpoints);
            all.add(secretEndpoint);
            for (Server s : servers) {
                s.setEndpoints(all);
            }
            secret.setEndpoints(all);

            List<Protocol> subprotocols = central.determineSubprotocols(endpoints, secretEndpoint, "start", precision);
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

            Server secret = new Server(String.valueOf(n), servers, n, precision);
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
        List<Protocol> subprotocols = central.determineSubprotocols(servers, secretServer, source, precision);
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

    private List<BigInteger> firstPallierCalculation(PublicPaillierKey key, BigInteger[] localData) {
        List<BigInteger> obfuscated = new ArrayList<>();
        for (int i = 0; i < localData.length; i++) {
            obfuscated.add(key.encrypt(localData[i]));
        }

        return obfuscated;
    }

    public List<BigInteger> localPallierCalculation(List<BigInteger> obfuscated, PublicPaillierKey key,
                                                    BigInteger[] localData) {
        for (int i = 0; i < obfuscated.size(); i++) {
            BigInteger old = obfuscated.get(i);
            BigInteger n = BigInteger.ZERO;
            if (localData[i].equals(BigInteger.ZERO)) {
                n = key.encrypt(localData[i]);
            } else if (localData[i].equals(BigInteger.ONE)) {
                n = obfuscated.get(i).multiply(key.encrypt(BigInteger.ZERO));
            } else {
                n = obfuscated.get(i).pow(localData[i].intValue());
            }
            obfuscated.set(i, n);
        }

        return obfuscated;
    }
}