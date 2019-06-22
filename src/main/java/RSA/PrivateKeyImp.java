package RSA;

import lombok.Getter;

public class PrivateKeyImp implements PrivateKey {

    @Getter
    private final int n, e, d;
    private final int BLOCK_SIZE;

    public PrivateKeyImp(int p, int q) {
        this.n = p * q;
        this.e = RSA.calculateE(p, q);
        this.d = RSA.calculateD(p, q);
        this.BLOCK_SIZE = RSA.blockSize(this.n);
    }

    public PrivateKeyImp(int n, int e, int d){
        this.n = n;
        this.e = e;
        this.d = d;
        this.BLOCK_SIZE = RSA.blockSize(this.n);
    }

    @Override
    public String toString() {
        return String.format("Private key parameters are:\nn: %d, e: %d, d: %d\nBlock size is %d", n, e, d, BLOCK_SIZE);
    }

    @Override
    public int getBlockSize() {
        return BLOCK_SIZE;
    }

    @Override
    public boolean hasPublicKeyParameters() {
        return e != -1;
    }
}
