import unittest

import numpy

from util.Util import matrixDiagonalMultiplication


class UtilTest(unittest.TestCase):
    def test_matrixDiagonalMultiplication(self):
        max = 100

        for length in range(2, max):
            matrices = []
            for population in range(1, max):
                matrix = numpy.zeros((length, length))
                for i in range(length):
                    matrix[i][i] = 1
                matrices.append(matrix)

            res = matrixDiagonalMultiplication(matrices)

            # due to it being filled with 1's on the diagonal the diagonal sum is equal to the length of the matrices
            self.assertEquals(length, res)
