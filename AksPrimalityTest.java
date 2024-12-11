import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;
import java.util.Scanner;

/*
 * Implementation of the AKS primality test
 *
 *
 *
 *
 */

public class AksPrimalityTest {

    static BigInteger ZERO = BigInteger.ZERO;
    static BigInteger ONE = BigInteger.ONE;
    static BigInteger TWO = BigInteger.valueOf(2L);

    static class Polynomial {

        ArrayList<BigInteger> coeff;
        int degree;

        Polynomial() {
            degree = 0;
            coeff = new ArrayList<>();
            coeff.add(ZERO);
        }

        Polynomial(int len) {
            degree = len;
            coeff = new ArrayList<>();
            for (int i = 0; i <= len; i++)
                coeff.add(ZERO);
        }

        Polynomial(Polynomial P) {
            degree = P.getDegree();
            coeff = new ArrayList<>();
            for (int i = 0; i <= degree; i++)
                coeff.add(P.getCoef(i));
        }

        void setCoef(BigInteger C, int i) {
            if (i < 0)
                return;
            if (i > degree) {
                for (int j = degree + 1; j < i; j++)
                    coeff.add(j, ZERO);
                coeff.add(i, C);
                degree = i;
            } else {
                coeff.set(i, C);
            }
        }

        BigInteger getCoef(int i) {
            if (i > degree)
                return ZERO;
            return coeff.get(i);
        }

        int getDegree() {
            return degree;
        }

        boolean isEqual(Polynomial P) {
            if (degree != P.getDegree())
                return false;
            for (int i = 0; i < degree; i++)
                if (!coeff.get(i).equals(P.getCoef(i)))
                    return false;
            return true;
        }

        void clear() {
            coeff.clear();
            degree = 0;
            coeff.add(ZERO);
        }

        void compact() {
            for (; degree > 0; degree--)
                if (!coeff.get(degree).equals(ZERO))
                    break;
                else
                    coeff.remove(degree);
        }

        @Override
        public String toString() {
            StringBuilder res = new StringBuilder();
            for (int i = degree; i > 0; i--)
                res.append(String.format("%s x^%d +", getCoef(i), i));
            res.append(String.format("%s x^0", getCoef(0)));
            return res.toString();
        }

    }

    static void mod_mult(Polynomial rop, Polynomial _x, Polynomial _y, BigInteger mod, int polymod) {
        Polynomial x = new Polynomial(_x);
        Polynomial y = new Polynomial(_y);
        rop.clear();

        int xdeg = x.getDegree();
        int ydeg = y.getDegree();
        int mdeg = xdeg > ydeg ? xdeg : ydeg;

        int k = 0;
        for (; k < polymod; k++) {
            BigInteger sum = ZERO;
            int i = 0;
            for (; i <= k; i++)
                sum = sum.add(x.getCoef(i).multiply(y.getCoef(k - i).add(y.getCoef(k + polymod - i))));
            for (; i <= k + polymod; i++)
                sum = sum.add(x.getCoef(i).multiply(y.getCoef(k + polymod - i)));
            rop.setCoef(sum.mod(mod), k);
            if (k > mdeg && sum.equals(ZERO))
                break;
        }
        rop.compact();
    }

    static void mod_power(Polynomial rop, Polynomial x, BigInteger power, BigInteger mult_mod, int poly_mod) {
        rop.clear();
        rop.setCoef(ONE, 0);
        int i = power.bitLength();
        for (; i >= 0; i--) {
            mod_mult(rop, rop, rop, mult_mod, poly_mod);
            if (power.testBit(i))
                mod_mult(rop, rop, x, mult_mod, poly_mod);
        }
        rop.compact();
    }

    static class Sieve {

        BitSet table;
        int size;

        public Sieve() {
            table = new BitSet();
            size = 2;
        }

        boolean isPrime(BigInteger r) {
            int rul = r.bitCount();
            if (size >= rul)
                return !table.get(rul);
            size <<= 1;
            for (int i = 2; i <= size; i++)
                if (!table.get(i))
                    for (int j = i * 2; j <= size; j += i)
                        table.set(j);
            return !table.get(rul);
        }

    }

    static boolean perfect_power(long n) {
        int maxpow = Long.numberOfTrailingZeros(Long.highestOneBit(n)) + 2;
        long phi = n;
        for (int pow = 2; pow <= maxpow; pow++) {
            long lo = 2, hi = phi;
            while (lo <= hi) {
                long m = lo + (hi - lo) / 2;
                long p = (long) Math.pow(m, pow);
                if (p == n)
                    return true;
                if (p >= n)
                    hi = m - 1;
                else
                    lo = m + 1;
            }
            phi = hi;
        }
        return false;
    }

    static boolean perfect_power(BigInteger n) {
        int maxpow = n.bitLength() + 2;
        BigInteger phi = n;
        for (int pow = 2; pow <= maxpow; pow++) {
            BigInteger lo = TWO, hi = phi;
            while (lo.compareTo(hi) <= 0) {
                BigInteger m = lo.add(hi.subtract(lo).divide(TWO));
                BigInteger p = m.pow(pow);
                if (p.equals(n))
                    return true;
                if (p.compareTo(n) >= 0)
                    hi = m.subtract(ONE);
                else
                    lo = m.add(ONE);
            }
            phi = hi;
        }
        return false;
    }

    static BigInteger root(BigInteger n, BigInteger lo, BigInteger hi, int pow) {
        while (lo.compareTo(hi) <= 0) {
            BigInteger m = lo.add(hi.subtract(lo).divide(TWO));
            BigInteger pp = m.pow(pow);
            if (pp.equals(n))
                return m;
            if (pp.compareTo(n) >= 0)
                hi = m.subtract(ONE);
            else
                lo = m.add(ONE);
        }
        return hi;
    }

    static boolean aks(BigInteger n) {
        if (perfect_power(n))
            return false;
        Sieve s = new Sieve();
        BigInteger r = TWO;
        BigInteger logn = BigInteger.valueOf(n.bitLength());
        BigInteger limit = logn.multiply(logn);
        limit = limit.shiftLeft(2);

        while (r.compareTo(n) < 0) {
            if (n.mod(r).equals(ZERO)) {
                return false;
            }
            boolean failed = false;
            if (s.isPrime(r)) {
                BigInteger i = ONE;
                for (; i.compareTo(limit) <= 0; i = i.add(ONE)) {
                    BigInteger res = n.modPow(i, r);
                    if (res.equals(ONE)) {
                        failed = true;
                        break;
                    }
                }
                if (!failed)
                    break;
            }
            r = r.add(ONE);
        }

        if (n.compareTo(r) <= 0)
            return true;

        BigInteger sqrtr = root(r, ONE, n, 2);
        BigInteger polylimit = sqrtr.add(ONE).multiply(TWO).multiply(logn);
        if (polylimit.compareTo(n) >= 0)
            polylimit = n.subtract(ONE);

        int intr = (int) r.longValue();
        for (BigInteger a = ONE; a.compareTo(polylimit) <= 0; a = a.add(ONE)) {
            BigInteger final_size = n.mod(r);
            Polynomial compare = new Polynomial(final_size.intValue());
            compare.setCoef(ONE, final_size.intValue());
            compare.setCoef(a, 0);
            Polynomial res = new Polynomial(intr);
            Polynomial base = new Polynomial(1);
            compare.compact();
            res.compact();
            base.compact();
            base.setCoef(a, 0);
            base.setCoef(ONE, 1);
            mod_power(res, base, n, n, intr);
            if (!res.isEqual(compare)) {
                return false;
            }
        }
        return true;
    }

    static boolean iter(BigInteger n) {
        BigInteger cap = root(n, ONE, n, 2).add(ONE);
        for (BigInteger chk = TWO; chk.compareTo(cap) <= 0; chk = chk.add(ONE))
            if (n.mod(chk).equals(ZERO))
                return false;
        return true;
    }



    void benchmark() {
        int N = 10;
        BigInteger[] primes = new BigInteger[N];
        Random r = new Random();
        for (int i = 0; i < N; i++)
            primes[i] = new BigInteger(60, r);

        System.out.printf("%15s | %15s | %15s | %15s%n", "Value", "AKS", "Miller-Rabin", "Trial Division");
        System.out.println(
                "---------------------------------------------------------------------------------------------------");
        for (int i = 0; i < N; i++) {
            long AKSTime = System.currentTimeMillis();
            boolean aks = aks(primes[i]);
            AKSTime = System.currentTimeMillis() - AKSTime;

            long MRTime = System.currentTimeMillis();
            boolean rab = primes[i].isProbablePrime(50000000);
            MRTime = System.currentTimeMillis() - MRTime;

            long ITERTime = System.currentTimeMillis();
            boolean tdiv = iter(primes[i]);
            ITERTime = System.currentTimeMillis() - ITERTime;

            System.out.printf("%15s | %15s | %15s | %15s%n", primes[i], String.format("%d ms", AKSTime),
                    String.format("%d ms", MRTime), String.format("%d ms", ITERTime));
            if (!(aks && rab && tdiv)) {
                System.err.println("INCORRECT RESULT");
            }
        }

    }

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        System.out.print("Enter a number: ");
        String inp = s.next();
        s.close();
        BigInteger N = new BigInteger(inp);
        long tt = System.currentTimeMillis();
        boolean res = aks(N);
        tt = System.currentTimeMillis() - tt;
        System.out.printf("%s is %sprime (%d ms)%n", N, res ? "" : "not ", tt);

    }

}