import java.util.Random;
import java.util.zip.CRC32;

public class AMFvsCRC32Benchmark {

    // AMF hash (int version)
    public static int amfHash(byte[] data) {
        int hash = data.length;
        for (int i = 0; i < data.length; i++) {
            hash += (data[i] + 129) * (i + 1);
        }
        return hash;
    }

    // CRC32 (converted to int)
    public static int crc32Hash(byte[] data) {
        CRC32 crc = new CRC32();
        crc.update(data);
        return (int) crc.getValue();
    }

    public static void main(String[] args) {

        final int DATA_SIZE = 1024;       // 1 KB per input
        final int ITERATIONS = 5_000_000; // adjust depending on CPU
        final int WARMUP = 1_000_000;

        byte[] data = new byte[DATA_SIZE];
        Random random = new Random(42);

        // Fill data once
        random.nextBytes(data);

        System.out.println("Starting warmup...");

        // Warmup (JIT optimization)
        for (int i = 0; i < WARMUP; i++) {
            amfHash(data);
            crc32Hash(data);
        }

        System.out.println("Benchmark started...");

        // --- AMF Benchmark ---
        long startAMF = System.nanoTime();
        int amfResult = 0;

        for (int i = 0; i < ITERATIONS; i++) {
            amfResult ^= amfHash(data);
        }

        long endAMF = System.nanoTime();

        // --- CRC32 Benchmark ---
        long startCRC = System.nanoTime();
        int crcResult = 0;

        for (int i = 0; i < ITERATIONS; i++) {
            crcResult ^= crc32Hash(data);
        }

        long endCRC = System.nanoTime();

        long amfTime = endAMF - startAMF;
        long crcTime = endCRC - startCRC;

        System.out.println("\nResults:");
        System.out.println("AMF Hash Time: " + amfTime / 1_000_000 + " ms");
        System.out.println("CRC32 Time:    " + crcTime / 1_000_000 + " ms");

        double ratio = (double) crcTime / amfTime;
        System.out.printf("Speed ratio (CRC32 / AMF): %.2fx\n", ratio);

        // Prevent dead-code elimination
        System.out.println("\nIgnore values:");
        System.out.println("AMF: " + amfResult);
        System.out.println("CRC: " + crcResult);
    }
}
