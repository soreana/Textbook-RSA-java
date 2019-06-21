package RSA;

import java.util.Random;

public interface RSA {

    static PrivateKey generatePrivateKey(){
        return generatePrivateKey(/* todo generate random prime number*/);
    }

    static PrivateKey generatePrivateKey(int p, int q){
        if( ! isPrime(q) || ! isPrime(p))
            throw new RuntimeException("You should input prime number.");
        else if ( p == q)
            throw new RuntimeException("You should input two diffrent prime number.");
        else if ( p*q < 256 )
            throw new RuntimeException("Multiply of inputed numbers should be more than 256");

        return new PrivateKeyImp(p,q);
    }

    static PublicKey exctractPublicKey(PrivateKey pr){
        return new PublicKeyImp(pr.getE(), pr.getN(), pr.getBlockSize());
    }

    static int calculateE(int p , int q, int phiN ){
        Random r = new Random();

        int e, max = Math.max(p,q);

        do
            e = r.nextInt(phiN - max) + (max +1);
        while (! isPrime(e));

        return e;
    }

    static boolean isPrime(int n) {
        if (n%2==0) return false;

        for(int i=3;i*i<=n;i+=2)
            if(n%i==0) return false;

        return true;
    }
}