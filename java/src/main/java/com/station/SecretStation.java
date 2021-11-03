package com.station;


import com.secret.Secret;
import com.secret.SecretPart;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.secret.Secret.generateSecret;
import static com.util.Util.matrixMultiplication;

public class SecretStation extends Station {
    private Secret secret;

    public SecretStation() {
    }

    public SecretStation(List<String> serverIds, int length) {
        this.secret = generateSecret(serverIds, length);
    }

    public SecretPart getPart(String id) {
        for (SecretPart part : secret.getParts()) {
            if (part.getId().equals(id)) {
                return part;
            }
        }
        return null;
    }

    public void shareSecret(List<DataStation> parties) {
        this.secret = generateSecret(parties);
        for (DataStation station : parties) {
            for (SecretPart part : secret.getParts()) {
                if (part.getId().equals(station.getId())) {
                    station.setLocalSecret(part);
                }
            }
        }
    }

    public DataStation generateDataStation(List<String> subset) {
        List<BigInteger[]> list = new ArrayList<>();
        for (String id : subset) {
            for (SecretPart part : secret.getParts()) {
                if (part.getId().equals(id)) {
                    list.add(part.getDiagonal());
                }
            }
        }
        String id = subset.stream().map(e -> e.toString()).reduce("", String::concat);
        return new DataStation(id, matrixMultiplication(list));

    }

    public DataStation generateDataStation(List<String> subset, String server) {
        List<BigInteger[]> list = new ArrayList<>();
        for (String id : subset) {
            for (SecretPart part : secret.getParts()) {
                if (part.getId().equals(id)) {
                    list.add(part.getDiagonal());
                }
            }
        }
        return new DataStation(server, matrixMultiplication(list));

    }


}