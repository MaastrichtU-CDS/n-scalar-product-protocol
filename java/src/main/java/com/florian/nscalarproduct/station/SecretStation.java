package com.florian.nscalarproduct.station;

import com.florian.nscalarproduct.secret.Secret;
import com.florian.nscalarproduct.secret.SecretPart;
import com.florian.nscalarproduct.util.Util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class SecretStation extends Station {
    private Secret secret;

    public SecretStation() {
    }

    public SecretStation(List<String> serverIds, int length, int precision) {
        this.secret = Secret.generateSecret(serverIds, length, precision);
    }

    public SecretPart getPart(String id) {
        for (SecretPart part : secret.getParts()) {
            if (part.getId().equals(id)) {
                return part;
            }
        }
        return null;
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
        return new DataStation(id, Util.matrixMultiplication(list));

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
        return new DataStation(server, Util.matrixMultiplication(list));

    }


}
