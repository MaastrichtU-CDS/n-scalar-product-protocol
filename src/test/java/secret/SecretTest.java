package secret;

import junit.framework.TestCase;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static util.Util.matrixDiagonalMultiplication;

public class SecretTest extends TestCase {

    @Test
    public void testGenerateSecret() {
        List<String> parties = new ArrayList<>();
        int length = 10;
        for (int i = 0; i < 10; i++) {
            parties.add(String.valueOf(i));
        }
        Secret secret = Secret.generateSecret(length, parties);

        BigInteger sum = BigInteger.ZERO;
        List<BigInteger[][]> matrices = new ArrayList<>();
        for (SecretPart part : secret.getParts()) {
            sum = sum.add(part.getR());
            matrices.add(part.getMatrix());
        }
        assertEquals(sum, matrixDiagonalMultiplication(matrices, length));

    }
}