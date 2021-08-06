package station;

import secret.Secret;
import secret.SecretPart;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static secret.Secret.generateSecret;
import static util.Util.matrixMultiplication;

public class SecretStation extends Station {
    private Secret secret;

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
        List<BigInteger[][]> list = new ArrayList<>();
        for (String id : subset) {
            for (SecretPart part : secret.getParts()) {
                if (part.getId().equals(id)) {
                    list.add(part.getMatrix());
                }
            }
        }
        String id = subset.stream().map(e -> e.toString()).reduce("", String::concat);
        return new DataStation(id, matrixMultiplication(list));

    }


}
