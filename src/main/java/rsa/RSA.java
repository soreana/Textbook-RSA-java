package rsa;

import lombok.Cleanup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

public interface RSA {

    static PrivateKey generatePrivateKey(int maxBits) {

        Random r = new SecureRandom();

        int p = randomPrime(maxBits, r);
        int q = randomPrime(maxBits, r);

        while (q == p && p * q < 256)
            q = randomPrime(maxBits, r);

        return generatePrivateKey(p, q);
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
        if (!pr.hasPublicKeyParameters())
            throw new RuntimeException("Private key doesn't contain public key");
        return new PublicKeyImp(pr.getE(), pr.getN(), pr.getBlockSize());
    }

    static PrivateKey readPrivateKeyFile(String prPath) throws IOException {
        @Cleanup BufferedReader br = new BufferedReader(new FileReader(prPath));

        int d = Integer.parseInt(br.readLine());
        int n = Integer.parseInt(br.readLine());
        int e = Integer.parseInt(Optional.ofNullable(br.readLine()).orElse("-1"));

        return new PrivateKeyImp(d, n, e);
    }

    static PublicKey readPublicKeyFile(String pubPath) throws IOException {
        @Cleanup BufferedReader br = new BufferedReader(new FileReader(pubPath));

        int e = Integer.parseInt(br.readLine());
        int n = Integer.parseInt(br.readLine());

        return new PublicKeyImp(e, n);
    }

    static int calculateE(int p, int q) {
        Random r = new SecureRandom();
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

    static int randomPrime(int maxBits, Random r) {
        while (true) {
            try {
                int prime = BigInteger.probablePrime(maxBits, r).intValueExact();
                if (isPrime(prime))
                    return prime;
            } catch (ArithmeticException e) {
                if (!e.getMessage().equals("BigInteger out of int range"))
                    throw e;
            }
        }
    }

    static void encrypt(PublicKey pub, int[] buff, int message) {

        BigInteger base = BigInteger.valueOf(message);

        BigInteger decrypt = base.pow(pub.getE())
                .mod(BigInteger.valueOf(pub.getN()));

        for (int i = 0; i < pub.getBlockSize(); i++) {
            buff[i] = decrypt.mod(BigInteger.valueOf(256)).intValue();
            decrypt = decrypt.divide(BigInteger.valueOf(256));
        }

    }

    static int blockSize(int n) {
        return n / 256 + 1;
    }

    static int calculateD(int phiN, int e) {
        // todo re-implement this method and calculate e modeInverse rather than using BigInteger.modInverse method

        return BigInteger.valueOf(e).modInverse(BigInteger.valueOf(phiN)).intValue();
    }

}