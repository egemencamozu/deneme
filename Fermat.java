import java.math.BigInteger;
import java.util.Random;
import java.io.*;

public class Fermat {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("large_number.txt"))) {
            String numberStr = reader.readLine(); // Dosyadan ilk satırı oku.

            if (numberStr != null) {
                BigInteger number = new BigInteger(numberStr.trim());
                int k = 10; // Test sayısı

                long startTime = System.nanoTime();
                String result = isProbablePrime(number, k);
                long endTime = System.nanoTime();

                System.out.println(number + " is " + result + ".");
                System.out.println("Calculation took " + (endTime - startTime) + " nanoseconds.");
            } else {
                System.out.println("File is empty.");
            }
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

    public static String isProbablePrime(BigInteger n, int k) {
        if (n.compareTo(BigInteger.ONE) <= 0) return "Composite";
        if (n.equals(BigInteger.TWO)) return "Prime";
        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) return "Composite";

        Random random = new Random();
        for (int i = 0; i < k; i++) {
            BigInteger a = new BigInteger(n.bitLength() - 1, random).add(BigInteger.TWO);
            // a için aralık 2 <= a <= n-2 olmalı
            while (a.compareTo(n.subtract(BigInteger.ONE)) >= 0) {
                a = new BigInteger(n.bitLength() - 1, random).add(BigInteger.TWO);
            }

            if (!modularExponentiation(a, n.subtract(BigInteger.ONE), n).equals(BigInteger.ONE)) {
                return "Composite";
            }
        }

        return "Prime";
    }
}
