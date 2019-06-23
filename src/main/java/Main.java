import rsa.RSA;
import rsa.PrivateKey;
import rsa.PublicKey;
import com.beust.jcommander.JCommander;
import lombok.Cleanup;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static Args parsArgs(String[] argv) {
        Args args = new Args();

        JCommander.newBuilder()
                .addObject(args)
                .build()
                .parse(argv);

        return args;
    }

    private static void generateKeys(Args args) throws IOException {
        Scanner console = new Scanner(System.in);
        System.out.println("Please input two prime numbers:");

        int p = console.nextInt();
        int q = console.nextInt();

        PrivateKey pr = RSA.generatePrivateKey(p, q);
        PublicKey pub = RSA.extractPublicKey(pr);

        System.out.println("p: " + p + ", q: " + q + ", phiN: " + RSA.calculatePhiN(p, q) + "\n" + pr + "\n" + pub);

        @Cleanup FileWriter pubFile = new FileWriter(args.pubPath),
                prFile = new FileWriter(args.prPath);

        prFile.write(pr.getD() + "\n" + pr.getN());
        pubFile.write(pub.getE() + "\n" + pub.getN());

        prFile.flush();
        pubFile.flush();
    }

    private static void encryptFile(Args args) throws IOException {
        PublicKey pub = RSA.readPublicKeyFile(args.pubPath);

        @Cleanup FileReader inputFile = new FileReader(args.inputFilePath);
        @Cleanup FileWriter outputFile = new FileWriter(args.outputFilePath);

        int[] buff = new int[pub.getBlockSize()];
        int input;

        do {
            input = inputFile.read();

            if (input != -1) {
                RSA.encrypt(pub, buff, input);

                if (args.debug) // todo clear this shit
                    System.out.println(input);

                if (args.debug) // todo clear this shit
                    System.out.print("buffer: ");

                for (int i = 0; i < pub.getBlockSize(); i++) {
                    if (args.debug) // todo clear this shit
                        System.out.print(buff[i] + " ");
                    outputFile.write(buff[i]);
                }
            }
        } while (input != -1);

        outputFile.flush();
    }

    private static void decryptFile(Args args) throws IOException {
        PrivateKey pr = RSA.readPrivateKeyFile(args.prPath);

        @Cleanup FileReader inputFile = new FileReader(args.inputFilePath);
        @Cleanup FileWriter outputFile = new FileWriter(args.outputFilePath);

        int[] buff = new int[pr.getBlockSize()];
        int origin;
        int blockSize = pr.getBlockSize();

        do {

            if (args.debug) // todo clear this shit
                System.out.print("buffer: ");

            for (int i = 0; i < blockSize; i++) {
                buff[i] = inputFile.read();
                if (args.debug) // todo clear this shit
                    System.out.print(buff[i] + " ");
            }

            if (args.debug) // todo clear this shit
                System.out.println();

            if (buff[blockSize - 1] != -1) {
                origin = RSA.decrypt(pr, buff);
                if (args.debug) // todo clear this shit
                    System.out.println(origin);
                outputFile.write(origin);
            }

        } while (buff[blockSize - 1] != -1);

        outputFile.flush();
    }

    public static void main(String[] argv) {

        Args args = parsArgs(argv);

        if (args.debug) // todo clear this shit
            System.out.println(args);

        try {

            if (args.gen)
                generateKeys(args);
            else if (args.encrypt)
                encryptFile(args);
            else if (args.decrypt)
                decryptFile(args);

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
