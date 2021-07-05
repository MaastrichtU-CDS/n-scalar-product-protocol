package secret;

import org.junit.jupiter.api.Test;
import station.DataStation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.Util.matrixDiagonalMultiplication;

public class SecretTest {

    @Test
    public void testGenerateSecret() {
        List<DataStation> parties = new ArrayList<>();
        int length = 10;
        for (int i = 0; i < 10; i++) {
            parties.add(new DataStation(String.valueOf(i), new BigInteger[10][10]));
        }
        Secret secret = Secret.generateSecret(parties);

        BigInteger sum = BigInteger.ZERO;
        List<BigInteger[][]> matrices = new ArrayList<>();
        for (SecretPart part : secret.getParts()) {
            sum = sum.add(part.getR());
            matrices.add(part.getMatrix());
        }
        assertEquals(sum, matrixDiagonalMultiplication(matrices, length));

    }
}