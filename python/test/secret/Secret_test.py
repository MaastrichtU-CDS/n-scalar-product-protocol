import unittest

import numpy

from station.DataStation import DataStation
from secret import generateSecret
from util.Util import matrixDiagonalMultiplication


class SecretTest(unittest.TestCase):
    def test_GenerateSecret(self):
        parties = []
        length = 10;

        dummy = numpy.zeros((length, length))
        for i in range(0,10):
            parties.append(DataStation(i, dummy));


        secret = generateSecret(parties);

        sum = 0
        matrices = []

        for part in secret.getParts():
            self.assertNotEquals(part.getR(), 0)
            sum = sum + part.getR();
            matrices.append(part.getMatrix());


        #Due to machine precision this sometimes fails.
        #This is despites Pythons claim it can work with giant numbers.
        #Same code works fine with BigInteger in java
        self.assertEquals(sum, matrixDiagonalMultiplication(matrices));
