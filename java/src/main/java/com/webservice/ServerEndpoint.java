package com.webservice;

import com.secret.SecretPart;
import com.webservice.domain.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.List;

public class ServerEndpoint {
    private final RestTemplate REST_TEMPLATE = new RestTemplate();
    private final String SERVERRURL;

    public ServerEndpoint(String url) {
        this.SERVERRURL = url;
    }

    //For sharing local secret externally
    public SecretPart getSecretPart(String id, String serverId) {
        GetSecretPartRequest req = new GetSecretPartRequest();
        req.setId(id);
        req.setServerId(getServerId());
        return REST_TEMPLATE.postForObject(SERVERRURL + "/getSecretPart", req, SecretPartResponse.class)
                .getSecretPart();
    }

    //For retrieving the local secret from an external source
    public void retrieveSecret(String id, String source) {
        RetrieveSecretRequest req = new RetrieveSecretRequest();
        req.setId(id);
        req.setSource(source);
        REST_TEMPLATE.put(SERVERRURL + "/retrieveSecret", req);
    }

    public void addSecretStation(String id, List<String> serverIds, int length) {
        AddSecretRequest req = new AddSecretRequest();
        req.setId(id);
        req.setServerIds(serverIds);
        req.setLength(length);
        REST_TEMPLATE.put(SERVERRURL + "/addSecretStation", req);
    }

    public void addDatastation(String id, String source) {
        AddDataStationRequest req = new AddDataStationRequest();
        req.setId(id);
        req.setSource(source);
        REST_TEMPLATE.put(SERVERRURL + "/addDatastation", req);
    }


    public void addDataFromSecret(String id, String source, List<String> subset) {
        AddDataFromSecretRequest req = new AddDataFromSecretRequest();
        req.setId(id);
        req.setSubset(subset);
        req.setSource(source);
        REST_TEMPLATE.put(SERVERRURL + "/addDataFromSecret", req);
    }

    public BigInteger localCalculationFirstParty(String id, List<BigInteger[]> obfuscated) {
        LocalCalculationPartyRequest req = new LocalCalculationPartyRequest();
        req.setId(id);
        req.setObfuscated(obfuscated);
        return REST_TEMPLATE.postForObject(SERVERRURL + "/localCalculationFirstParty", req, BigInteger.class);
    }

    public BigInteger removeV2(String id, BigInteger partial) {
        RemoveV2Request req = new RemoveV2Request();
        req.setId(id);
        req.setPartial(partial);
        return REST_TEMPLATE.postForObject(SERVERRURL + "/removeV2", req, BigInteger.class);
    }

    public BigInteger localCalculationNthParty(String id, List<BigInteger[]> obfuscated) {
        LocalCalculationPartyRequest req = new LocalCalculationPartyRequest();
        req.setId(id);
        req.setObfuscated(obfuscated);
        return REST_TEMPLATE.postForObject(SERVERRURL + "/localCalculationNthParty", req, BigInteger.class);
    }

    public BigInteger[] getObfuscated(String id) {
        NPartyRequest req = new NPartyRequest();
        req.setId(id);
        return REST_TEMPLATE.postForObject(SERVERRURL + "/getObfuscated", req, ObfuscatedResponse.class)
                .getObfuscated();
    }

    public String getServerId() {
        return REST_TEMPLATE.getForEntity(SERVERRURL + "/getServerId", String.class).getBody();
    }

    public int getPopulation() {
        return REST_TEMPLATE.getForEntity(SERVERRURL + "/getPopulation", Integer.class).getBody();
    }
}
