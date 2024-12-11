import java.math.BigInteger;
import java.util.Random;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class SolovayStrassen {
    public static void main(String[] args) {
        try {
            String numberStr = new String(Files.readAllBytes(Paths.get("large_number.txt"))).trim();
            BigInteger n = new BigInteger(numberStr);
            int k = 5;

            long startTime = System.nanoTime();
            boolean isPrime = isProbablePrime(n, k);
            long endTime = System.nanoTime();

            if (isPrime) {
                System.out.println(n + " is probably prime.");
            } else {
                System.out.println(n + " is composite.");
            }
            System.out.println("Execution time: " + (endTime - startTime) + " nanoseconds.");
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in the file.");
        }
    }

    public static BigInteger modularExponentiation(BigInteger base, BigInteger exp, BigInteger mod) {
        BigInteger result = BigInteger.ONE;
        base = base.mod(mod);
        while (exp.compareTo(BigInteger.ZERO) > 0) {
            if (exp.mod(BigInteger.TWO).equals(BigInteger.ONE)) {
                result = result.multiply(base).mod(mod);
            }
            exp = exp.divide(BigInteger.TWO);
            base = base.multiply(base).mod(mod);
        }
        return result;
    }

    public static int jacobiSymbol(BigInteger a, BigInteger n) {
        if (n.compareTo(BigInteger.ZERO) <= 0 || n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            return 0;
        }
        int result = 1;
        a = a.mod(n);

        while (!a.equals(BigInteger.ZERO)) {
            while (a.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                a = a.divide(BigInteger.TWO);
                if (n.mod(BigInteger.valueOf(8)).equals(BigInteger.valueOf(3)) ||
                    n.mod(BigInteger.valueOf(8)).equals(BigInteger.valueOf(5))) {
                    result = -result;
                }
            }
            BigInteger temp = a;
            a = n;
            n = temp;

            if (a.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)) &&
                n.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
                result = -result;
            }
            a = a.mod(n);
        }
        return n.equals(BigInteger.ONE) ? result : 0;
    }

    public static boolean isProbablePrime(BigInteger n, int k) {
        if (n.compareTo(BigInteger.TWO) < 0) {
            return false;
        }
        if (n.equals(BigInteger.TWO)) {
            return true;
        }
        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            return false;
        }

        Random rand = new Random();
        for (int i = 0; i < k; i++) {
            BigInteger a = new BigInteger(n.bitLength() - 1, rand).add(BigInteger.TWO);
            while (a.compareTo(n.subtract(BigInteger.TWO)) > 0) {
                a = new BigInteger(n.bitLength() - 1, rand).add(BigInteger.TWO);
            }

            int jacobi = jacobiSymbol(a, n);
            BigInteger modExp = modularExponentiation(a, n.subtract(BigInteger.ONE).divide(BigInteger.TWO), n);

            if (jacobi == 0 || !modExp.equals(BigInteger.valueOf(jacobi).mod(n))) {
                return false;
            }
        }
        return true;
    }
}
