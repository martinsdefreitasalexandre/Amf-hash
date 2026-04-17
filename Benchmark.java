import java.util.Random;
import java.util.zip.CRC32;

public class Benchmark {

    public static long amfHash(byte[] data) {
        long hash = data.length;
        for (int i = 0; i < data.length; i++) {
            hash += (data[i] + 129) * (i + 1);
        }
        return hash;
    }

    public static long crc32(byte[] data) {
        CRC32 crc = new CRC32();
        crc.update(data);
        return crc.getValue();
    }

    public static void main(String[] args) {
        int size = 10_000_000; // 10MB
        byte[] data = new byte[size];
        new Random(42).nextBytes(data);

        // Warm-up (VERY important for JVM)
        for (int i = 0; i < 5; i++) {
            amfHash(data);
            crc32(data);
        }

        // AMF benchmark
        long start = System.nanoTime();
        long amf = amfHash(data);
        long end = System.nanoTime();
        long amfTime = end - start;

        // CRC32 benchmark
        start = System.nanoTime();
        long crc = crc32(data);
        end = System.nanoTime();
        long crcTime = end - start;

        System.out.println("AMF Hash: " + amf + " Time: " + (amfTime / 1_000_000.0) + " ms");
        System.out.println("CRC32   : " + crc + " Time: " + (crcTime / 1_000_000.0) + " ms");

        System.out.println("Speed ratio (CRC / AMF): " + ((double) crcTime / amfTime));
    }
}
