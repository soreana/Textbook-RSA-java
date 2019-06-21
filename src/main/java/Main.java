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

    public static void main(String[] argv) {

        Args args = parsArgs(argv);

        if (args.debug) // todo clear this shit
            System.out.println(args);

        Scanner console = new Scanner(System.in);
        System.out.println("Please input two prime numbers:");
        try {
            PrivateKey pr = RSA.generatePrivateKey(console.nextInt(), console.nextInt());
            PublicKey pub = RSA.exctractPublicKey(pr);

            System.out.println(pr);
            System.out.println(pub);

            @Cleanup FileWriter pubFile = new FileWriter(args.pubPath),
                    prFile = new FileWriter(args.prPath),
                    outputFile = new FileWriter(args.outputFilePath);

            @Cleanup FileReader inputFile = new FileReader(args.inputFilePath);

            prFile.write(pr.getD() + "\n");
            prFile.write(pr.getN() + "");

            pubFile.write(pub.getE() + "\n");
            pubFile.write(pub.getN() + "");


            int tmp;
            do {
                tmp = inputFile.read();
                tmp = RSA.encrypt(pub, tmp);
                outputFile.write(tmp);
            } while (tmp != -1);

            outputFile.flush();

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
