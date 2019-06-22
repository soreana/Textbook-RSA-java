package RSA;

import lombok.Getter;

public class PrivateKeyImp implements PrivateKey {

    @Getter
    private final int d, n, e;
    private final int BLOCK_SIZE;

    public PrivateKeyImp(int p, int q) {
        this.d = RSA.calculateD(p, q);
        this.n = p * q;
        this.e = RSA.calculateE(p, q);
        this.BLOCK_SIZE = RSA.blockSize(this.n);
    }

    public PrivateKeyImp(int d, int n, int e){
        this.d = d;
        this.n = n;
        this.e = e;
        this.BLOCK_SIZE = RSA.blockSize(this.n);
    }

    @Override
    public String toString() {
        return String.format("Private key parameters are:\nd: %d,n: %d, e: %d\nBlock size is %d", d, n, e, BLOCK_SIZE);
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
