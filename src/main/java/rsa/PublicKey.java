package rsa;

/**
 * Interface used to represent Public Key. Every public key implementation should implement this interface.
 *
 * @author Sina Kashipazha
 */

public interface PublicKey {

    /**
     * @return e , e used to calculate encrypted message in C = M^e mod n formula
     **/

    int getE();

    /**
     * @return n , n used to calculate encrypted message in C = M^e mod n formula
     */

    int getN();

    /**
     * @return BLOCK_SIZE , Cyphered message store in BLOCK_SIZE * Byte storage
     */

    int getBlockSize();
}
