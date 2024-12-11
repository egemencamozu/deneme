import java.math.BigInteger;
import java.util.List;

public class compare {


    public static void main(String[] args) {
        BigInteger bigInteger = new BigInteger("35742549198872617291353508656626642567");

        BigInteger[] longs = new BigInteger[13];
        longs[0]=BigInteger.valueOf(233);   //3 basamak
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
        longs[12]=BigInteger.valueOf(2^2203-1);//664 basamak

        for (int i = 0; i <longs.length ; i++) {


            // FOR AKS TEST
            long tt = System.currentTimeMillis();
            boolean res = AksPrimalityTest.aks((longs[i]));
            tt = System.currentTimeMillis() - tt;
            String resstring = "";
            if (res) {
                resstring = "Prime";
            } else resstring = "Composite";
            System.out.println("AKS TEST RESULT FOR " + longs[i] + " is " + resstring + " in " + tt + "ms");


            // FOR FERMAT TEST
            long t1 = System.currentTimeMillis();
            String res1 = Fermat.isProbablePrime(longs[i].longValue(), 10);
            t1 = System.currentTimeMillis() - t1;
            System.out.println("Fermat Test result for " + longs[i] + " is " + res1 + " in " + t1 + "ms");

            //For Miller Rabin
            long t2 = System.currentTimeMillis();
            boolean res2 = MillerRabin.isPrime(longs[i], 5);
            t2 = System.currentTimeMillis() - t2;
            String res2string = "";
            if (res2) {
                res2string = " probably Prime";
            } else res2string = "probably Composite";
            System.out.println("MillerRabin Test result for " + longs[i] + " is " + res2string + " in " + t2 + "ms");

            //FOR SOLOVAY-STRASSEN
            long t3 = System.currentTimeMillis();
            boolean res3 = SolovayStrassen.isProbablePrime(longs[i].longValue(), 5);
            t3 = System.currentTimeMillis() - t3;
            String res3string = "";
            if (res3) {
                res3string = " probably Prime";
            } else res3string = "probably Composite";
            System.out.println("SolovayStrassen Test Result for " + longs[i] + " is " + res3string + " in " + t3 + "ms");


            System.out.println("---------------------------------------");


        }

    }




}











































