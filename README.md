# 📌 AMF Hash

**AMF Hash** is a lightweight hashing algorithm focused on **high performance and simplicity**, using only basic arithmetic operations.

It is designed as an experimental alternative to traditional checksums such as CRC32, prioritizing speed and ease of implementation.

---

## ⚙️ Core Idea

AMF Hash computes a deterministic value based on:
- input length  
- byte values  
- positional weighting  

```java
Amfv1
        int hash = data.length;
        for (int i = 0; i < data.length; i++) {
            hash += (data[i] + 129) * (i + 1);
        }
Amfv2
        int hash = data.length * 0x9E3779B9; // length included strongly

        for (int i = 0; i < data.length; i++) {
            int value = (data[i] + 129) * (i + 1);

            hash ^= value;
            hash *= 0x85EBCA6B;
            hash ^= (hash >>> 13);
        }

        hash ^= (hash >>> 16);
        hash *= 0xC2B2AE35;
        hash ^= (hash >>> 16);
AMFv3
 private static final int P1 = 0x9E3779B1;
    private static final int P2 = 0x85EBCA77;
    private static final int P3 = 0xC2B2AE3D;
    private static final int P4 = 0x27D4EB2F;

    public static int hash(byte[] data) {
        int len = data.length;
        int hash = len * P1;

        int i = 0;

        while (i + 4 <= len) {
            int block =
                    (data[i] & 0xFF) |
                    ((data[i + 1] & 0xFF) << 8) |
                    ((data[i + 2] & 0xFF) << 16) |
                    ((data[i + 3] & 0xFF) << 24);

            int indexed = block ^ ((i + 1) * P4);

            hash += indexed * P2;
            hash = Integer.rotateLeft(hash, 13);
            hash *= P1;

            i += 4;
        }

        while (i < len) {
            int value = (data[i] + 129) * (i + 1);

            hash ^= value * P3;
            hash = Integer.rotateLeft(hash, 11);
            hash *= P2;

            i++;
        }

        // final avalanche
        hash ^= hash >>> 16;
        hash *= P2;
        hash ^= hash >>> 13;
        hash *= P3;
        hash ^= hash >>> 16;

        return hash;
    }



	•	Performance-first
No lookup tables, no complex bit operations
	•	Portability
Easily implemented in C, Java, Python, or embedded systems
	•	Deterministic behavior
Same input → same output

⸻

⚡ Performance Considerations

AMF Hash can be competitive in:
	•	software-only environments
	•	tight loops and real-time systems

Compared to CRC32:
	•	May be faster without hardware acceleration
	•	Usually slower when CRC32 hardware instructions are available

⸻

⚠️ Limitations

AMF Hash is not:
	•	a cryptographic hash
	•	designed for secure data integrity
	•	mathematically proven for collision resistance

CRC32 and similar algorithms are based on polynomial error-detection theory, while AMF is an experimental arithmetic approach.

⸻

🧪 Use Cases
	•	Fast internal checksums
	•	Data indexing / bucketing
	•	Experimental hashing research
	•	Performance benchmarking

⸻

🔬 Future Work
	•	Statistical randomness testing
	•	Collision rate analysis
	•	SIMD / vectorized optimization
	•	Formal mathematical modeling

⸻

👤 Author

Alexandre Martins de Freitas
Software developer and independent researcher focused on performance-driven algorithms and systems.

⸻

📄 License

License (MIT)

## 📊 Benchmark Visuals

Benchmark run using the C implementation with software CRC32.

**Setup**
- Input size: `1 KB`
- Iterations: `5,000,000`
- Compiler: `gcc -O3`
- CRC32 mode: software table implementation
- Output type: `uint32_t` / int-style 32-bit result

### Results

| Algorithm | Time | Relative Result |
|---|---:|---:|
| AMF Hash | `2.691271s` | `1.00x` baseline |
| CRC32 software | `16.986898s` | `6.31x` slower |

![AMF vs CRC32 Benchmark](amf_vs_crc32_benchmark.png)

### Notes

This benchmark compares AMF Hash against a software CRC32 implementation.  
Modern CPUs may include CRC32 hardware instructions, which can make CRC32 significantly faster than software CRC32.

AMF Hash is experimental and should not be treated as cryptographic or as a proven replacement for CRC32 error detection.
