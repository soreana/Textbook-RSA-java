package rsa;

import lombok.Getter;

/**
 * This class implement PublicKey interface in simplest possible way.
 *
 * @author Sina Kashipazha
 */

public class PublicKeyImp implements PublicKey {

    /**
     * e used to calculate encrypted message C = M^e mod n
     * n used to calculate encrypted message C = M^e mod n
     * BLOCK_SIZE , Cyphered message store in BLOCK_SIZE * Byte storage
     */

    @Getter
    private final int e, n;
    private final int BLOCK_SIZE;

    /**
     * Create new public key Object with respect to e and n. Nothing checks.
     *
     * @param e          e used to calculate encrypted message C = M^e mod n
     * @param n          n used to calculate encrypted message C = M^e mod n
     * @param BLOCK_SIZE , Cyphered message store in BLOCK_SIZE * Byte storage
     */

    PublicKeyImp(int e, int n, int BLOCK_SIZE) {
        this.e = e;
        this.n = n;
        this.BLOCK_SIZE = BLOCK_SIZE;
    }

    /**
     * Create new public key Object with respect to e and n. Nothing checks.
     *
     * @param e e used to calculate encrypted message C = M^e mod n
     * @param n n used to calculate encrypted message C = M^e mod n
     */

    PublicKeyImp(int e, int n) {
        this(e, n, RSA.blockSize(n));
    }

    @Override
    public String toString() {
        return String.format("Public key parameters are:\ne: %d, n: %d\nBlock size is %d", e, n, BLOCK_SIZE);
    }

    /**
     * @return BLOCK_SIZE , Cyphered message store in BLOCK_SIZE * Byte storage
     */

    @Override
    public int getBlockSize() {
        return BLOCK_SIZE;
    }
}
