package webservice;

import secret.SecretPart;
import station.DataStation;
import station.SecretStation;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Server {
    private Map<String, DataStation> dataStations = new HashMap<>();
    private Map<String, SecretStation> secretStations = new HashMap<>();
    private String serverId;
    private BigInteger[] localData;
    private int population;

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
    public SecretPart getSecretPart(String id, String serverId) {
        return secretStations.get(id).getPart(serverId);
    }

    //For retrieving the local secret from an external source
    public void retrieveSecret(String id, Server source) {
        dataStations.get(id).setLocalSecret(source.getSecretPart(id, serverId));
    }

    public void addSecretStation(String id, List<String> serverIds, int length) {
        secretStations.put(id, new SecretStation(serverIds, length));
    }

    public void addDatastation(String id) {
        if (localData == null) {
            return;
        }
        dataStations.put(id, new DataStation(serverId, localData));
    }

    public void addDatastation(String id, String source) {
        if (dataStations.get(source) != null) {
            dataStations.put(id, new DataStation(dataStations.get(source)));
        } else {
            dataStations.put(id, new DataStation(serverId, localData));
        }
    }


    public void addDataFromSecret(String id, String source, List<String> subset) {
        dataStations.put(id, secretStations.get(source).generateDataStation(subset, serverId));
    }

    public BigInteger localCalculationFirstParty(String id, List<BigInteger[]> obfuscated) {
        return dataStations.get(id).localCalculationFirstParty(obfuscated);
    }

    public BigInteger removeV2(String id, BigInteger partial) {
        return dataStations.get(id).removeV2(partial);
    }

    public BigInteger localCalculationNthParty(String id, List<BigInteger[]> obfuscated) {
        return dataStations.get(id).localCalculationNthParty(obfuscated);
    }

    public BigInteger[] getObfuscated(String id) {
        return dataStations.get(id).getObfuscated();
    }

    public String getServerId() {
        return serverId;
    }

    public int getPopulation() {
        return population;
    }
}
