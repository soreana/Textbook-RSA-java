package rsa;

import lombok.Cleanup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

public interface RSA {
    Logger log = LogManager.getLogger(RSA.class);

    /**
     * @param maxBits generated private key with respect to this number
     * @return generated private key
     */

    static PrivateKey generatePrivateKey(int maxBits) {

        Random r = new SecureRandom();

        int p = randomPrime(maxBits, r);
        int q = randomPrime(maxBits, r);

        while (q == p && p * q < 256)
            q = randomPrime(maxBits, r);

        return generatePrivateKey(p, q);
    }

    /**
     * Method to generate private key based on two prime number p and q.
     *
     * @param p prime number
     * @param q prime number
     * @return generated private key
     */

    static PrivateKey generatePrivateKey(int p, int q) {
        if (!isPrime(q) || !isPrime(p))
            throw new RuntimeException("You should input prime number.");
        else if (p == q)
            throw new RuntimeException("You should input two diffrent prime number.");
        else if (p * q < 256)
            throw new RuntimeException("Multiply of inputed numbers should be more than 256");

        return new PrivateKeyImp(p, q);
    }

    /**
     * @param pr private key
     * @return extracted public key
     * @throws RuntimeException if provided private key hasn't public key info.
     */

    static PublicKey extractPublicKey(PrivateKey pr) {
        if (!pr.hasPublicKeyParameters())
            throw new RuntimeException("Private key doesn't contain public key");
        return new PublicKeyImp(pr.getE(), pr.getN(), pr.getBlockSize());
    }

    /**
     * @param prPath private key file path
     * @return read private key
     * @throws IOException           if can't read private key file
     * @throws FileNotFoundException if private key file doesn't exist in prPath
     */

    static PrivateKey readPrivateKeyFile(String prPath) throws IOException {
        @Cleanup BufferedReader br = new BufferedReader(new FileReader(prPath));

        int d = Integer.parseInt(br.readLine());
        int n = Integer.parseInt(br.readLine());
        int e = Integer.parseInt(Optional.ofNullable(br.readLine()).orElse("-1"));

        return new PrivateKeyImp(d, n, e);
    }

    /**
     * @param pubPath public key file path
     * @return read public key
     * @throws IOException           if can't read public key file
     * @throws FileNotFoundException if public key file doesn't exist in prPath
     */

    static PublicKey readPublicKeyFile(String pubPath) throws IOException {
        @Cleanup BufferedReader br = new BufferedReader(new FileReader(pubPath));

        int e = Integer.parseInt(br.readLine());
        int n = Integer.parseInt(br.readLine());

        return new PublicKeyImp(e, n);
    }

    /**
     * @param p prime number p
     * @param q prime number q
     * @return calculated E
     */

    static int calculateE(int p, int q) {
        Random r = new SecureRandom();
        int phiN = calculatePhiN(p, q);

        int e, max = Math.max(p, q);

        do
            e = r.nextInt(phiN - max) + (max + 1);
        while (!isPrime(e));

        return e;
    }

    /**
     * φ(n)
     *
     * @param p prime number p
     * @param q prime number q
     * @return φ(n)
     */

    static int calculatePhiN(int p, int q) {
        return (p - 1) * (q - 1);
    }

    /**
     *
     * @param n check to see if n is prime or not
     * @return true if n is prime false otherwise.
     */

    static boolean isPrime(int n) {
        if (n % 2 == 0) return false;

        for (int i = 3; i * i <= n; i += 2)
            if (n % i == 0) return false;

        return true;
    }

    /**
     *
     * @param maxBits generated prime numbers bit count will be less than maxBits
     * @param r use's r to generate random prime
     * @return generated prime number
     */

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

    /**
     * calculate block size
     */

    static int blockSize(int n) {
        return n / 256 + 1;
    }

    /**
     *
     * @param phiN Euler's totient function φ(n)
     * @param e  encryption key
     * @return calculated d
     */

    static int calculateD(int phiN, int e) {
        // todo re-implement this method and calculate e modeInverse rather than using BigInteger.modInverse method

        return BigInteger.valueOf(e).modInverse(BigInteger.valueOf(phiN)).intValue();
    }

    /**
     *
     * @param pub public key used to encrypt message
     * @param buff encrypted message save to this buffer
     * @param message encrypt message
     */

    static void encrypt(PublicKey pub, int[] buff, int message) {

        BigInteger base = BigInteger.valueOf(message);

        BigInteger encrypt = base.pow(pub.getE())
                .mod(BigInteger.valueOf(pub.getN()));

        for (int i = 0; i < pub.getBlockSize(); i++) {
            buff[i] = encrypt.mod(BigInteger.valueOf(256)).intValue();
            encrypt = encrypt.divide(BigInteger.valueOf(256));
        }

    }

    /**
     *
     * @param pr private key used to decrypt buff
     * @param buff buff contain encrypted message
     * @return decrypted message
     */

    static int decrypt(PrivateKey pr, int[] buff) {

        int maxIndex = pr.getBlockSize() - 1;

        BigInteger base = BigInteger.valueOf(buff[maxIndex]);

        for (int i = maxIndex - 1; i >= 0; i--)
            base = base.multiply(BigInteger.valueOf(256)).add(BigInteger.valueOf(buff[i]));

        return base.pow(pr.getD())
                .mod(BigInteger.valueOf(pr.getN()))
                .intValue();
    }

    /**
     *
     * @param pub public key used to encrypt inputFilePath
     * @param inputFilePath encrypt content of this file and write them to outputFilePath
     * @param outputFilePath encrypted content of inputFilePath will write to this file
     * @throws IOException if can't read public key file or input file or can't write to output file.
     * @throws FileNotFoundException if either of public key file or input file doesn't exist
     */

    static void encryptFile(PublicKey pub, String inputFilePath, String outputFilePath) throws IOException {
        @Cleanup FileReader inputFile = new FileReader(inputFilePath);
        @Cleanup FileWriter outputFile = new FileWriter(outputFilePath);

        int[] buff = new int[pub.getBlockSize()];
        int input;

        do {
            input = inputFile.read();

            if (input != -1) {
                RSA.encrypt(pub, buff, input);

                log.debug("Inputed character is :" + input);

                log.debug(() -> "buffer: " + Arrays.toString(buff));

                for (int i = 0; i < pub.getBlockSize(); i++)
                    outputFile.write(buff[i]);
            }
        } while (input != -1);

        outputFile.flush();
    }

    /**
     *
     * @param pr private key used to decrypt inputFilePath
     * @param inputFilePath decrypt content of this file and write them to outputFilePath
     * @param outputFilePath decrypt content of inputFilePath will write to this file
     * @throws IOException if can't read private key file or input file or can't write to output file.
     * @throws FileNotFoundException if either of private key file or input file doesn't exist
     */

    static void decryptFile(PrivateKey pr, String inputFilePath, String outputFilePath) throws IOException {
        @Cleanup FileReader inputFile = new FileReader(inputFilePath);
        @Cleanup FileWriter outputFile = new FileWriter(outputFilePath);

        int[] buff = new int[pr.getBlockSize()];
        int origin;
        int blockSize = pr.getBlockSize();

        do {

            log.debug(() -> "buffer: " + Arrays.toString(buff));

            for (int i = 0; i < blockSize; i++)
                buff[i] = inputFile.read();

            if (buff[blockSize - 1] != -1) {
                origin = RSA.decrypt(pr, buff);
                log.debug("Original message was: " + origin);
                outputFile.write(origin);
            }

        } while (buff[blockSize - 1] != -1);

        outputFile.flush();
    }
}