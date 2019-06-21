import RSA.RSA;
import RSA.PrivateKey;
import RSA.PublicKey;
import lombok.Cleanup;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        System.out.println("Please input two prime numbers:");
        try {
            PrivateKey pr = RSA.generatePrivateKey(console.nextInt(),console.nextInt());
            PublicKey pub = RSA.exctractPublicKey(pr);

            System.out.println(pr);
            System.out.println(pub);

            @Cleanup FileWriter prFile = new FileWriter("PRIVATE.key");
            prFile.write(pr.getD() + "\n");
            prFile.write(pr.getN() + "");

            @Cleanup FileWriter pubFile = new FileWriter("PUBLIC.key");
            pubFile.write(pub.getE() + "\n");
            pubFile.write(pub.getN() + "");

        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
