#include <stdio.h>
#include <stdlib.h>

typedef struct {
    long dividend;
    long divisor;
    long quotient;
    long remainder;
} AMFDivisionResult;

AMFDivisionResult amf_divide(long a, long b) {
    if (b == 0) {
        fprintf(stderr, "Error: division by zero\n");
        exit(EXIT_FAILURE);
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
            r -= b;
        }
    }

    AMFDivisionResult result;
    result.dividend = a;
    result.divisor = b;
    result.quotient = q;
    result.remainder = r;
    return result;
}

double amf_to_double(AMFDivisionResult result) {
    return (double)result.dividend / (double)result.divisor;
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
    print_amf_result(amf_divide(10, 3));
    print_amf_result(amf_divide(3, 10));
    print_amf_result(amf_divide(-10, 3));
    print_amf_result(amf_divide(10, -3));
    print_amf_result(amf_divide(-10, -3));
    return 0;
}
