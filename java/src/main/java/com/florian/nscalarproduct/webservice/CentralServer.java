package com.florian.nscalarproduct.webservice;

import com.florian.nscalarproduct.station.CentralStation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CentralServer {

    @Value ("${servers}")
    private List<String> servers;
    @Value ("${secretServer}")
    private String secretServer;

    @GetMapping ("nparty")
    public BigInteger nParty() {
        //Starting point, this can be called on the central node in vantage6
        RestTemplate restTemplate = new RestTemplate();
        List<ServerEndpoint> endpoints = new ArrayList<>();

        for (String s : servers) {
            restTemplate.put(s + "/initData", "");
            endpoints.add(new ServerEndpoint(s));
        }

        Integer population = endpoints.get(0).getPopulation();

        restTemplate.put(secretServer + "/initRandom", population);
        ServerEndpoint secret = new ServerEndpoint(secretServer);

        CentralStation station = new CentralStation();

        Protocol prot = new Protocol(endpoints, secret, "start");
        return station.calculateNPartyScalarProduct(prot);
    }
}
