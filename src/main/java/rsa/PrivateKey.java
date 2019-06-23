package rsa;

public interface PrivateKey {

    int getN();
    int getE();
    int getD();
    int getBlockSize();

    boolean hasPublicKeyParameters();
}
