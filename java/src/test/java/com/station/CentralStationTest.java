package com.station;

import java.math.BigInteger;

public class CentralStationTest {

//    @Test
//    public void testCalculateNPartyScalarProduct() {
//        for (int n = 6; n < 8; n++) {
//            for (int population = 2; population < 10; population++) {
//                List<Server> servers = new ArrayList<>();
//                List<BigInteger[]> datasets = new ArrayList<>();
//                ;
//                for (int i = 0; i < n; i++) {
//                    BigInteger[] data = createData(population);
//                    servers.add(new Server(String.valueOf(i), data));
//                    datasets.add(data);
//                }
//                Server secret = new Server(String.valueOf(n), servers, population);
//                CentralStation central = new CentralStation();
//
//                // calculate expected anwser:
//                BigInteger expected = matrixDiagonalMultiplication(datasets, datasets.get(0).length);
//
//                Protocol prot = new Protocol(servers, secret, "start");
//                BigInteger result = central.calculateNPartyScalarProduct(prot);
//                assertEquals(expected, result);
//            }
//        }
//    }

//    @Test
//    public void testDetermineSubprotocols() {
//        for (int population = 1; population < 10; population++) {
//            List<ServerEndpoint> servers = new ArrayList<>();
//            for (int i = 0; i < population; i++) {
//                BigInteger[] data = createData(population);
//                servers.add(new ServerEndpoint(String.valueOf(i)));
//            }
//            String secretId = String.valueOf(population);
//            ServerEndpoint secret = new ServerEndpoint(String.valueOf(population));
//            CentralStation central = new CentralStation();
//            // retrieve secret
//            for (ServerEndpoint s : servers) {
//                s.retrieveSecret("start", secret);
//            }
//
//            List<Protocol> subprotocols = central.determineSubprotocols(servers, secret, "start");
//            assertEquals(subprotocols.size(), calculateExpectedCombinations(population).longValue());
//        }
//    }

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