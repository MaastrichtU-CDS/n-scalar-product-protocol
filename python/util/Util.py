import numpy


def matrixDiagonalMultiplication(matrices: []):
    return matrixMultiplication(matrices).diagonal().sum()

def matrixMultiplication(matrices: []):
    temp = matrices[0]
    for i in range(1, len(matrices)):
        temp = numpy.dot(temp, matrices[i])
    return temp
