import java.math.BigInteger;
import java.util.Random;

public class QuadraticFrobenius {

    public static void main(String[] args) {
        // Example usage
        BigInteger number = new BigInteger(""); // Example number
        int iterations = 2; // Number of iterations for the test

        boolean isPrime = isProbablePrime(number, iterations);
        System.out.println("Is " + number + " prime? " + isPrime);
    }

    // Quadratic Frobenius Primality Test
    public static boolean isProbablePrime(BigInteger n, int iterations) {
        // Step 1: Quick checks for small primes
        if (n.compareTo(BigInteger.TWO) < 0) return false; // Less than 2
        if (n.equals(BigInteger.TWO)) return true;         // 2 is prime
        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) return false; // Even number
        
        // Step 2: Trial division with small primes
        BigInteger[] smallPrimes = {BigInteger.valueOf(3), BigInteger.valueOf(5), BigInteger.valueOf(7)};
        for (BigInteger prime : smallPrimes) {
            if (n.equals(prime)) return true;
            if (n.mod(prime).equals(BigInteger.ZERO)) return false;
        }

        // Step 3: Perform Quadratic Frobenius Test
        Random rand = new Random();
        for (int i = 0; i < iterations; i++) {
            BigInteger a = getRandomBigInteger(n, rand);
            if (!testFrobeniusCondition(n, a)) {
                return false; // Composite
            }
        }
        return true; // Probably prime
    }

    // Generate a random BigInteger 'a' such that 0 < a < n
    private static BigInteger getRandomBigInteger(BigInteger n, Random rand) {
        BigInteger a;
        do {
            a = new BigInteger(n.bitLength(), rand).mod(n);
        } while (a.compareTo(BigInteger.ZERO) <= 0 || a.compareTo(n) >= 0);
        return a;
    }

    // Frobenius condition test
    private static boolean testFrobeniusCondition(BigInteger n, BigInteger a) {
        BigInteger four = BigInteger.valueOf(4);
        BigInteger frobeniusValue = a.pow(2).subtract(four).mod(n);

        // Check if frobeniusValue is a quadratic residue mod n
        return !hasQuadraticResidue(frobeniusValue, n);
    }

    // Check if 'a' is a quadratic residue mod n using the Legendre symbol
    private static boolean hasQuadraticResidue(BigInteger a, BigInteger n) {
        BigInteger exp = n.subtract(BigInteger.ONE).divide(BigInteger.TWO);
        BigInteger result = a.modPow(exp, n);
        return result.equals(BigInteger.ONE); // Quadratic residue
    }

   
}
