public final class AMFQuickHash {

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
}
