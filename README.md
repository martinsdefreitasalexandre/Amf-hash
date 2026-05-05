# AMF Hash and AMF Division

**Author:** Alexandre Martins de Freitas (Alexdkk)  
**Version:** 4.0  
**Status:** Experimental / research project  
**Language:** Java, C  

AMF Hash is an experimental family of fast, non-cryptographic hashing algorithms created to explore weighted byte-index hashing, quotient–remainder structure, and high-speed checksum-style computation.

The project includes:

- **AMF Hash v1** — original weighted-sum hash
- **AMF Hash v2** — improved mixed version
- **AMF QuickHash / AMF v4** — faster block-based multi-lane version
- **AMF Division** — quotient–remainder representation of hash values

---

## Overview

AMF Hash started from a simple weighted byte-index model:

```text
H(D) = n + Σ((dᵢ + 129) × (i + 1))
