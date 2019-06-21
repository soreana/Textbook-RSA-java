package RSA;

public interface PrivateKey {

    int getP();
    int getQ();
    int getN();
    int getE();
    int getD();
    int getBlockSize();
}
