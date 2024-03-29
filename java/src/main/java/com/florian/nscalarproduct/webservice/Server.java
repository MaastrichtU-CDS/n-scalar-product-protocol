package com.florian.nscalarproduct.webservice;

import com.florian.nscalarproduct.data.Attribute;
import com.florian.nscalarproduct.data.Data;
import com.florian.nscalarproduct.encryption.AES;
import com.florian.nscalarproduct.encryption.RSA;
import com.florian.nscalarproduct.secret.EncryptedSecretPart;
import com.florian.nscalarproduct.station.DataStation;
import com.florian.nscalarproduct.station.SecretStation;
import com.florian.nscalarproduct.webservice.domain.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class Server implements ApplicationContextAware {
    protected Map<String, DataStation> dataStations = new HashMap<>();
    protected Map<String, SecretStation> secretStations = new HashMap<>();
    @Value ("${server}")
    protected String serverId;
    protected BigInteger[] localData;
    protected int population;
    private static final int MAGICN = 3;
    private ApplicationContext context;

    private List<ServerEndpoint> endpoints = new ArrayList<>();

    @Value ("${servers}")
    private List<String> servers;

    public Server(List<String> servers) {
        this.servers = servers;
    }

    public Server() {
    }

    @PutMapping ("initEndpoints")
    public void initEndpoints() {
        // only do this is servers is set and if endpoints is currently empty.
        // if endpoints is not empty they have already been set via setEndpoints.
        // setEndpoints is relevant for vantage6, which needs to dynamically determine the URLS.
        if (servers != null && endpoints.size() == 0) {
            for (String s : servers) {
                endpoints.add(new ServerEndpoint(s));
            }
        }
    }

    @PostMapping ("setEndpoints")
    public void setEndponts(@RequestBody EndpointRequest req) {
        this.servers = req.getServers();
        reset();
        initEndpoints();

    }

    @PostMapping ("setID")
    public void setId(String id) {
        this.serverId = id;
    }

    protected void reset() {
        dataStations = new HashMap<>();
        secretStations = new HashMap<>();
        localData = null;
        endpoints = new ArrayList<>();
    }

    public void setEndpoints(List<ServerEndpoint> endpoints) {
        this.endpoints = endpoints;
    }

    @PutMapping ("initRandom")
    public void initRandom(@RequestBody Integer population, int precision) {
        //this method exists purely as an example of how to init the secretStations
        reset();
        initEndpoints();
        secretStations.put("start", new SecretStation(endpoints.stream().map(s -> s.getServerId()).collect(
                Collectors.toList()), population, precision));
        this.population = population;
    }

    @PutMapping ("initData")
    public void initData() {
        reset();
        initEndpoints();
        localData = new BigInteger[MAGICN];
        localData[0] = BigInteger.ZERO;
        localData[1] = BigInteger.ZERO;
        localData[2] = BigInteger.ONE;
        for (String s : servers) {
            endpoints.add(new ServerEndpoint(s));
        }
        dataStations.put("start", new DataStation(serverId, localData));
        this.population = MAGICN;
    }


    @PutMapping ("collectGarbage")
    public void collectGarbage(@RequestBody String id) {
        // remove the datastations associated with this calculation so memory doesn't overflow
        // can only be called after RemoveV2 has been called for this calculation
        dataStations.keySet().remove(id);
        secretStations.keySet().remove(id);
    }

    public void collectGarbageExternal(String id) {
        for (ServerEndpoint end : endpoints) {
            end.collectGarbage(id);
        }
    }

    public Server(String serverId, List<Server> servers, int length, int precision) {
        this.serverId = serverId;
        secretStations.put("start", new SecretStation(servers.stream().map(s -> s.getServerId()).collect(
                Collectors.toList()), length, precision));
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
    public SecretPartResponse getSecretPart(@RequestBody GetSecretPartRequest req)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretPartResponse response = new SecretPartResponse();

        AES aes = null;
        try {
            aes = new AES();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        RSA rsa = null;
        try {
            rsa = new RSA();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(req.getRsaKey());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        response.setAESkey(rsa.encryptSecretKey(aes.getKey(), pubKey));

        response.setSecretPartAES(new EncryptedSecretPart(secretStations.get(req.getId()).getPart(req.getServerId()),
                                                          aes));
        return response;
    }


    //For retrieving the local secret from an external source
    @PutMapping ("retrieveSecret")
    public void retrieveSecret(@RequestBody RetrieveSecretRequest req) {
        endpoints.parallelStream().forEach(x -> {
            try {
                retrieveSecret(req, x);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean retrieveSecret(RetrieveSecretRequest req, ServerEndpoint end)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (end.getServerId().equals(req.getSource())) {
            if (end.getSecretPart(req.getId(), serverId) == null) {
                return true;
            }
            dataStations.get(req.getId()).setLocalSecret(end.getSecretPart(req.getId(), serverId));
        }
        return false;
    }

    @PutMapping ("addSecretStation")
    public void addSecretStation(@RequestBody AddSecretRequest req) {
        secretStations.put(req.getId(), new SecretStation(req.getServerIds(), req.getLength(), req.getPrecision()));
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
        BigInteger res = dataStations.get(req.getId()).removeV2(req.getPartial());
        collectGarbage(req.getId());
        collectGarbageExternal(req.getId());
        return res;
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

    @PutMapping ("kill")
    public void kill() {
        ((ConfigurableApplicationContext) context).close();
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.context = ctx;
    }

    public static void checkHorizontalSplit(Data data, BigInteger[] localData) {
        // utility method to check for horizontal splits in the data
        // Only used in wrapper projects that utilize n-party-protocol
        if (data.hasHorizontalSplit()) {
            // if a horizontal split is present, check if this record is locally present.
            // if it is not locally present, treat is as if all local attributes are unknown and set localdata to 1
            // for this record
            Attribute localPresence = data.getData().get(data.getLocalPresenceColumn()).get(0);
            AttributeRequirement checkLocalPresence = new AttributeRequirement();
            checkLocalPresence.setValue(
                    new Attribute(Attribute.AttributeType.bool, "true", localPresence.getAttributeName()));
            List<Attribute> present = data.getData().get(data.getLocalPresenceColumn());
            for (int i = 0; i < localData.length; i++) {
                if (!checkLocalPresence.checkRequirement(present.get(i))) {
                    localData[i] = BigInteger.ONE;
                }
            }
        }
    }

    public List<ServerEndpoint> getEndpoints() {
        return this.endpoints;
    }
}
