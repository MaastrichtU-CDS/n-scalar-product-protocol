package com.webservice;

import com.secret.SecretPart;
import com.webservice.domain.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.List;

public class ServerEndpoint {
    private final RestTemplate REST_TEMPLATE = new RestTemplate();
    private final String SERVERRURL;
    private final Server SERVER;
    private final boolean TESTING;
    //ToDo figure out how to  automatically remove the if-statements that use this flag in production code

    public ServerEndpoint(Server server) {
        this.SERVER = server;
        this.SERVERRURL = "";
        this.TESTING = true;
    }

    public ServerEndpoint(String url) {
        this.SERVERRURL = url;
        this.SERVER = null;
        this.TESTING = false;
    }

    //For sharing local secret externally
    public SecretPart getSecretPart(String id, String serverId) {
        GetSecretPartRequest req = new GetSecretPartRequest();
        req.setId(id);
        req.setServerId(serverId);
        if (TESTING) {
            return SERVER.getSecretPart(req).getSecretPart();
        }
        return REST_TEMPLATE.postForObject(SERVERRURL + "/getSecretPart", req, SecretPartResponse.class)
                .getSecretPart();
    }

    //For retrieving the local secret from an external source
    public void retrieveSecret(String id, String source) {
        RetrieveSecretRequest req = new RetrieveSecretRequest();
        req.setId(id);
        req.setSource(source);
        if (TESTING) {
            SERVER.retrieveSecret(req);
        } else {
            REST_TEMPLATE.put(SERVERRURL + "/retrieveSecret", req);
        }
    }

    public void addSecretStation(String id, List<String> serverIds, int length) {
        AddSecretRequest req = new AddSecretRequest();
        req.setId(id);
        req.setServerIds(serverIds);
        req.setLength(length);
        if (TESTING) {
            SERVER.addSecretStation(req);
        } else {
            REST_TEMPLATE.put(SERVERRURL + "/addSecretStation", req);
        }
    }

    public void addDatastation(String id, String source) {
        AddDataStationRequest req = new AddDataStationRequest();
        req.setId(id);
        req.setSource(source);
        if (TESTING) {
            SERVER.addDatastation(req);
        } else {
            REST_TEMPLATE.put(SERVERRURL + "/addDatastation", req);
        }
    }


    public void addDataFromSecret(String id, String source, List<String> subset) {
        AddDataFromSecretRequest req = new AddDataFromSecretRequest();
        req.setId(id);
        req.setSubset(subset);
        req.setSource(source);
        if (TESTING) {
            SERVER.addDataFromSecret(req);
        } else {
            REST_TEMPLATE.put(SERVERRURL + "/addDataFromSecret", req);
        }
    }

    public BigInteger localCalculationFirstParty(String id, List<BigInteger[]> obfuscated) {
        LocalCalculationPartyRequest req = new LocalCalculationPartyRequest();
        req.setId(id);
        req.setObfuscated(obfuscated);
        if (TESTING) {
            return SERVER.localCalculationFirstParty(req);
        }
        return REST_TEMPLATE.postForObject(SERVERRURL + "/localCalculationFirstParty", req, BigInteger.class);
    }

    public BigInteger removeV2(String id, BigInteger partial) {
        RemoveV2Request req = new RemoveV2Request();
        req.setId(id);
        req.setPartial(partial);
        if (TESTING) {
            return SERVER.removeV2(req);
        }
        return REST_TEMPLATE.postForObject(SERVERRURL + "/removeV2", req, BigInteger.class);
    }

    public BigInteger localCalculationNthParty(String id, List<BigInteger[]> obfuscated) {
        LocalCalculationPartyRequest req = new LocalCalculationPartyRequest();
        req.setId(id);
        req.setObfuscated(obfuscated);
        if (TESTING) {
            return SERVER.localCalculationNthParty(req);
        }
        return REST_TEMPLATE.postForObject(SERVERRURL + "/localCalculationNthParty", req, BigInteger.class);
    }

    public BigInteger[] getObfuscated(String id) {
        NPartyRequest req = new NPartyRequest();
        req.setId(id);
        if (TESTING) {
            return SERVER.getObfuscated(req).getObfuscated();
        }
        return REST_TEMPLATE.postForObject(SERVERRURL + "/getObfuscated", req, ObfuscatedResponse.class)
                .getObfuscated();
    }

    public String getServerId() {
        if (TESTING) {
            return SERVER.getServerId();
        }
        return REST_TEMPLATE.getForEntity(SERVERRURL + "/getServerId", String.class).getBody();
    }

    public int getPopulation() {
        if (TESTING) {
            return SERVER.getPopulation();
        }
        return REST_TEMPLATE.getForEntity(SERVERRURL + "/getPopulation", Integer.class).getBody();
    }
}
