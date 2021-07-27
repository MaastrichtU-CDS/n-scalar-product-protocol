import random
from copy import deepcopy

from secret import SecretPart
from util.Util import matrixDiagonalMultiplication


class DataStation:
    MAX = 10

    def __init__(self, id:str, localData):
        self.__localData = localData
        self.__id = id
        self.__population = len(localData)
        self.__secret: SecretPart
        self.__obfuscated = None
        self.__v2 = None

    def copy(self):
        station = DataStation(self.__id, self.__localData)
        station.__population = self.__population
        return station

    def getPopulation(self):
        return self.__population

    def getId(self):
        return self.__id

    def getObfuscated(self):
        return self.__obfuscated

    def setLocalSecret(self, secret: SecretPart):
        self.__secret = secret
        self.__obfuscated = self.__localData + secret.getMatrix()

    def removeV2(self, partial):
        return partial + self.__v2

    def localCalculationFirstParty(self, obfuscated: []):
        fullList = deepcopy(obfuscated)
        fullList.append(self.__localData)
        self.__v2 = random.randint(1, DataStation.MAX)

        return len(obfuscated) * self.__secret.getR() + matrixDiagonalMultiplication(fullList) - self.__v2

    def localCalculationNthParty(self, obfuscated: []):
        fullList = deepcopy(obfuscated)
        fullList.append(self.__secret.getMatrix())

        return 0 - matrixDiagonalMultiplication(fullList) + len(obfuscated) * self.__secret.getR()
