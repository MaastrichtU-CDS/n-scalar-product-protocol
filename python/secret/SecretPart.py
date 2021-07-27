
class SecretPart:

    def __init__(self, matrix, r: int, id: str):
        self.__matrix = matrix
        self.__r = r
        self.__id = id

    def getMatrix(self):
        return self.__matrix

    def getR(self):
        return self.__r

    def getId(self):
        return self.__id



