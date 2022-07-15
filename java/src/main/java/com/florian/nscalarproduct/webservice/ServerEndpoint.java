package com.florian.nscalarproduct.webservice;

import com.florian.nscalarproduct.encryption.RSA;
import com.florian.nscalarproduct.secret.SecretPart;
import com.florian.nscalarproduct.webservice.domain.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public class ServerEndpoint {
    protected static final RestTemplate REST_TEMPLATE = new RestTemplate();
    protected final String serverUrl;
    protected final Server server;
    protected final boolean testing;
    //ToDo figure out how to  automatically remove the if-statements that use this flag in production code

    public ServerEndpoint(Server server) {
        this.server = server;
        this.serverUrl = "";
        this.testing = true;
    }

    public ServerEndpoint(String url) {
        this.serverUrl = url;
        this.server = null;
        this.testing = false;
    }

    //For sharing local secret externally
    public SecretPart getSecretPart(String id, String serverId)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        GetSecretPartRequest req = new GetSecretPartRequest();
        req.setId(id);
        req.setServerId(serverId);

        RSA rsa = null;
        try {
            rsa = new RSA();
        } catch (Exception e) {
            e.printStackTrace();
        }
        req.setRsaKey(rsa.getPublicKey().getEncoded());


        if (testing) {
            SecretPartResponse response = server.getSecretPart(req);
            try {
                return new SecretPart(response.getSecretPartAES(), rsa, response.getAESkey());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            SecretPartResponse response = REST_TEMPLATE.postForObject(serverUrl + "/getSecretPart", req,
                                                                      SecretPartResponse.class);
            try {
                return new SecretPart(response.getSecretPartAES(), rsa, response.getAESkey());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //For retrieving the local secret from an external source
    public void retrieveSecret(String id, String source) {
        RetrieveSecretRequest req = new RetrieveSecretRequest();
        req.setId(id);
        req.setSource(source);
        if (testing) {
            server.retrieveSecret(req);
        } else {
            REST_TEMPLATE.put(serverUrl + "/retrieveSecret", req);
        }
    }

    public void addSecretStation(String id, List<String> serverIds, int length) {
        AddSecretRequest req = new AddSecretRequest();
        req.setId(id);
        req.setServerIds(serverIds);
        req.setLength(length);
        if (testing) {
            server.addSecretStation(req);
        } else {
            REST_TEMPLATE.put(serverUrl + "/addSecretStation", req);
        }
    }

    public void addDatastation(String id, String source) {
        AddDataStationRequest req = new AddDataStationRequest();
        req.setId(id);
        req.setSource(source);
        if (testing) {
            server.addDatastation(req);
        } else {
            REST_TEMPLATE.put(serverUrl + "/addDatastation", req);
        }
    }


    public void addDataFromSecret(String id, String source, List<String> subset) {
        AddDataFromSecretRequest req = new AddDataFromSecretRequest();
        req.setId(id);
        req.setSubset(subset);
        req.setSource(source);
        if (testing) {
            server.addDataFromSecret(req);
        } else {
            REST_TEMPLATE.put(serverUrl + "/addDataFromSecret", req);
        }
    }

    public BigInteger localCalculationFirstParty(String id, List<BigInteger[]> obfuscated) {
        LocalCalculationPartyRequest req = new LocalCalculationPartyRequest();
        req.setId(id);
        req.setObfuscated(obfuscated);
        if (testing) {
            return server.localCalculationFirstParty(req);
        }
        return REST_TEMPLATE.postForObject(serverUrl + "/localCalculationFirstParty", req, BigInteger.class);
    }

    public BigInteger removeV2(String id, BigInteger partial) {
        RemoveV2Request req = new RemoveV2Request();
        req.setId(id);
        req.setPartial(partial);
        if (testing) {
            return server.removeV2(req);
        }
        return REST_TEMPLATE.postForObject(serverUrl + "/removeV2", req, BigInteger.class);
    }

    public void collectGarbage(String id) {
        if (testing) {
            server.collectGarbage(id);
        } else {
            REST_TEMPLATE.put(serverUrl + "/collectGarbage", id);
        }
    }

    public BigInteger localCalculationNthParty(String id, List<BigInteger[]> obfuscated) {
        LocalCalculationPartyRequest req = new LocalCalculationPartyRequest();
        req.setId(id);
        req.setObfuscated(obfuscated);
        if (testing) {
            return server.localCalculationNthParty(req);
        }
        return REST_TEMPLATE.postForObject(serverUrl + "/localCalculationNthParty", req, BigInteger.class);
    }

    public BigInteger[] getObfuscated(String id) {
        NPartyRequest req = new NPartyRequest();
        req.setId(id);
        if (testing) {
            return server.getObfuscated(req).getObfuscated();
        }
        return REST_TEMPLATE.postForObject(serverUrl + "/getObfuscated", req, ObfuscatedResponse.class)
                .getObfuscated();
    }

    public String getServerId() {
        if (testing) {
            return server.getServerId();
        }
        return REST_TEMPLATE.getForEntity(serverUrl + "/getServerId", String.class).getBody();
    }

    public int getPopulation() {
        if (testing) {
            return server.getPopulation();
        }
        return REST_TEMPLATE.getForEntity(serverUrl + "/getPopulation", Integer.class).getBody();
    }

    public void initEndpoints() {
        if (!testing) {
            REST_TEMPLATE.put(serverUrl + "/initEndpoints", null);
        }

    }
}
