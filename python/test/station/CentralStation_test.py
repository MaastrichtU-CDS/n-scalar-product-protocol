import unittest

from station import CentralStation
from station import DataStation
from station import SecretStation
from test.station import DataStation_test
from util.Util import matrixDiagonalMultiplication


class CentralStationTest(unittest.TestCase):

    def test_CalculateNPartyScalarProduct(self):
        for n in range(2,8):
            for population in range(2,10):
                datastations = []
                datasets = []
                for i in range(0,n):
                    data = DataStation_test.createData(population)
                    datastations.append(DataStation.DataStation(str(i), data))
                    datasets.append(data)

                central = CentralStation()

                # calculate expected anwser:
                expected = matrixDiagonalMultiplication(datasets)
                result = central.calculateNPartyScalarProduct(datastations)

                self.assertEquals(expected, result)
                
    def test_Calculate3PartyScalarProduct(self):
        POPUlATION = 5
        N = 3

        datastations = []
        datasets = []
        for i in range(0,N):
            data = DataStation_test.createData(POPUlATION)
            datastations.append(DataStation.DataStation(str(i), data))
            datasets.append(data)
        
        central = CentralStation()

        # calculate expected anwser:
        expected = matrixDiagonalMultiplication(datasets)
        result = central.calculateNPartyScalarProduct(datastations)

        self.assertEquals(expected, result)

    def test_DetermineSubprotocols(self):
        for population in range(1, 10):
            datastations = []
            for i in range(0, population):
                datastations.append(DataStation_test.createStation(i, population))

            central = CentralStation()
            secretStation = SecretStation()
            secretStation.shareSecret(datastations)

            subprotocols = central.determineSubprotocols(datastations, secretStation)
            self.assertEquals(len(subprotocols), calculateExpectedCombinations(population))

def calculateExpectedCombinations(n:int ):
    nFactorial = factorial(n)
    sum = 0
    for x in range(2,n):
        xFactorial = factorial(x)
        nxFactorial = factorial(n - x)
        sum = sum + (nFactorial / (xFactorial *(nxFactorial)))

    return sum


def factorial(n: int):
    nFactorial = 1
    for i in range(1,n+1):
        nFactorial = nFactorial * i
    return nFactorial
