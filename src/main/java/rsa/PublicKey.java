package rsa;


public interface PublicKey {

    int getE();
    int getN();
    int getBlockSize();
}
