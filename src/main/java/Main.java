import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        System.out.println("Please input two prime numbers:");
        try {
            RSA rsa = new RSA(console.nextInt(),console.nextInt());
            System.out.println(rsa);
            // todo PRIVATE.key first line: d, second line: n
            // todo PUBLIC.key first line: e, second line: n
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }

    }
}
