import org.apache.logging.log4j.Logger;
import rsa.RSA;
import rsa.PrivateKey;
import rsa.PublicKey;
import com.beust.jcommander.JCommander;
import lombok.Cleanup;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;

public class Main {
    private static Logger log = LogManager.getLogger(Main.class);

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

    public static void main(String[] argv) {

        Args args = parsArgs(argv);

        log.debug(()-> args);

        try {

            if (args.gen)
                generateKeys(args);
            else if (args.encrypt) {
                PublicKey pub = RSA.readPublicKeyFile(args.pubPath);
                RSA.encryptFile(pub, args.inputFilePath, args.outputFilePath);
            } else if (args.decrypt) {
                PrivateKey pr = RSA.readPrivateKeyFile(args.prPath);
                RSA.decryptFile(pr, args.inputFilePath, args.outputFilePath);
            } else
                log.warn("Nothing Done. Please provide some arguments.");

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
