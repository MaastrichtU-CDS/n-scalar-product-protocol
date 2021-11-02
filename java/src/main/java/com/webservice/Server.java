package com.webservice;

import com.station.CentralStation;
import com.station.DataStation;
import com.station.SecretStation;
import com.webservice.domain.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class Server {
    private Map<String, DataStation> dataStations = new HashMap<>();
    private Map<String, SecretStation> secretStations = new HashMap<>();
    @Value ("${server}")
    private String serverId;
    private BigInteger[] localData;
    private int population;

    private List<ServerEndpoint> endpoints = new ArrayList<>();

    @Value ("${servers}")
    private List<String> servers;

    public void setEndpoints(List<ServerEndpoint> endpoints) {
        this.endpoints = endpoints;
    }

    @PutMapping ("InitData")
    public void InitData() {
        reset();
        localData = new BigInteger[3];
        localData[0] = BigInteger.ZERO;
        localData[1] = BigInteger.ZERO;
        localData[2] = BigInteger.ONE;
        for (String s : servers) {
            endpoints.add(new ServerEndpoint(s));
        }
        dataStations.put("start", new DataStation(serverId, localData));
        this.population = 3;
    }

    public void reset() {
        dataStations = new HashMap<>();
        secretStations = new HashMap<>();
        localData = null;
    }

    @PutMapping ("Random")
    public void InitRandom() {
        reset();
        for (String s : servers) {
            endpoints.add(new ServerEndpoint(s));
        }
        secretStations.put("start", new SecretStation(endpoints.stream().map(s -> s.getServerId()).collect(
                Collectors.toList()), 3));
        this.population = 3;
    }

    @GetMapping ("nparty")
    public BigInteger nparty() {
        RestTemplate REST_TEMPLATE = new RestTemplate();
        REST_TEMPLATE.put("http://localhost:8080/Random", "");
        REST_TEMPLATE.put("http://localhost:8081/InitData", "");
        REST_TEMPLATE.put("http://localhost:8082/InitData", "");
        REST_TEMPLATE.put("http://localhost:8083/InitData", "");

        CentralStation station = new CentralStation();
        List<ServerEndpoint> servers = new ArrayList<>();
        ServerEndpoint secret = null;
        for (ServerEndpoint s : endpoints) {
            if (!s.getServerId().equals("0")) {
                servers.add(s);
            } else {
                secret = s;
            }
        }
        Protocol prot = new Protocol(servers, secret, "start");
        return station.calculateNPartyScalarProduct(prot);
    }

    public Server() {
    }

    public Server(String serverId, List<Server> servers, int length) {
        this.serverId = serverId;
        secretStations.put("start", new SecretStation(servers.stream().map(s -> s.getServerId()).collect(
                Collectors.toList()), length));
        this.population = length;
    }

    public Server(String serverId, BigInteger[] data) {
        this.serverId = serverId;
        this.localData = data;
        dataStations.put("start", new DataStation(serverId, localData));
        this.population = data.length;
    }

    //For sharing local secret externally
    @PostMapping ("getSecretPart")
    public SecretPartResponse getSecretPart(@RequestBody GetSecretPartRequest req) {
        SecretPartResponse response = new SecretPartResponse();
        if (secretStations.get(req.getId()).getPart(req.getServerId()) == null) {
            System.out.println("");
        }
        response.setSecretPart(secretStations.get(req.getId()).getPart(req.getServerId()));
        return response;
    }

    //For retrieving the local secret from an external source
    @PutMapping ("retrieveSecret")
    public void retrieveSecret(@RequestBody RetrieveSecretRequest req) {
        for (ServerEndpoint end : endpoints) {
            if (end.getServerId().equals(req.getSource())) {
                if (end.getSecretPart(req.getId(), serverId) == null) {
                    return;
                }
                dataStations.get(req.getId()).setLocalSecret(end.getSecretPart(req.getId(), serverId));
            }
        }
    }

    @PutMapping ("addSecretStation")
    public void addSecretStation(@RequestBody AddSecretRequest req) {
        secretStations.put(req.getId(), new SecretStation(req.getServerIds(), req.getLength()));
    }

    @PutMapping ("addDatastation")
    public void addDatastation(@RequestBody AddDataStationRequest req) {
        if (dataStations.get(req.getSource()) != null) {
            dataStations.put(req.getId(), new DataStation(dataStations.get(req.getSource())));
        } else {
            dataStations.put(req.getId(), new DataStation(serverId, localData));
        }
    }

    @PutMapping ("addDataFromSecret")
    public void addDataFromSecret(@RequestBody AddDataFromSecretRequest req) {
        dataStations.put(req.getId(),
                         secretStations.get(req.getSource()).generateDataStation(req.getSubset(), serverId));
    }

    @PostMapping ("localCalculationFirstParty")
    public BigInteger localCalculationFirstParty(@RequestBody LocalCalculationPartyRequest req) {
        return dataStations.get(req.getId()).localCalculationFirstParty(req.getObfuscated());
    }

    @PostMapping ("removeV2")
    public BigInteger removeV2(@RequestBody RemoveV2Request req) {
        return dataStations.get(req.getId()).removeV2(req.getPartial());
    }

    @PostMapping ("localCalculationNthParty")
    public BigInteger localCalculationNthParty(@RequestBody LocalCalculationPartyRequest req) {
        return dataStations.get(req.getId()).localCalculationNthParty(req.getObfuscated());
    }

    @PostMapping ("getObfuscated")
    public ObfuscatedResponse getObfuscated(@RequestBody NPartyRequest req) {
        ObfuscatedResponse res = new ObfuscatedResponse();
        res.setObfuscated(dataStations.get(req.getId()).getObfuscated());
        return res;
    }

    @GetMapping ("getServerId")
    public String getServerId() {
        return serverId;
    }

    @GetMapping ("getPopulation")
    public int getPopulation() {
        return population;
    }
}
