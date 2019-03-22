
import lombok.Getter;

import java.math.BigInteger;
import java.util.Random;

public class RSA {
    private static Random r = new Random();
    private final int phiN;

    @Getter
    private int p,q,n,e,d;

    public RSA(int p, int q) {
        if( ! isPrime(q) || ! isPrime(p))
            throw new RuntimeException("You should input prime number.");
        else if ( p == q)
            throw new RuntimeException("You should input two diffrent prime number.");
        else if ( p*q < 256 )
            throw new RuntimeException("Multiply of inputed numbers should be more than 256");

        this.p = p;
        this.q = q;
        this.n = p*q;
        this.phiN = (p-1)*(q-1);
        this.e = calculateE(p,q,phiN);
        // todo calculate this
        this.d = BigInteger.valueOf(e).modInverse(BigInteger.valueOf(phiN)).intValue();
    }

    public static int calculateE(int p , int q, int phiN ){
        int e, max = Math.max(p,q);

        do
            e = r.nextInt(phiN - max) + (max +1);
        while (! isPrime(e));

        return e;
    }

    public static boolean isPrime(int n) {
        if (n%2==0) return false;

        for(int i=3;i*i<=n;i+=2)
            if(n%i==0) return false;

        return true;
    }

    @Override
    public String toString() {
        return String.format("p: %d, q: %d, n: %d, phiN: %d, e: %d, d: %d", p, q, n, phiN, e, d);
    }
}