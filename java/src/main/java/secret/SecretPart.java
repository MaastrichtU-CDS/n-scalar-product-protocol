package secret;

import java.math.BigInteger;

public class SecretPart {
    private BigInteger[][] matrix;
    private BigInteger r;
    private String id;

    public SecretPart(BigInteger[][] matrix, BigInteger r, String id) {
        this.matrix = matrix;
        this.r = r;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public BigInteger[][] getMatrix() {
        return matrix;
    }

    public BigInteger getR() {
        return r;
    }
}
