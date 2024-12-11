import java.math.BigInteger;
import java.io.FileWriter;
import java.io.IOException;

public class LargeNumberToFile {
    public static void main(String[] args) {
        // 2^44497 - 1 sayısını hesapla
        BigInteger number = new BigInteger("2").pow(2203).subtract(BigInteger.ONE);
        /* longs[0]=BigInteger.valueOf(233);   //3 basamak
        longs[1]=BigInteger.valueOf(7916);  // 4 basamak
        longs[2]=BigInteger.valueOf(131071); // 2^17-1
        longs[3]=BigInteger.valueOf(2147483647);   // 2^31-1  10 basamak
        longs[4]=BigInteger.valueOf(2^61-1);   //19 basamak
        longs[5]=BigInteger.valueOf(2^89-1);  //27 basamak
        longs[6]=BigInteger.valueOf(2^107-1); //33 basamak
        longs[7]=bigInteger;
        longs[8]=BigInteger.valueOf(2^127-1);  //39 basamak
        longs[9]=BigInteger.valueOf(2^521-1); //157 basamak
        longs[10]=BigInteger.valueOf(2^607-1);  //183
        longs[11]=BigInteger.valueOf(2^1279-1);//386 basamak
        longs[12]=BigInteger.valueOf(2^2203-1);//664 basamak */
        
        // Sayıyı dosyaya yaz
        try (FileWriter writer = new FileWriter("large_number.txt")) {
            writer.write(number.toString());
            System.out.println("Sayının tüm basamakları large_number.txt dosyasına yazıldı.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
