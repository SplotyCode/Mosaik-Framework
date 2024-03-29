package io.github.splotycode.mosaik.networking.store;

public enum HashMethod {

    MODULO_STANDARD {
        @Override
        public int hash(int hashcode, int buckets) {
            return Math.abs(hashcode % buckets);
        }
    },
    JDK {
        @Override
        public int hash(int hashcode, int buckets) {
            return buckets & (hashcode ^ hashcode >>> 16);
        }
    },
    MODULO_JDK {
        @Override
        public int hash(int hashcode, int buckets) {
            hashcode ^= (hashcode >>> 20) ^ (hashcode >>> 12);
            return Math.abs((hashcode ^ (hashcode >>> 7) ^ (hashcode >>> 4)) % buckets);
        }
    };

    public abstract int hash(int hashcode, int buckets);

}
