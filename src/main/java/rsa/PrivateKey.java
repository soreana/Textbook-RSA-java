package rsa;

public interface PrivateKey {

    int getN();
    int getE();
    int getD();
    int getBlockSize();

    /**
     *
     * @return true if private key can used to generate public key
     */

    boolean hasPublicKeyParameters();
}
