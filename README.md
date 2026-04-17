AMF Hash and AMF Division

Author: Alexandre Martins de FreitasDate: 2026

Abstract

We introduce AMF Hash, a deterministic hashing method combined with a structured quotient–remainder representation (AMF Division). Unlike traditional hash functions producing a single scalar value, this approach represents hashes as a pair (q, r), enabling multi-scale interpretation.

AMF Hash Definition

Given a byte array D:

H(D) = n + Σ (d_i + α)(i+1)

Where:

n = length of D

α = constant (e.g., 129)

AMF Division

For modulus M:

H(D) = Mq + r0 ≤ r < M

Define:

AMFHash_M(D) = (q, r)

Interpretation

q → coarse distribution

r → fine structure

Properties

Deterministic

O(n) complexity

Reversible representation via H = Mq + r

Notes

This is not a cryptographic hash and does not guarantee collision resistance.

License

Public disclosure for authorship and research purposes.
