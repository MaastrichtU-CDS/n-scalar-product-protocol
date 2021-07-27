
import unittest

import numpy

from station import DataStation
from secret import generateSecret
from util.Util import matrixDiagonalMultiplication

POPULATION = 4


class DataStationTest(unittest.TestCase):
    def test_2PartyCalculation(self):
        A_data = createData(POPULATION)
        stationA = DataStation.DataStation("1", A_data)
        B_data = createData(POPULATION)
        stationB = DataStation.DataStation("2", B_data)

        secret = generateSecret([stationA, stationB])

        stationA.setLocalSecret(secret.getParts()[0])
        stationB.setLocalSecret(secret.getParts()[1])

        ra = secret.getParts()[0].getR()
        rb = secret.getParts()[1].getR()

        B_hat = stationB.getObfuscated()
        A_hat = stationA.getObfuscated()

        Ra = secret.getParts()[0].getMatrix()

        # manually run the calculation: A_hat * B + rb - B_hat * Ra + ra to determine expected result
        expectedresult = matrixDiagonalMultiplication([A_hat, B_data]) + (rb) - (
            matrixDiagonalMultiplication([B_hat, Ra])) + (ra)

        # run 2 - party calculation
        list = []
        list.append(stationA.getObfuscated())
        partial = stationB.localCalculationFirstParty(list)
        list2 = []
        list2.append(stationB.getObfuscated())
        partial = partial + stationA.localCalculationNthParty(list2)
        result = stationB.removeV2(partial)

        self.assertEquals(expectedresult, result)

    def test_GetObfuscated(self):
        data = createData(POPULATION)

        station = DataStation.DataStation("1", data)
        secret = generateSecret([station]).getParts()[0]

        station.setLocalSecret(secret)

        obfuscated = station.getObfuscated()
        for i in range(0, POPULATION):
            self.assertEquals(data[i][i] + secret.getMatrix()[i][i], obfuscated[i][i])


def createStation(id: str, population: int):
    return DataStation.DataStation(str(id), createData(population))


def createData(population: int):
    data = numpy.zeros((population, population))
    for i in range(0, population):
        data[i][i] = 1 #random.randint(0, 1)
    return data
