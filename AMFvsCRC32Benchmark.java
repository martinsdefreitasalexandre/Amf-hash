import java.util.Random;
import java.util.zip.CRC32;

/**
 * Benchmark AMF hash against Java CRC32.
 *
 * Run normally:
 *   javac AMFvsCRC32Benchmark.java
 *   java AMFvsCRC32Benchmark
 *
 * Run with CRC32 intrinsics disabled:
 *   java -XX:+UnlockDiagnosticVMOptions -XX:-UseCRC32Intrinsics AMFvsCRC32Benchmark
 */
public class AMFvsCRC32Benchmark {

    private static final int[] SIZES = {
            256,
            1024,
            4096,
            65536,
            1_048_576,
            10_485_760
    };

    private static final int WARMUP_RUNS = 8;
    private static final int MEASURE_RUNS = 12;
    private static final long SEED = 42L;

    public static long amfHash(byte[] data) {
        long hash = data.length;
        for (int i = 0; i < data.length; i++) {
            hash += (data[i] + 129L) * (i + 1L);
        }
        return hash;
    }

    public static long crc32(byte[] data) {
        CRC32 crc = new CRC32();
        crc.update(data);
        return crc.getValue();
    }

    private static byte[] randomBytes(int size, Random random) {
        byte[] data = new byte[size];
        random.nextBytes(data);
        return data;
    }

    private static double mibPerSecond(int bytes, long nanos) {
        double mib = bytes / (1024.0 * 1024.0);
        double seconds = nanos / 1_000_000_000.0;
        return mib / seconds;
    }

    private static long benchAMF(byte[] data, int runs) {
        long total = 0L;
        long sink = 0L;
        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            sink ^= amfHash(data);
            long end = System.nanoTime();
            total += (end - start);
        }
        if (sink == Long.MIN_VALUE) {
            System.out.println("Impossible sink: " + sink);
        }
        return total / runs;
    }

    private static long benchCRC(byte[] data, int runs) {
        long total = 0L;
        long sink = 0L;
        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            sink ^= crc32(data);
            long end = System.nanoTime();
            total += (end - start);
        }
        if (sink == Long.MIN_VALUE) {
            System.out.println("Impossible sink: " + sink);
        }
        return total / runs;
    }

    private static void warmup(byte[] data) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            amfHash(data);
            crc32(data);
        }
    }

    private static String humanSize(int size) {
        if (size >= 1024 * 1024) {
            return (size / (1024 * 1024)) + " MB";
        }
        if (size >= 1024) {
            return (size / 1024) + " KB";
        }
        return size + " B";
    }

    public static void main(String[] args) {
        System.out.println("Benchmark: AMF Hash vs java.util.zip.CRC32");
        System.out.println("Warmup runs: " + WARMUP_RUNS + ", measured runs: " + MEASURE_RUNS);
        System.out.println("Java version: " + System.getProperty("java.version"));
        System.out.println("JVM: " + System.getProperty("java.vm.name"));
        System.out.println();

        System.out.printf("%-10s %-18s %-18s %-12s %-18s %-18s %-12s%n",
                "Size",
                "AMF ns/op",
                "AMF MiB/s",
                "AMF value",
                "CRC32 ns/op",
                "CRC32 MiB/s",
                "CRC32 value");

        Random random = new Random(SEED);

        for (int size : SIZES) {
            byte[] data = randomBytes(size, random);
            warmup(data);

            long amfAvgNanos = benchAMF(data, MEASURE_RUNS);
            long crcAvgNanos = benchCRC(data, MEASURE_RUNS);

            long amfValue = amfHash(data);
            long crcValue = crc32(data);

            double amfThroughput = mibPerSecond(size, amfAvgNanos);
            double crcThroughput = mibPerSecond(size, crcAvgNanos);

            System.out.printf("%-10s %-18d %-18.2f %-12d %-18d %-18.2f %-12d%n",
                    humanSize(size),
                    amfAvgNanos,
                    amfThroughput,
                    amfValue,
                    crcAvgNanos,
                    crcThroughput,
                    crcValue);
        }

        System.out.println();
        System.out.println("Tip: run twice and compare results.");
        System.out.println("Disable CRC32 intrinsics with:");
        System.out.println("  java -XX:+UnlockDiagnosticVMOptions -XX:-UseCRC32Intrinsics AMFvsCRC32Benchmark");
    }
}
