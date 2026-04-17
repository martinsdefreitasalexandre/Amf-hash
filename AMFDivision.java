package com.amf;

/**
 * AMF Division
 * Author: Alexandre Martins de Freitas
 *
 * Formal representation:
 * a = bq + r, with 0 <= r < |b|
 *
 * So:
 * a / b = q + r / b
 */
public final class AMFDivision {

    private AMFDivision() {
        // Utility class
    }

    public static final class Result {
        private final long dividend;
        private final long divisor;
        private final long quotient;
        private final long remainder;

        public Result(long dividend, long divisor, long quotient, long remainder) {
            this.dividend = dividend;
            this.divisor = divisor;
            this.quotient = quotient;
            this.remainder = remainder;
        }

        public long getDividend() {
            return dividend;
        }

        public long getDivisor() {
            return divisor;
        }

        public long getQuotient() {
            return quotient;
        }

        public long getRemainder() {
            return remainder;
        }

        public double toDouble() {
            return (double) quotient + ((double) remainder / (double) divisor);
        }

        public String asExpression() {
            return quotient + " + " + remainder + "/" + divisor;
        }

        @Override
        public String toString() {
            return "AMFDivision.Result{" +
                    "dividend=" + dividend +
                    ", divisor=" + divisor +
                    ", quotient=" + quotient +
                    ", remainder=" + remainder +
                    ", expression='" + asExpression() + '\'' +
                    '}';
        }
    }

    /**
     * Computes the AMF division of a by b.
     *
     * Returns q and r such that:
     * a = bq + r
     * 0 <= r < |b|
     */
    public static Result divide(long a, long b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }

        long absB = Math.abs(b);

        // floorDiv/floorMod give a canonical Euclidean-style result
        long q = Math.floorDiv(a, absB);
        long r = Math.floorMod(a, absB);

        return new Result(a, b, q, r);
    }

    public static void main(String[] args) {
        Result r1 = divide(10, 3);
        Result r2 = divide(3, 10);
        Result r3 = divide(-10, 3);

        System.out.println(r1);
        System.out.println("Numeric value: " + r1.toDouble());
        System.out.println();

        System.out.println(r2);
        System.out.println("Numeric value: " + r2.toDouble());
        System.out.println();

        System.out.println(r3);
        System.out.println("Numeric value: " + r3.toDouble());
    }
}
