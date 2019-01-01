package me.david.davidlib.utils.array;

public final class ArrayUtil {

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];

    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    public static <T> int find(final T[] src, final T obj) {
        for (int i = 0; i < src.length; i++) {
            final T o = src[i];
            if (o == null) {
                if (obj == null) {
                    return i;
                }
            }
            else {
                if (o.equals(obj)) {
                    return i;
                }
            }
        }
        return -1;
    }

}
