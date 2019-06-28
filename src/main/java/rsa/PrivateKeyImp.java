package rsa;

import lombok.Getter;

/**
 * This class implement PrivateKey interface in simplest possible way.
 *
 * @author Sina Kashipazha
 */

public class PrivateKeyImp implements PrivateKey {

    /**
     * d used to calculate decrypted message in M = C^d mod n formula
     * n used to calculate encrypted message C = M^e mod n
     * e used to calculate encrypted message C = M^e mod n
     * BLOCK_SIZE , Cyphered message store in BLOCK_SIZE * Byte storage
     */

    @Getter
    private final int d, n, e;
    private final int BLOCK_SIZE;

    /**
     * Calculate new private key parameters using prime numbers p and q.
     */

    PrivateKeyImp(int p, int q) {
        this.e = RSA.calculateE(p, q);
        this.d = RSA.calculateD(RSA.calculatePhiN(p,q), this.e);
        this.n = p * q;
        this.BLOCK_SIZE = RSA.blockSize(this.n);
    }

    /**
     * Store previously calculated private key parameters to new Object.
     */

    PrivateKeyImp(int d, int n, int e){
        this.d = d;
        this.n = n;
        this.e = e;
        this.BLOCK_SIZE = RSA.blockSize(this.n);
    }

    @Override
    public String toString() {
        return String.format("Private key parameters are:\nd: %d, n: %d, e: %d\nBlock size is %d", d, n, e, BLOCK_SIZE);
    }

    /**
     * @return BLOCK_SIZE , Cyphered message store in BLOCK_SIZE * Byte storage
     */

    @Override
    public int getBlockSize() {
        return BLOCK_SIZE;
    }

    /**
     * @return true if private key contain public key parameters
     */

    @Override
    public boolean hasPublicKeyParameters() {
        return e != -1;
    }
}
