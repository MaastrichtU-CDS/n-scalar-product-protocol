package station;

import org.apache.commons.math3.util.Combinations;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CentralStation {
    private List<DataStation> datastations;

    public CentralStation(List<DataStation> datastations) {
        this.datastations = datastations;
    }

    public BigInteger calculateNPartyScalarProduct(List<DataStation> datastations) {
        // determine first datastation:
        DataStation first = datastations.get(0);

        // create list of other datastations
        List<DataStation> others = datastations.stream().filter(x -> x != first).collect(Collectors.toList());

        // determine the active secretStation
        SecretStation active = new SecretStation();

        // share secret
        active.shareSecret(datastations);

        // calculate partial result first datastation
        BigInteger partial = first.localCalculationFirstParty(others.stream().map(x -> x.getObfuscated()).collect(
                Collectors.toList()));

        // calculate partial result nth station

        for (DataStation nth : others) {
            List<DataStation> setMinusN = datastations.stream().filter(x -> x != nth).collect(Collectors.toList());
            partial = nth.localCalculationNthParty(setMinusN.stream().map(x -> x.getObfuscated()).collect(
                    Collectors.toList()), partial);
        }


        // determine subprotocols
        List<List<DataStation>> subprotocols = determineSubprotocols(datastations, active);
        BigInteger temp = partial;
        // run subprotocols and add results
        for (List<DataStation> subprotocol : subprotocols) {
            // each subprotocol needs to be multiplied by the difference in size with the current protocol
            // e.g.a 4-party protocol with A, B C & D will have 2 subprotocols of ABRcRd and 1 with ARbRcRd
            // so factor this in
            BigInteger nFactor = BigInteger.valueOf(datastations.size() - subprotocol.size());
            partial = partial.add(calculateNPartyScalarProduct(subprotocol).multiply(nFactor));
        }
        // remove V2 and return result
        return first.removeV2(partial);
    }

    public List<List<DataStation>> determineSubprotocols(List<DataStation> datastations, SecretStation
            secretStation) {
        // determine Ra combinations:
        int n = datastations.size();
        List<List<DataStation>> subProtocols = new ArrayList<>();
        for (int k = 2; k <= n - 1; k++) {
            Combinations combinations = new Combinations(n, k);
            Iterator iterator = combinations.iterator();
            while (iterator.hasNext()) {
                List<Integer> combo = Arrays.stream((int[]) iterator.next()).boxed()
                        .collect(Collectors.toList());
                List<DataStation> subprotocol = new ArrayList<>();
                // collect datastations not in this collection:
                for (int i = 0; i < datastations.size(); i++) {
                    if (!combo.contains(i)) {
                        subprotocol.add(new DataStation(datastations.get(i)));
                    }
                }
                List<String> ids = new ArrayList<>();
                for (int i : combo) {
                    ids.add(datastations.get(i).getId());
                }
                subprotocol.add(secretStation.generateDataStation(ids));
                subProtocols.add(subprotocol);
            }
        }
        return subProtocols;
    }


}
