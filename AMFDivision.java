import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public final class AMFDivision {

    public record AMFFrame(BigInteger quotient, BigInteger remainder, BigInteger divisor) {
        public BigInteger reconstruct() {
            return divisor.multiply(quotient).add(remainder);
        }

        @Override
        public String toString() {
            return "AMFFrame{q=" + quotient + ", r=" + remainder + ", b=" + divisor + "}";
        }
    }

    public static AMFFrame divide(BigInteger dividend, BigInteger divisor) {
        if (divisor.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Division by zero");
        }

        BigInteger[] qr = dividend.divideAndRemainder(divisor);

        BigInteger q = qr[0];
        BigInteger r = qr[1];

        // Normalize remainder for Euclidean form: 0 <= r < |divisor|
        if (r.signum() < 0) {
            if (divisor.signum() > 0) {
                q = q.subtract(BigInteger.ONE);
                r = r.add(divisor);
            } else {
                q = q.add(BigInteger.ONE);
                r = r.subtract(divisor);
            }
        }

        return new AMFFrame(q, r, divisor);
    }

    public static List<BigInteger> expand(BigInteger dividend, BigInteger divisor, int base, int digits) {
        if (base < 2) {
            throw new IllegalArgumentException("Base must be >= 2");
        }

        List<BigInteger> output = new ArrayList<>();
        BigInteger radix = BigInteger.valueOf(base);

        AMFFrame frame = divide(dividend, divisor);

        // Whole part
        output.add(frame.quotient());

        BigInteger remainder = frame.remainder();

        // Fractional / infinite-frame continuation
        for (int i = 0; i < digits && remainder.signum() != 0; i++) {
            remainder = remainder.multiply(radix);

            AMFFrame next = divide(remainder, divisor);

            output.add(next.quotient());
            remainder = next.remainder();
        }

        return output;
    }

    public static void main(String[] args) {
        BigInteger a = BigInteger.valueOf(7);
        BigInteger b = BigInteger.valueOf(3);

        AMFFrame frame = divide(a, b);
        System.out.println(frame);

        List<BigInteger> expansion = expand(a, b, 10, 10);
        System.out.println(expansion);
        // Output: [2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3]
    }
}
