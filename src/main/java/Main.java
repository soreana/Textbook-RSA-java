import lombok.Cleanup;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        System.out.println("Please input two prime numbers:");
        try {
            RSA rsa = new RSA(console.nextInt(),console.nextInt());
            System.out.println(rsa);

            @Cleanup FileWriter privateKey = new FileWriter("PRIVATE.key");
            privateKey.write(rsa.getD() + "\n");
            privateKey.write(rsa.getN() + "");

            @Cleanup FileWriter publicKey = new FileWriter("PUBLIC.key");
            publicKey.write(rsa.getE() + "\n");
            publicKey.write(rsa.getN() + "");

        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
