package com.amf;

public final class AMFDivision {

    private AMFDivision() {
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
            return (double) dividend / (double) divisor;
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

    public static Result divide(long a, long b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }

        long q = a / b;
        long r = a % b;

        // Normalize so remainder is always 0 <= r < |b|
        if (r < 0) {
            if (b > 0) {
                q -= 1;
                r += b;
            } else {
                q += 1;
                r -= b; // subtracting negative b adds |b|
            }
        }

        return new Result(a, b, q, r);
    }

    public static void main(String[] args) {
        System.out.println(divide(10, 3));    // 3 + 1/3
        System.out.println(divide(3, 10));    // 0 + 3/10
        System.out.println(divide(-10, 3));   // -4 + 2/3
        System.out.println(divide(10, -3));   // -3 + 1/-3
        System.out.println(divide(-10, -3));  // 4 + 2/-3
    }
}
