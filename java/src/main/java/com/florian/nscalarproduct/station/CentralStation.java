package com.florian.nscalarproduct.station;

import com.florian.nscalarproduct.webservice.Protocol;
import com.florian.nscalarproduct.webservice.ServerEndpoint;
import org.apache.commons.math3.util.Combinations;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CentralStation {

    public BigInteger calculateNPartyScalarProduct(Protocol prot) {

        List<ServerEndpoint> servers = prot.getServers();
        ServerEndpoint secretServer = prot.getSecretServer();
        String id = prot.getId();
        // determine first datastation:
        ServerEndpoint first = servers.get(0);

        // create list of other datastations
        List<ServerEndpoint> others = servers.stream().filter(x -> x != first).collect(Collectors.toList());

        // retrieve secret
        for (ServerEndpoint s : servers) {
            s.retrieveSecret(id, secretServer.getServerId());
        }

        // calculate partial result first datastation
        // this bit would be a webservice call
        BigInteger partial = first
                .localCalculationFirstParty(id, others.parallelStream().map(x -> x.getObfuscated(id)).collect(
                        Collectors.toList()));

        // calculate partial result nth station
        partial = partial.add(others.parallelStream().map(nth -> calculateNthParty(id, nth, servers))
                                      .reduce(BigInteger.ZERO, BigInteger::add));

        // determine subprotocols
        List<Protocol> subprotocols = determineSubprotocols(servers, secretServer, id);
        /*
         run subprotocols and add results
         It should be possible to parallelize this, butwith the server setup java gets confused and starts throwing
         random nullpointers
        */
        partial = partial.add(subprotocols.stream()
                                      .map(subprotocol -> calculateSubprotocols(subprotocol, servers.size()))
                                      .reduce(BigInteger.ZERO, BigInteger::add));

        // remove V2 and return result
        return first.removeV2(id, partial);
    }

    public List<Protocol> determineSubprotocols(List<ServerEndpoint> servers, ServerEndpoint
            secretServer, String source) {
        // determine Ra combinations:
        int n = servers.size();
        List<Protocol> subProtocols = new ArrayList<>();

        for (int k = 2; k <= n - 1; k++) {
            Combinations combinations = new Combinations(n, k);

            Iterator iterator = combinations.iterator();

            while (iterator.hasNext()) {
                List<Integer> combo = Arrays.stream((int[]) iterator.next()).boxed()
                        .collect(Collectors.toList());
                List<ServerEndpoint> subprotocol = new ArrayList<>();


                // collect datastations not in this collection:
                List<String> serverIds = new ArrayList<>();
                for (int i = 0; i < servers.size(); i++) {
                    if (!combo.contains(i)) {
                        subprotocol.add(servers.get(i));
                        serverIds.add(servers.get(i).getServerId());
                    }
                }
                serverIds.add(secretServer.getServerId());
                //new ID = source ID + the ID's of the selected servers.
                //Source ID ensures a unique path.
                String newId = source + serverIds.stream().map(e -> e.toString()).reduce("", String::concat);

                List<String> ids = new ArrayList<>();
                for (int i : combo) {
                    ids.add(servers.get(i).getServerId());
                }

                //Determine new Secret Server
                //Just pick the first one out of the servers not providing a Data here.
                String selectedIds = ids.get(0);
                ServerEndpoint selected = null;
                for (ServerEndpoint s : servers) {
                    if (s.getServerId().equals(selectedIds)) {
                        selected = s;
                    }
                }
                selected.addSecretStation(newId, serverIds, servers.get(0).getPopulation());

                for (ServerEndpoint s : subprotocol) {
                    s.addDatastation(newId, source);
                }
                secretServer.addDataFromSecret(newId, source, ids);
                subprotocol.add(secretServer);
                subProtocols.add(new Protocol(subprotocol, selected, newId));
            }
        }
        return subProtocols;
    }

    private BigInteger calculateNthParty(String id, ServerEndpoint nth, List<ServerEndpoint> servers) {
        List<ServerEndpoint> setMinusN = servers.stream().filter(x -> x != nth).collect(Collectors.toList());
        //this bit would be a webservice call.
        return nth.localCalculationNthParty(id, setMinusN.stream().map(x -> x.getObfuscated(id)).collect(
                Collectors.toList()));
    }

    private BigInteger calculateSubprotocols(Protocol subprotocol, int parentSize) {
        // each subprotocol needs to be multiplied by the difference in size with the current protocol
        // e.g.a 4-party protocol with A, B C & D will have 2 subprotocols of ABRcRd and 1 with ARbRcRd
        // so factor this in
        BigInteger nFactor = BigInteger.valueOf(parentSize - subprotocol.getServers().size());
        // this bit would be a webservice call
        return calculateNPartyScalarProduct(subprotocol).multiply(nFactor);
    }

}
