package rsa;

/**
 * Interface used to represent Private Key. Every private key implementation should implement this interface.
 *
 * @author Sina Kashipazha
 */

public interface PrivateKey {

    /**
     * @return n , n used to calculate encrypted message in C = M^e mod n formula
     */

    int getN();

    /**
     * @return e , e used to calculate decrypted message in M = C^d mod n formula
     **/

    int getE();

    /**
     * @return d , d used to calculate decrypted message in M = C^d mod n formula
     **/

    int getD();

    /**
     * @return BLOCK_SIZE , Cyphered message store in BLOCK_SIZE * Byte storage
     */

    int getBlockSize();

    /**
     * @return true if private key contain public key parameters
     */

    boolean hasPublicKeyParameters();
}
