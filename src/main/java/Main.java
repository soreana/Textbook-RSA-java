import RSA.RSA;
import RSA.PrivateKey;
import RSA.PublicKey;
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

        PrivateKey pr = RSA.generatePrivateKey(console.nextInt(), console.nextInt());
        PublicKey pub = RSA.exctractPublicKey(pr);

        System.out.println(pr + "\n" + pub);

        @Cleanup FileWriter pubFile = new FileWriter(args.pubPath),
                prFile = new FileWriter(args.prPath);

        prFile.write(pr.getD() + "\n" + pr.getN());
        pubFile.write(pub.getE() + "\n" + pub.getN());

        prFile.flush();
        pubFile.flush();
    }

    public static void main(String[] argv) {

        Args args = parsArgs(argv);

        if (args.debug) // todo clear this shit
            System.out.println(args);

        try {
            if (args.gen)
                generateKeys(args);
            else if (args.encrypt) {

                // todo read keys

                @Cleanup FileWriter outputFile = new FileWriter(args.outputFilePath);
                @Cleanup FileReader inputFile = new FileReader(args.inputFilePath);

                int tmp;
                do {
                    tmp = inputFile.read();
                    //tmp = RSA.encrypt(pub, tmp);
                    outputFile.write(tmp);
                } while (tmp != -1);

                outputFile.flush();

            } else if (args.decrypt) {
                // todo read keys
                // todo decrypt
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
