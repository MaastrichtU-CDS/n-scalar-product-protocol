package secret;

import java.math.BigInteger;

public class SecretPart {
    private BigInteger[] diagonal;
    private BigInteger r;
    private String id;

    public SecretPart(BigInteger[] diagonal, BigInteger r, String id) {
        this.diagonal = diagonal;
        this.r = r;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public BigInteger[] getDiagonal() {
        return diagonal;
    }

    public BigInteger getR() {
        return r;
    }
}
