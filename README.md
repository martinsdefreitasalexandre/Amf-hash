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
int hash = data.length;
for (int i = 0; i < data.length; i++) {
    hash += (data[i] + 129) * (i + 1);
}
