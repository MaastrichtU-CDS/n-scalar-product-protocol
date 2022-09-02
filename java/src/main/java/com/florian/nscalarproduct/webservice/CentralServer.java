package com.florian.nscalarproduct.webservice;

import com.florian.nscalarproduct.station.CentralStation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CentralServer {
    protected static final RestTemplate REST_TEMPLATE = new RestTemplate();

    @Value ("${servers}")
    protected List<String> servers;
    @Value ("${secretServer}")
    protected String secretServer;

    @GetMapping ("nparty")
    public BigInteger nParty(@RequestBody int precision) {
        //Starting point, this can be called on the central node in vantage6
        //this is just an example, actual use might need to use their own implementation due to how the secret should be
        //initialized

        List<ServerEndpoint> endpoints = new ArrayList<>();

        for (String s : servers) {
            endpoints.add(new ServerEndpoint(s));
            initData(s);
        }

        Integer population = endpoints.get(0).getPopulation();

        REST_TEMPLATE.put(secretServer + "/initRandom", population);
        ServerEndpoint secret = new ServerEndpoint(secretServer);

        CentralStation station = new CentralStation();

        Protocol prot = new Protocol(endpoints, secret, "start", precision);
        return station.calculateNPartyScalarProduct(prot);
    }

    public void initData(String s) {
        REST_TEMPLATE.put(s + "/initData", "");
    }
}
