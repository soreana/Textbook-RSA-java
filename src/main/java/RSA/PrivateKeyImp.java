package RSA;

import lombok.Getter;

import java.math.BigInteger;

public class PrivateKeyImp implements PrivateKey {

    private final int phiN;

    @Getter
    private final int p, q, n, e, d;
    private final int BLOCK_SIZE;

    public PrivateKeyImp(int p, int q) {
        this.p = p;
        this.q = q;
        this.n = p * q;
        this.phiN = (p - 1) * (q - 1);
        this.BLOCK_SIZE = this.n / 256;
        this.e = RSA.calculateE(p, q, this.phiN);
        // todo calculate this
        this.d = BigInteger.valueOf(e).modInverse(BigInteger.valueOf(this.phiN)).intValue();
    }

    @Override
    public String toString() {
        return String.format("Private key parameters are:\np: %d, q: %d, n: %d, phiN: %d, e: %d, d: %d\nBlock size is %d", p, q, n, phiN, e, d, BLOCK_SIZE);
    }

    @Override
    public int getBlockSize() {
        return BLOCK_SIZE;
    }
}
