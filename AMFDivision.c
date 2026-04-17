#include <stdio.h>
#include <stdlib.h>

typedef struct {
    long dividend;
    long divisor;
    long quotient;
    long remainder;
} AMFDivisionResult;

/*
 * Computes AMF division:
 * a = bq + r
 * 0 <= r < |b|
 *
 * Uses Euclidean-style normalization.
 */
AMFDivisionResult amf_divide(long a, long b) {
    if (b == 0) {
        fprintf(stderr, "Error: division by zero\n");
        exit(EXIT_FAILURE);
    }

    long abs_b = labs(b);
    long q = a / abs_b;
    long r = a % abs_b;

    /*
     * Normalize so remainder is always non-negative:
     * 0 <= r < |b|
     */
    if (r < 0) {
        q -= 1;
        r += abs_b;
    }

    AMFDivisionResult result;
    result.dividend = a;
    result.divisor = b;
    result.quotient = q;
    result.remainder = r;

    return result;
}

double amf_to_double(AMFDivisionResult result) {
    return (double)result.quotient +
           ((double)result.remainder / (double)result.divisor);
}

void print_amf_result(AMFDivisionResult result) {
    printf("AMFDivisionResult{dividend=%ld, divisor=%ld, quotient=%ld, remainder=%ld, expression=%ld + %ld/%ld}\n",
           result.dividend,
           result.divisor,
           result.quotient,
           result.remainder,
           result.quotient,
           result.remainder,
           result.divisor);
}

int main(void) {
    AMFDivisionResult r1 = amf_divide(10, 3);
    AMFDivisionResult r2 = amf_divide(3, 10);
    AMFDivisionResult r3 = amf_divide(-10, 3);

    print_amf_result(r1);
    printf("Numeric value: %.12f\n\n", amf_to_double(r1));

    print_amf_result(r2);
    printf("Numeric value: %.12f\n\n", amf_to_double(r2));

    print_amf_result(r3);
    printf("Numeric value: %.12f\n", amf_to_double(r3));

    return 0;
}
