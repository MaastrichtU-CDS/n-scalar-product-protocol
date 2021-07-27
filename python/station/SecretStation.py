from secret import Secret
from secret import generateSecret
from station import DataStation
from util.Util import matrixMultiplication


class SecretStation:
    def __init__(self):
        self.__secret = None

    def shareSecret(self, parties:[DataStation]):
        self.__secret = generateSecret(parties)
        for party in parties:
            for secretPart in self.__secret.getParts():
                if secretPart.getId() == party.getId():
                    party.setLocalSecret(secretPart)

    def generateDataStation(self, subset: [str]):
        list = []
        for id  in subset:
            for part in self.__secret.getParts():
                if part.getId() == id:
                    list.append(part.getMatrix())

        return DataStation.DataStation(''.join(subset), matrixMultiplication(list))