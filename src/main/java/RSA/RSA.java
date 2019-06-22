package RSA;

import java.math.BigInteger;
import java.util.Random;

public interface RSA {

    static PrivateKey generatePrivateKey() {
        return generatePrivateKey(/* todo generate random prime number*/);
    }

    static PrivateKey generatePrivateKey(int p, int q) {
        if (!isPrime(q) || !isPrime(p))
            throw new RuntimeException("You should input prime number.");
        else if (p == q)
            throw new RuntimeException("You should input two diffrent prime number.");
        else if (p * q < 256)
            throw new RuntimeException("Multiply of inputed numbers should be more than 256");

        return new PrivateKeyImp(p, q);
    }

    static PublicKey extractPublicKey(PrivateKey pr) {
        if (! pr.hasPublicKeyParameters())
            throw new RuntimeException("Private key doesn't contain public key");
        return new PublicKeyImp(pr.getE(), pr.getN(), pr.getBlockSize());
    }

    static PrivateKey readPrivatekeyFile(String prPath){
        // todo implement this method
        return null;
    }

    static PublicKey readPublicKeyFile(String pubPath) {
        // todo implement this method
        return null;
    }

    static int calculateE(int p, int q) {
        Random r = new Random();
        int phiN = calculatePhiN(p, q);

        int e, max = Math.max(p, q);

        do
            e = r.nextInt(phiN - max) + (max + 1);
        while (!isPrime(e));

        return e;
    }

    static int calculatePhiN(int p, int q) {
        return (p - 1) * (q - 1);
    }

    static boolean isPrime(int n) {
        if (n % 2 == 0) return false;

        for (int i = 3; i * i <= n; i += 2)
            if (n % i == 0) return false;

        return true;
    }

    static int encrypt(PublicKey pub, int tmp) {
        // todo implement this method
        return tmp;
    }

    static int blockSize(int n) {
        return n / 256 + 1;
    }

    static int calculateD(int p, int q) {
        // todo refactor this method and calculate e modeInverse rather than using BigInteger.modInverse method
        BigInteger phiN = BigInteger.valueOf(calculatePhiN(p, q));
        BigInteger e = BigInteger.valueOf(calculateE(p, q));

        return e.modInverse(phiN).intValue();
    }

}