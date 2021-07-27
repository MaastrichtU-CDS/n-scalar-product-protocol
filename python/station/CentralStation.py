from copy import deepcopy
from itertools import combinations

from station import DataStation
from station.SecretStation import SecretStation


class CentralStation:

    def calculateNPartyScalarProduct(self, datastations: [DataStation]):
        # determine first datastation:
        first = datastations[0]

        # create list of other datastations
        others = [x for x in datastations if x != first ]

        # determine the active secretStation
        # these should probably be pre-made instead of just making a new one here
        active = SecretStation()

        # share secret
        # this bit would be a webservice call
        active.shareSecret(datastations)

        # calculate partial result first datastation
        # this bit would be a webservice call
        partial = first.localCalculationFirstParty([x.getObfuscated() for x in others])

        # calculate partial result nth station

        #This can be parallelerized, it is in java
        partial = partial + sum([self.calculateNthParty(nth, datastations) for nth in others])

        # determine subprotocols
        subprotocols = self.determineSubprotocols(datastations, active)
        # run subprotocols and add results

        #This can be parallelerized, it is in java
        partial = partial + sum([self.calculateSubprotocols(sub, len(datastations)) for sub in subprotocols])

        # remove V2 and return result
        return first.removeV2(partial)


    def calculateNthParty(self, nth : DataStation, datastations: [DataStation]):
        setMinusN = [x for x in datastations if x != nth ]

        # this bit would be a  webservice call
        return nth.localCalculationNthParty([x.getObfuscated() for x in setMinusN])

    def determineSubprotocols(self, datastations: [DataStation], secretStation: SecretStation):
        # determine Ra combinations:
        n = len(datastations)
        subProtocols= []

        for k in range(2, n):
            combos = combinations(datastations, k)

            for combo in combos:
                subprotocol = []
                ids = []
                for station in datastations:
                    if not station in combo:
                        subprotocol.append(station.copy())
                    else:
                        ids.append(station.getId())

                subprotocol.append(secretStation.generateDataStation(ids))
                subProtocols.append(subprotocol)
        
        return subProtocols

    def calculateSubprotocols(self, subprotocol: [DataStation], parentSize: int):
        # each    subprotocol    needs    to    be    multiplied    by    the    difference in size    with the current protocol
        # e.g.a 4-party protocol with A, B C & D will have 2 subprotocols of ABRcRd and 1 with ARbRcRd
        # so factor this in
        nFactor = parentSize - len(subprotocol)
        # this bit would be a webservice call
        return self.calculateNPartyScalarProduct(subprotocol) * (nFactor)

