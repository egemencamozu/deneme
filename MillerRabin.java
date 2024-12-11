import java.math.BigInteger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class MillerRabin {
    public static void main(String[] args) {
        // Dosyadan sayıyı oku
        BigInteger number = readBigIntegerFromFile("large_number.txt");

        if (number != null) {
            int iterations = 5; // Deneme sayısı

            long startTime = System.nanoTime();
            boolean isPrime = isPrime(number, iterations);
            long endTime = System.nanoTime();

            System.out.println(number + " is prime? " + isPrime);
            System.out.println("Calculation took " + (endTime - startTime) + " nanoseconds.");
        } else {
            System.out.println("Dosyadan sayıyı okuyamadım.");
        }
    }

    // Dosyadan büyük bir sayıyı okuyan metod
    public static BigInteger readBigIntegerFromFile(String fileName) {
        BigInteger number = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            number = new BigInteger(line); // Dosyadaki ilk satırı BigInteger'a dönüştür
        } catch (IOException e) {
            e.printStackTrace();
        }
        return number;
    }

    public static boolean isPrime(BigInteger n, int iterations) {
        if (n.compareTo(BigInteger.TWO) < 0) return false;
        if (n.equals(BigInteger.TWO) || n.equals(BigInteger.valueOf(3))) return true;
        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) return false;

        // Decompose n-1 as 2^e * d
        BigInteger d = n.subtract(BigInteger.ONE);
        int e = 0;
        while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            d = d.divide(BigInteger.TWO);
            e++;
        }

        Random random = new Random();
        for (int i = 0; i < iterations; i++) {
            BigInteger a = BigInteger.TWO.add(new BigInteger(n.bitLength() - 2, random).mod(n.subtract(BigInteger.TWO)));
            BigInteger x = a.modPow(d, n); // Compute a^d % n

            if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE))) {
                continue;
            }

            boolean isComposite = true;
            for (int j = 0; j < e - 1; j++) {
                x = x.modPow(BigInteger.TWO, n);
                if (x.equals(n.subtract(BigInteger.ONE))) {
                    isComposite = false;
                    break;
                }
            }
            if (isComposite) return false;
        }
        return true; // "Probably prime"
    }
}