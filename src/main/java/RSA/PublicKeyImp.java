package RSA;

import lombok.Getter;

public class PublicKeyImp implements PublicKey {
    @Getter
    private final int e, n;
    private final int BLOCK_SIZE;

    public PublicKeyImp(int e, int n, int BLOCK_SIZE) {
        this.e = e;
        this.n = n;
        this.BLOCK_SIZE = BLOCK_SIZE;
    }

    public PublicKeyImp(int e, int n){
        this(e,n,RSA.blockSize(n));
    }

    @Override
    public String toString() {
        return String.format("Public key parameters are:\ne: %d, n: %d\nBlock size is %d", e, n, BLOCK_SIZE);
    }
}
