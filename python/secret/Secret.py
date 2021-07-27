import random

import numpy

from secret.SecretPart import SecretPart
from util.Util import matrixDiagonalMultiplication


class Secret:
    MAX = 100

    def __init__(self, secretParts: [SecretPart]):
        self.__secretParts = secretParts

    def getParts(self):
        return self.__secretParts



def generateSecret(parties: []):
    matrices = []
    length = parties[0].getPopulation()
    size = len(parties)



    for party in parties:
        matrix = numpy.zeros((length, length))
        for i in range(length):
            matrix[i][i] =  random.randint(1,Secret.MAX)
        matrices.append(matrix)

    sum = matrixDiagonalMultiplication(matrices)
    rs = []
    remainder = sum
    max = numpy.floor(sum / size)
    for i in range(size-1):
        if max < 1:
            print(max)
        value = random.randint(1, max)
        rs.append(value)
        remainder = remainder - value
    rs.append(remainder)
    parts: [SecretPart] = []
    for i in range(0, len(rs)):
        parts.append(SecretPart(matrices[i], rs[i], parties[i].getId()))

    return Secret(parts)
