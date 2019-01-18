package me.david.davidlib.util.collection;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

//TODO add mergeArrayAndCollection for primarys and indexOfNot

@SuppressWarnings({"unused", "WeakerAccess"})
public final class ArrayUtil {

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];

    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];


    @SuppressWarnings("unchecked")
    public static <T> T[] createArray(Class<?> type, int length) {
        return (T[])Array.newInstance(type, length);
    }

    /*
     * --------------------------------
     * ------- FROM COLLECTION --------
     * --------------------------------
     */

    public static <T> T[] toArray(Collection<T> integers, Class<T> clazz) {
        return integers.toArray(createArray(clazz, integers.size()));
    }

    public static int[] toIntArray(Collection<Integer> integers) {
        return toPrimitive(integers.toArray(new Integer[integers.size()]));
    }

    public static short[] toShortArray(Collection<Short> shorts) {
        return toPrimitive(shorts.toArray(new Short[shorts.size()]));
    }

    public static long[] toLongArray(Collection<Long> longs) {
        return toPrimitive(longs.toArray(new Long[longs.size()]));
    }

    public static boolean[] toBooleanArray(Collection<Boolean> booleans) {
        return toPrimitive(booleans.toArray(new Boolean[booleans.size()]));
    }

    public static char[] toCharArray(Collection<Character> characters) {
        return toPrimitive(characters.toArray(new Character[characters.size()]));
    }

    public static byte[] toByteArray(Collection<Byte> bytes) {
        return toPrimitive(bytes.toArray(new Byte[bytes.size()]));
    }

    public static float[] toFloatArray(Collection<Float> floats) {
        return toPrimitive(floats.toArray(new Float[floats.size()]));
    }

    public static double[] toDoubleArray(Collection<Double> doubles) {
        return toPrimitive(doubles.toArray(new Double[doubles.size()]));
    }



    /*
     * --------------------------------
     * ----------- RESIZE -------------
     * --------------------------------
     */
    public static byte[] resize(byte[] array, final int newSize) {
        if (newSize == 0) return EMPTY_BYTE_ARRAY;
        return array.length == newSize ? array : Arrays.copyOf(array, newSize);
    }

    public static boolean[] resize(boolean[] array, final int newSize) {
        if (newSize == 0) return EMPTY_BOOLEAN_ARRAY;
        return array.length == newSize ? array : Arrays.copyOf(array, newSize);
    }

    public static short[] resize(short[] array, final int newSize) {
        if (newSize == 0) return EMPTY_SHORT_ARRAY;
        return array.length == newSize ? array : Arrays.copyOf(array, newSize);
    }

    public static float[] resize(float[] array, final int newSize) {
        if (newSize == 0) return EMPTY_FLOAT_ARRAY;
        return array.length == newSize ? array : Arrays.copyOf(array, newSize);
    }

    public static long[] resize(long[] array, int newSize) {
        if (newSize == 0) return EMPTY_LONG_ARRAY;
        return array.length == newSize ? array : Arrays.copyOf(array, newSize);
    }

    public static char[] resize(char[] array, int newSize) {
        if (newSize == 0) return EMPTY_CHAR_ARRAY;
        return array.length == newSize ? array : Arrays.copyOf(array, newSize);
    }

    public static double[] resize(double[] array, final int newSize) {
        if (newSize == 0) return EMPTY_DOUBLE_ARRAY;
        return array.length == newSize ? array : Arrays.copyOf(array, newSize);
    }

    public static int[] resize(int[] array, final int newSize) {
        if (newSize == 0) return EMPTY_INT_ARRAY;
        return array.length == newSize ? array : Arrays.copyOf(array, newSize);
    }

    public static <T> T[] resize(T[] array, final int newSize) {
        if (newSize == 0) return createArray(array.getClass().getComponentType(), newSize);
        return array.length == newSize ? array : Arrays.copyOf(array, newSize);
    }


    /*
     * --------------------------------
     * ------------ MERGE -------------
     * --------------------------------
     */

    public static <T> T[] merge(T[] a1, T[] a2) {
        if (a1.length == 0) return a2;
        if (a2.length == 0) return a1;
        final Class<?> class1 = a1.getClass().getComponentType();
        final Class<?> class2 = a2.getClass().getComponentType();
        final Class<?> aClass = class1.isAssignableFrom(class2) ? class1 : class2;

        T[] result = createArray(aClass, a1.length + a2.length);
        System.arraycopy(a1, 0, result, 0, a1.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        return result;
    }

    public static int[] merge(int[] a1, int[] a2) {
        if (a1.length == 0) return a2;
        if (a2.length == 0) return a1;
        int[] result = new int[a1.length + a2.length];
        System.arraycopy(a1, 0, result, 0, a1.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        return result;
    }

    public static float[] merge(float[] a1, float[] a2) {
        if (a1.length == 0) return a2;
        if (a2.length == 0) return a1;
        float[] result = new float[a1.length + a2.length];
        System.arraycopy(a1, 0, result, 0, a1.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        return result;
    }

    public static short[] merge(short[] a1, short[] a2) {
        if (a1.length == 0) return a2;
        if (a2.length == 0) return a1;
        short[] result = new short[a1.length + a2.length];
        System.arraycopy(a1, 0, result, 0, a1.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        return result;
    }

    public static double[] merge(double[] a1, double[] a2) {
        if (a1.length == 0) return a2;
        if (a2.length == 0) return a1;
        double[] result = new double[a1.length + a2.length];
        System.arraycopy(a1, 0, result, 0, a1.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        return result;
    }

    public static long[] merge(long[] a1, long[] a2) {
        if (a1.length == 0) return a2;
        if (a2.length == 0) return a1;
        long[] result = new long[a1.length + a2.length];
        System.arraycopy(a1, 0, result, 0, a1.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        return result;
    }

    public static boolean[] merge(boolean[] a1, boolean[] a2) {
        if (a1.length == 0) return a2;
        if (a2.length == 0) return a1;
        boolean[] result = new boolean[a1.length + a2.length];
        System.arraycopy(a1, 0, result, 0, a1.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        return result;
    }

    public static byte[] merge(byte[] a1, byte[] a2) {
        if (a1.length == 0) return a2;
        if (a2.length == 0) return a1;
        byte[] result = new byte[a1.length + a2.length];
        System.arraycopy(a1, 0, result, 0, a1.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        return result;
    }

    public static char[] merge(char[] a1, char[] a2) {
        if (a1.length == 0) return a2;
        if (a2.length == 0) return a1;
        char[] result = new char[a1.length + a2.length];
        System.arraycopy(a1, 0, result, 0, a1.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        return result;
    }

     /*
     * --------------------------------
     * --------- toPrimitive ----------
     * --------------------------------
     */

    public static int[] toPrimitive(final Integer[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_INT_ARRAY;
        }
        final int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public static short[] toPrimitive(final Short[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_SHORT_ARRAY;
        }
        final short[] result = new short[array.length];
        for (short i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public static long[] toPrimitive(final Long[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_LONG_ARRAY;
        }
        final long[] result = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public static boolean[] toPrimitive(final Boolean[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        final boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public static char[] toPrimitive(final Character[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_CHAR_ARRAY;
        }
        final char[] result = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public static byte[] toPrimitive(final Byte[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        final byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public static double[] toPrimitive(final Double[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_DOUBLE_ARRAY;
        }
        final double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public static float[] toPrimitive(final Float[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_FLOAT_ARRAY;
        }
        final float[] result = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

     /*
     * --------------------------------
     * ------------ APPEND ------------
     * --------------------------------
     */

    public static <T> T[] append(T[] src, final T element) {
        int length = src.length;
        T[] result = createArray(src.getClass().getComponentType(), length + 1);
        System.arraycopy(src, 0, result, 0, length);
        result[length] = element;
        return result;
    }

    public static long[] append(long[] array, long value) {
        array = resize(array, array.length + 1);
        array[array.length - 1] = value;
        return array;
    }

    public static double[] append(double[] array, double value) {
        array = resize(array, array.length + 1);
        array[array.length - 1] = value;
        return array;
    }

    public static short[] append(short[] array, short value) {
        array = resize(array, array.length + 1);
        array[array.length - 1] = value;
        return array;
    }

    public static float[] append(float[] array, float value) {
        array = resize(array, array.length + 1);
        array[array.length - 1] = value;
        return array;
    }

    public static int[] append(int[] array, int value) {
        array = resize(array, array.length + 1);
        array[array.length - 1] = value;
        return array;
    }

    public static char[] append(char[] array, char value) {
        array = resize(array, array.length + 1);
        array[array.length - 1] = value;
        return array;
    }

    public static byte[] append(byte[] array, byte value) {
        array = resize(array, array.length + 1);
        array[array.length - 1] = value;
        return array;
    }

    public static boolean[] append(boolean[] array, boolean value) {
        array = resize(array, array.length + 1);
        array[array.length - 1] = value;
        return array;
    }

    /*
     * --------------------------------
     * ------------ PREPEND -----------
     * --------------------------------
     */

    public static <T> T[] prepend(T[] array, T element) {
        int length = array.length;
        T[] result = createArray(array.getClass().getComponentType(), length + 1);
        System.arraycopy(array, 0, result, 1, length);
        result[0] = element;
        return result;
    }

    public static byte[] prepend(byte[] array, byte element) {
        int length = array.length;
        final byte[] result = new byte[length + 1];
        result[0] = element;
        System.arraycopy(array, 0, result, 1, length);
        return result;
    }

    public static char[] prepend(char[] array, char element) {
        int length = array.length;
        final char[] result = new char[length + 1];
        result[0] = element;
        System.arraycopy(array, 0, result, 1, length);
        return result;
    }

    public static boolean[] prepend(boolean[] array, boolean element) {
        int length = array.length;
        final boolean[] result = new boolean[length + 1];
        result[0] = element;
        System.arraycopy(array, 0, result, 1, length);
        return result;
    }

    public static int[] prepend(int[] array, int element) {
        int length = array.length;
        final int[] result = new int[length + 1];
        result[0] = element;
        System.arraycopy(array, 0, result, 1, length);
        return result;
    }

    public static short[] prepend(short[] array, short element) {
        int length = array.length;
        final short[] result = new short[length + 1];
        result[0] = element;
        System.arraycopy(array, 0, result, 1, length);
        return result;
    }

    public static float[] prepend(float[] array, float element) {
        int length = array.length;
        final float[] result = new float[length + 1];
        result[0] = element;
        System.arraycopy(array, 0, result, 1, length);
        return result;
    }

    public static long[] prepend(long[] array, long element) {
        int length = array.length;
        final long[] result = new long[length + 1];
        result[0] = element;
        System.arraycopy(array, 0, result, 1, length);
        return result;
    }

    public static double[] prepend(double[] array, double element) {
        int length = array.length;
        final double[] result = new double[length + 1];
        result[0] = element;
        System.arraycopy(array, 0, result, 1, length);
        return result;
    }

    /*
     * --------------------------------
     * ------------ REMOVE ------------
     * --------------------------------
     */

    public static <T> T[] removeIndex(final T[] src, int idx) {
        int length = src.length;
        if (idx < 0 || idx >= length) {
            throw new IllegalArgumentException("invalid index: " + idx);
        }
        T[] result = createArray(src.getClass().getComponentType(), length - 1);
        System.arraycopy(src, 0, result, 0, idx);
        System.arraycopy(src, idx + 1, result, idx, length - idx - 1);
        return result;
    }

    public static <T> T[] remove(final T[] src, T element) {
        final int idx = indexOf(src, element);
        if (idx == -1) return src;

        return removeIndex(src, idx);
    }

    public static int[] removeIndex(final int[] src, int idx) {
        int length = src.length;
        if (idx < 0 || idx >= length) {
            throw new IllegalArgumentException("invalid index: " + idx);
        }
        int[] result = newIntArray(src.length - 1);
        System.arraycopy(src, 0, result, 0, idx);
        System.arraycopy(src, idx + 1, result, idx, length - idx - 1);
        return result;
    }

    public static int[] remove(final int[] src, int element) {
        final int idx = indexOf(src, element);
        if (idx == -1) return src;

        return removeIndex(src, idx);
    }

    public static short[] removeIndex(final short[] src, int idx) {
        int length = src.length;
        if (idx < 0 || idx >= length) {
            throw new IllegalArgumentException("invalid index: " + idx);
        }
        short[] result = newShortArray(src.length - 1);
        System.arraycopy(src, 0, result, 0, idx);
        System.arraycopy(src, idx + 1, result, idx, length - idx - 1);
        return result;
    }

    public static short[] remove(final short[] src, short element) {
        final int idx = indexOf(src, element);
        if (idx == -1) return src;

        return removeIndex(src, idx);
    }

    public static long[] removeIndex(final long[] src, int idx) {
        int length = src.length;
        if (idx < 0 || idx >= length) {
            throw new IllegalArgumentException("invalid index: " + idx);
        }
        long[] result = newLongArray(src.length - 1);
        System.arraycopy(src, 0, result, 0, idx);
        System.arraycopy(src, idx + 1, result, idx, length - idx - 1);
        return result;
    }

    public static long[] remove(final long[] src, long element) {
        final int idx = indexOf(src, element);
        if (idx == -1) return src;

        return removeIndex(src, idx);
    }

    public static double[] removeIndex(final double[] src, int idx) {
        int length = src.length;
        if (idx < 0 || idx >= length) {
            throw new IllegalArgumentException("invalid index: " + idx);
        }
        double[] result = newDoubleArray(src.length - 1);
        System.arraycopy(src, 0, result, 0, idx);
        System.arraycopy(src, idx + 1, result, idx, length - idx - 1);
        return result;
    }

    public static double[] remove(final double[] src, double element) {
        final int idx = indexOf(src, element);
        if (idx == -1) return src;

        return removeIndex(src, idx);
    }

    public static float[] removeIndex(final float[] src, int idx) {
        int length = src.length;
        if (idx < 0 || idx >= length) {
            throw new IllegalArgumentException("invalid index: " + idx);
        }
        float[] result = newFloatArray(src.length - 1);
        System.arraycopy(src, 0, result, 0, idx);
        System.arraycopy(src, idx + 1, result, idx, length - idx - 1);
        return result;
    }

    public static float[] remove(final float[] src, float element) {
        final int idx = indexOf(src, element);
        if (idx == -1) return src;

        return removeIndex(src, idx);
    }

    public static char[] removeIndex(final char[] src, int idx) {
        int length = src.length;
        if (idx < 0 || idx >= length) {
            throw new IllegalArgumentException("invalid index: " + idx);
        }
        char[] result = newCharArray(src.length - 1);
        System.arraycopy(src, 0, result, 0, idx);
        System.arraycopy(src, idx + 1, result, idx, length - idx - 1);
        return result;
    }

    public static char[] remove(final char[] src, char element) {
        final int idx = indexOf(src, element);
        if (idx == -1) return src;

        return removeIndex(src, idx);
    }

    public static byte[] removeIndex(final byte[] src, int idx) {
        int length = src.length;
        if (idx < 0 || idx >= length) {
            throw new IllegalArgumentException("invalid index: " + idx);
        }
        byte[] result = newByteArray(src.length - 1);
        System.arraycopy(src, 0, result, 0, idx);
        System.arraycopy(src, idx + 1, result, idx, length - idx - 1);
        return result;
    }

    public static byte[] remove(final byte[] src, byte element) {
        final int idx = indexOf(src, element);
        if (idx == -1) return src;

        return removeIndex(src, idx);
    }

    public static boolean[] removeIndex(final boolean[] src, int idx) {
        int length = src.length;
        if (idx < 0 || idx >= length) {
            throw new IllegalArgumentException("invalid index: " + idx);
        }
        boolean[] result = newBooleanArray(src.length - 1);
        System.arraycopy(src, 0, result, 0, idx);
        System.arraycopy(src, idx + 1, result, idx, length - idx - 1);
        return result;
    }

    public static boolean[] remove(final boolean[] src, boolean element) {
        final int idx = indexOf(src, element);
        if (idx == -1) return src;

        return removeIndex(src, idx);
    }

    /*
     * --------------------------------
     * ----------- REVERSE ------------
     * --------------------------------
     */

    public static <T> T[] reverse(T[] array) {
        T[] newArray = array.clone();
        for (int i = 0; i < array.length; i++) {
            newArray[array.length - i - 1] = array[i];
        }
        return newArray;
    }

    public static int[] reverse(int[] array) {
        int[] newArray = array.clone();
        for (int i = 0; i < array.length; i++) {
            newArray[array.length - i - 1] = array[i];
        }
        return newArray;
    }

    public static long[] reverse(long[] array) {
        long[] newArray = array.clone();
        for (int i = 0; i < array.length; i++) {
            newArray[array.length - i - 1] = array[i];
        }
        return newArray;
    }

    public static short[] reverse(short[] array) {
        short[] newArray = array.clone();
        for (int i = 0; i < array.length; i++) {
            newArray[array.length - i - 1] = array[i];
        }
        return newArray;
    }

    public static double[] reverse(double[] array) {
        double[] newArray = array.clone();
        for (int i = 0; i < array.length; i++) {
            newArray[array.length - i - 1] = array[i];
        }
        return newArray;
    }

    public static float[] reverse(float[] array) {
        float[] newArray = array.clone();
        for (int i = 0; i < array.length; i++) {
            newArray[array.length - i - 1] = array[i];
        }
        return newArray;
    }

    public static char[] reverse(char[] array) {
        char[] newArray = array.clone();
        for (int i = 0; i < array.length; i++) {
            newArray[array.length - i - 1] = array[i];
        }
        return newArray;
    }

    public static byte[] reverse(byte[] array) {
        byte[] newArray = array.clone();
        for (int i = 0; i < array.length; i++) {
            newArray[array.length - i - 1] = array[i];
        }
        return newArray;
    }

    public static boolean[] reverse(boolean[] array) {
        boolean[] newArray = array.clone();
        for (int i = 0; i < array.length; i++) {
            newArray[array.length - i - 1] = array[i];
        }
        return newArray;
    }

    /*
     * --------------------------------
     * ------------ SWAP --------------
     * --------------------------------
     */

    public static <T> void swap(T[] array, int i1, int i2) {
        final T t = array[i1];
        array[i1] = array[i2];
        array[i2] = t;
    }

    public static void swap(int[] array, int i1, int i2) {
        final int t = array[i1];
        array[i1] = array[i2];
        array[i2] = t;
    }

    public static void swap(boolean[] array, int i1, int i2) {
        final boolean t = array[i1];
        array[i1] = array[i2];
        array[i2] = t;
    }

    public static void swap(char[] array, int i1, int i2) {
        final char t = array[i1];
        array[i1] = array[i2];
        array[i2] = t;
    }

    public static void swap(byte[] array, int i1, int i2) {
        final byte t = array[i1];
        array[i1] = array[i2];
        array[i2] = t;
    }

    public static void swap(long[] array, int i1, int i2) {
        final long t = array[i1];
        array[i1] = array[i2];
        array[i2] = t;
    }

    public static void swap(short[] array, int i1, int i2) {
        final short t = array[i1];
        array[i1] = array[i2];
        array[i2] = t;
    }

    public static void swap(float[] array, int i1, int i2) {
        final float t = array[i1];
        array[i1] = array[i2];
        array[i2] = t;
    }

    public static void swap(double[] array, int i1, int i2) {
        final double t = array[i1];
        array[i1] = array[i2];
        array[i2] = t;
    }

    /*
     * --------------------------------
     * ------------ INDEXOF -----------
     * --------------------------------
     */

    public static <T> int indexOf(final T[] src, final T obj) {
        for (int i = 0; i < src.length; i++) {
            final T o = src[i];
            if (o == null) {
                if (obj == null) {
                    return i;
                }
            } else {
                if (o.equals(obj)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static int indexOf(long[] longs, long value) {
        for (int i = 0; i < longs.length; i++) {
            if (longs[i] == value) return i;
        }
        return -1;
    }

    public static int indexOf(int[] ints, int value) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == value) return i;
        }
        return -1;
    }

    public static int indexOf(short[] ints, short value) {
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == value) return i;
        }
        return -1;
    }

    public static int indexOf(double[] doubles, double value) {
        for (int i = 0; i < doubles.length; i++) {
            if (doubles[i] == value) return i;
        }
        return -1;
    }

    public static int indexOf(float[] floats, float value) {
        for (int i = 0; i < floats.length; i++) {
            if (floats[i] == value) return i;
        }
        return -1;
    }

    public static int indexOf(char[] chars, char value) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == value) return i;
        }
        return -1;
    }

    public static int indexOf(byte[] bytes, byte value) {
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == value) return i;
        }
        return -1;
    }

    public static int indexOf(boolean[] booleans, boolean value) {
        for (int i = 0; i < booleans.length; i++) {
            if (booleans[i] == value) return i;
        }
        return -1;
    }


    public static <T> int lastIndexOf(final T[] src, final T obj) {
        for (int i = src.length - 1; i >= 0; i--) {
            final T o = src[i];
            if (o == null) {
                if (obj == null) {
                    return i;
                }
            } else {
                if (o.equals(obj)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static <T> int lastIndexOfNot(final T[] src, final T obj) {
        for (int i = src.length - 1; i >= 0; i--) {
            final T o = src[i];
            if (o == null) {
                if (obj != null) {
                    return i;
                }
            } else {
                if (!o.equals(obj)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static int lastIndexOf(final int[] src, final int obj) {
        for (int i = src.length - 1; i >= 0; i--) {
            final int o = src[i];
            if (o == obj) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOfNot(final int[] src, final int obj) {
        for (int i = src.length - 1; i >= 0; i--) {
            final int o = src[i];
            if (o != obj) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(final long[] src, final long obj) {
        for (int i = src.length - 1; i >= 0; i--) {
            final long o = src[i];
            if (o == obj) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOfNot(final long[] src, final long obj) {
        for (int i = src.length - 1; i >= 0; i--) {
            final long o = src[i];
            if (o != obj) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(final short[] src, final short obj) {
        for (int i = src.length - 1; i >= 0; i--) {
            final short o = src[i];
            if (o == obj) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOfNot(final short[] src, final short obj) {
        for (int i = src.length - 1; i >= 0; i--) {
            final short o = src[i];
            if (o != obj) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(final float[] src, final float obj) {
        for (int i = src.length - 1; i >= 0; i--) {
            final float o = src[i];
            if (o == obj) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOfNot(final float[] src, final float obj) {
        for (int i = src.length - 1; i >= 0; i--) {
            final float o = src[i];
            if (o != obj) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(final double[] src, final double obj) {
        for (int i = src.length - 1; i >= 0; i--) {
            final double o = src[i];
            if (o == obj) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOfNot(final double[] src, final double obj) {
        for (int i = src.length - 1; i >= 0; i--) {
            final double o = src[i];
            if (o != obj) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(final boolean[] src, final boolean obj) {
        for (int i = src.length - 1; i >= 0; i--) {
            final boolean o = src[i];
            if (o == obj) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOfNot(final boolean[] src, final boolean obj) {
        for (int i = src.length - 1; i >= 0; i--) {
            final boolean o = src[i];
            if (o != obj) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(final char[] src, final char obj) {
        for (int i = src.length - 1; i >= 0; i--) {
            final char o = src[i];
            if (o == obj) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOfNot(final char[] src, final char obj) {
        for (int i = src.length - 1; i >= 0; i--) {
            final char o = src[i];
            if (o != obj) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(final byte[] src, final byte obj) {
        for (int i = src.length - 1; i >= 0; i--) {
            final byte o = src[i];
            if (o == obj) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOfNot(final byte[] src, final byte obj) {
        for (int i = src.length - 1; i >= 0; i--) {
            final byte o = src[i];
            if (o != obj) {
                return i;
            }
        }
        return -1;
    }

    /*
     * --------------------------------
     * ----------- CONTAINS -----------
     * --------------------------------
     */

    public static <T> boolean contains(T[] objects, final T o) {
        return indexOf(objects, o) >= 0;
    }

    public static boolean contains(boolean[] entries, final boolean entry) {
        return indexOf(entries, entry) >= 0;
    }

    public static boolean contains(char[] entries, final char entry) {
        return indexOf(entries, entry) >= 0;
    }

    public static boolean contains(byte[] entries, final byte entry) {
        return indexOf(entries, entry) >= 0;
    }

    public static boolean contains(int[] entries, final int entry) {
        return indexOf(entries, entry) >= 0;
    }

    public static boolean contains(short[] entries, final short entry) {
        return indexOf(entries, entry) >= 0;
    }

    public static boolean contains(long[] entries, final long entry) {
        return indexOf(entries, entry) >= 0;
    }

    public static boolean contains(float[] entries, final float entry) {
        return indexOf(entries, entry) >= 0;
    }

    public static boolean contains(double[] entries, final double entry) {
        return indexOf(entries, entry) >= 0;
    }

    /*
     * --------------------------------
     * ----------- NEWARRAY -----------
     * --------------------------------
     */


    public static int[] newIntArray(int count) {
        return count == 0 ? EMPTY_INT_ARRAY : new int[count];
    }

    public static long[] newLongArray(int count) {
        return count == 0 ? EMPTY_LONG_ARRAY : new long[count];
    }

    public static short[] newShortArray(int count) {
        return count == 0 ? EMPTY_SHORT_ARRAY : new short[count];
    }

    public static float[] newFloatArray(int count) {
        return count == 0 ? EMPTY_FLOAT_ARRAY : new float[count];
    }

    public static double[] newDoubleArray(int count) {
        return count == 0 ? EMPTY_DOUBLE_ARRAY : new double[count];
    }

    public static char[] newCharArray(int count) {
        return count == 0 ? EMPTY_CHAR_ARRAY : new char[count];
    }

    public static boolean[] newBooleanArray(int count) {
        return count == 0 ? EMPTY_BOOLEAN_ARRAY : new boolean[count];
    }

    public static byte[] newByteArray(int count) {
        return count == 0 ? EMPTY_BYTE_ARRAY : new byte[count];
    }

    /*
     * --------------------------------
     * ------------ FIRST -------------
     * --------------------------------
     */

    public static <T> T first(T[] array) {
        return !isEmpty(array) ? array[0] : null;
    }

    public static int first(int[] array) {
        return !isEmpty(array) ? array[0] : 0;
    }

    public static int first(short[] array) {
        return !isEmpty(array) ? array[0] : 0;
    }

    public static long first(long[] array) {
        return !isEmpty(array) ? array[0] : 0;
    }

    public static double first(double[] array) {
        return !isEmpty(array) ? array[0] : 0;
    }

    public static float first(float[] array) {
        return !isEmpty(array) ? array[0] : 0;
    }

    public static char first(char[] array) {
        return !isEmpty(array) ? array[0] : 0;
    }

    public static byte first(byte[] array) {
        return !isEmpty(array) ? array[0] : 0;
    }

    public static boolean first(boolean[] array) {
        return isEmpty(array) && array[0];
    }

    /*
     * --------------------------------
     * ------------ LAST --------------
     * --------------------------------
     */

    public static <T> T last(T[] array) {
        return !isEmpty(array) ? array[array.length - 1] : null;
    }

    public static char last(char[] array) {
        return !isEmpty(array) ? array[array.length - 1] : Character.MIN_VALUE;
    }

    public static boolean last(boolean[] array) {
        return !isEmpty(array) && array[array.length - 1];
    }

    public static byte last(byte[] array) {
        return !isEmpty(array) ? array[array.length - 1] : 0;
    }

    public static short last(short[] array) {
        return !isEmpty(array) ? array[array.length - 1] : 0;
    }

    public static long last(long[] array) {
        return !isEmpty(array) ? array[array.length - 1] : 0;
    }

    public static int last(int[] array) {
        return !isEmpty(array) ? array[array.length - 1] : 0;
    }

    public static float last(float[] array) {
        return !isEmpty(array) ? array[array.length - 1] : 0;
    }

    public static double last(double[] array) {
        return !isEmpty(array) ? array[array.length - 1] : 0;
    }

    public static <T> void setLast(T[] array, T obj) {
        if (array.length == 0 )
            throw new ArrayIndexOutOfBoundsException("Can not set last object in a array  0 length array");
        array[array.length - 1] = obj;
    }

    public static void setLast(int[] array, int obj) {
        if (array.length == 0 )
            throw new ArrayIndexOutOfBoundsException("Can not set last object in a array  0 length array");
        array[array.length - 1] = obj;
    }

    public static void setLast(short[] array, short obj) {
        if (array.length == 0 )
            throw new ArrayIndexOutOfBoundsException("Can not set last object in a array  0 length array");
        array[array.length - 1] = obj;
    }

    public static void setLast(long[] array, long obj) {
        if (array.length == 0 )
            throw new ArrayIndexOutOfBoundsException("Can not set last object in a array  0 length array");
        array[array.length - 1] = obj;
    }

    public static void setLast(float[] array, float obj) {
        if (array.length == 0 )
            throw new ArrayIndexOutOfBoundsException("Can not set last object in a array  0 length array");
        array[array.length - 1] = obj;
    }

    public static void setLast(double[] array, double obj) {
        if (array.length == 0 )
            throw new ArrayIndexOutOfBoundsException("Can not set last object in a array  0 length array");
        array[array.length - 1] = obj;
    }

    public static void setLast(char[] array, char obj) {
        if (array.length == 0 )
            throw new ArrayIndexOutOfBoundsException("Can not set last object in a array  0 length array");
        array[array.length - 1] = obj;
    }

    public static void setLast(boolean[] array, boolean obj) {
        if (array.length == 0 )
            throw new ArrayIndexOutOfBoundsException("Can not set last object in a array  0 length array");
        array[array.length - 1] = obj;
    }

    public static void setLast(byte[] array, byte obj) {
        if (array.length == 0 )
            throw new ArrayIndexOutOfBoundsException("Can not set last object in a array  0 length array");
        array[array.length - 1] = obj;
    }

    /*
     * --------------------------------
     * ------------ COPY --------------
     * --------------------------------
     */

    public static <T> void copy(final Collection<? extends T> src, final T[] dst) {
        int i = 0;
        for (T t : src) {
            dst[i++] = t;
        }
    }

    public static void copy(final Collection<Integer> src, final int[] dst) {
        int i = 0;
        for (int t : src) {
            dst[i++] = t;
        }
    }

    public static void copy(final Collection<Short> src, final short[] dst) {
        int i = 0;
        for (short t : src) {
            dst[i++] = t;
        }
    }

    public static void copy(final Collection<Long> src, final long[] dst) {
        int i = 0;
        for (long t : src) {
            dst[i++] = t;
        }
    }

    public static void copy(final Collection<Float> src, final float[] dst) {
        int i = 0;
        for (float t : src) {
            dst[i++] = t;
        }
    }

    public static void copy(final Collection<Double> src, final double[] dst) {
        int i = 0;
        for (double t : src) {
            dst[i++] = t;
        }
    }

    public static void copy(final Collection<Character> src, final char[] dst) {
        int i = 0;
        for (char t : src) {
            dst[i++] = t;
        }
    }

    public static void copy(final Collection<Byte> src, final byte[] dst) {
        int i = 0;
        for (byte t : src) {
            dst[i++] = t;
        }
    }

    public static void copy(final Collection<Boolean> src, final boolean[] dst) {
        int i = 0;
        for (boolean t : src) {
            dst[i++] = t;
        }
    }

    public static <T> T[] copy(T[] original) {
        if (original == null) return null;
        return Arrays.copyOf(original, original.length);
    }

    public static boolean[] copy(boolean[] original) {
        if (original == null) return null;
        return original.length == 0 ? EMPTY_BOOLEAN_ARRAY : Arrays.copyOf(original, original.length);
    }

    public static char[] copy(char[] original) {
        if (original == null) return null;
        return original.length == 0 ? EMPTY_CHAR_ARRAY : Arrays.copyOf(original, original.length);
    }

    public static byte[] copy(byte[] original) {
        if (original == null) return null;
        return original.length == 0 ? EMPTY_BYTE_ARRAY : Arrays.copyOf(original, original.length);
    }

    public static int[] copy(int[] original) {
        if (original == null) return null;
        return original.length == 0 ? EMPTY_INT_ARRAY : Arrays.copyOf(original, original.length);
    }

    public static long[] copy(long[] original) {
        if (original == null) return null;
        return original.length == 0 ? EMPTY_LONG_ARRAY : Arrays.copyOf(original, original.length);
    }

    public static short[] copy(short[] original) {
        if (original == null) return null;
        return original.length == 0 ? EMPTY_SHORT_ARRAY : Arrays.copyOf(original, original.length);
    }

    public static double[] copy(double[] original) {
        if (original == null) return null;
        return original.length == 0 ? EMPTY_DOUBLE_ARRAY : Arrays.copyOf(original, original.length);
    }

    public static float[] copy(float[] original) {
        if (original == null) return null;
        return original.length == 0 ? EMPTY_FLOAT_ARRAY : Arrays.copyOf(original, original.length);
    }

     /*
     * --------------------------------
     * ------------ STRIP -------------
     * --------------------------------
     */

    public static <T> T[] stripNulls(T[] array) {
        return array.length != 0 && array[array.length-1] == null ? Arrays.copyOf(array, nullsIndex(array)) : array;
    }

    private static <T> int nullsIndex(T[] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] != null) {
                return i + 1;
            }
        }
        return 0;
    }

     /*
     * --------------------------------
     * ------------ EMPTY -------------
     * --------------------------------
     */

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(short[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(double[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(float[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(char[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(boolean[] array) {
        return array == null || array.length == 0;
    }

     /*
     * --------------------------------
     * ------------- MIN --------------
     * --------------------------------
     */

    public static int min(int[] values) {
        int min = Integer.MAX_VALUE;
        for (int value : values) {
            if (value < min) min = value;
        }
        return min;
    }

    public static short min(short[] values) {
        short min = Short.MAX_VALUE;
        for (short value : values) {
            if (value < min) min = value;
        }
        return min;
    }

    public static long min(long[] values) {
        long min = Long.MAX_VALUE;
        for (long value : values) {
            if (value < min) min = value;
        }
        return min;
    }

    public static float min(float[] values) {
        float min = Float.MAX_VALUE;
        for (float value : values) {
            if (value < min) min = value;
        }
        return min;
    }

    public static double min(double[] values) {
        double min = Double.MAX_VALUE;
        for (double value : values) {
            if (value < min) min = value;
        }
        return min;
    }

    public static byte min(byte[] values) {
        byte min = Byte.MAX_VALUE;
        for (byte value : values) {
            if (value < min) min = value;
        }
        return min;
    }

    public static boolean min(boolean[] values) {
        for (boolean value : values) {
            if (!value) return false;
        }
        return true;
    }

    public static char min(char[] values) {
        char min = Character.MAX_VALUE;
        for (char value : values) {
            if (value < min) min = value;
        }
        return min;
    }

     /*
     * --------------------------------
     * ------------- MAX --------------
     * --------------------------------
     */

    public static int max(int[] values) {
        int max = Integer.MIN_VALUE;
        for (int value : values) {
            if (value > max) max = value;
        }
        return max;
    }

    public static short max(short[] values) {
        short max = Short.MIN_VALUE;
        for (short value : values) {
            if (value > max) max = value;
        }
        return max;
    }

    public static long max(long[] values) {
        long max = Long.MIN_VALUE;
        for (long value : values) {
            if (value > max) max = value;
        }
        return max;
    }

    public static float max(float[] values) {
        float max = Float.MIN_VALUE;
        for (float value : values) {
            if (value > max) max = value;
        }
        return max;
    }

    public static double max(double[] values) {
        double max = Double.MIN_VALUE;
        for (double value : values) {
            if (value > max) max = value;
        }
        return max;
    }

    public static byte max(byte[] values) {
        byte max = Byte.MIN_VALUE;
        for (byte value : values) {
            if (value > max) max = value;
        }
        return max;
    }

    public static boolean max(boolean[] values) {
        for (boolean value : values) {
            if (value) return true;
        }
        return false;
    }

    public static char max(char[] values) {
        char max = Character.MIN_VALUE;
        for (char value : values) {
            if (value > max) max = value;
        }
        return max;
    }

    /*
     * --------------------------------
     * ----------- Merge --------------
     * --------------------------------
     */

    public static <T> T[] mergeArrayAndCollection(T[] array, Collection<T> collection) {
        if (collection.isEmpty()) {
            return array;
        }

        final T[] array2 = collection.toArray(createArray(array.getClass().getComponentType(), collection.size()));

        if (array.length == 0) {
            return array2;
        }

        final T[] result = createArray(array.getClass().getComponentType(), collection.size() + array.length);
        System.arraycopy(array, 0, result, 0, array.length);
        System.arraycopy(array2, 0, result, array.length, array2.length);
        return result;
    }

    public static int[] mergeArrayAndCollection(int[] array, Collection<Integer> collection) {
        if (collection.isEmpty()) {
            return array;
        }

        final int[] array2 = toIntArray(collection);

        if (array.length == 0) {
            return array2;
        }

        final int[] result = new int[collection.size() + array.length];
        System.arraycopy(array, 0, result, 0, array.length);
        System.arraycopy(array2, 0, result, array.length, array2.length);
        return result;
    }

    public static short[] mergeArrayAndCollection(short[] array, Collection<Short> collection) {
        if (collection.isEmpty()) {
            return array;
        }

        final short[] array2 = toShortArray(collection);

        if (array.length == 0) {
            return array2;
        }

        final short[] result = new short[collection.size() + array.length];
        System.arraycopy(array, 0, result, 0, array.length);
        System.arraycopy(array2, 0, result, array.length, array2.length);
        return result;
    }

    public static long[] mergeArrayAndCollection(long[] array, Collection<Long> collection) {
        if (collection.isEmpty()) {
            return array;
        }

        final long[] array2 = toLongArray(collection);

        if (array.length == 0) {
            return array2;
        }

        final long[] result = new long[collection.size() + array.length];
        System.arraycopy(array, 0, result, 0, array.length);
        System.arraycopy(array2, 0, result, array.length, array2.length);
        return result;
    }

    public static boolean[] mergeArrayAndCollection(boolean[] array, Collection<Boolean> collection) {
        if (collection.isEmpty()) {
            return array;
        }

        final boolean[] array2 = toBooleanArray(collection);

        if (array.length == 0) {
            return array2;
        }

        final boolean[] result = new boolean[collection.size() + array.length];
        System.arraycopy(array, 0, result, 0, array.length);
        System.arraycopy(array2, 0, result, array.length, array2.length);
        return result;
    }

    public static byte[] mergeArrayAndCollection(byte[] array, Collection<Byte> collection) {
        if (collection.isEmpty()) {
            return array;
        }

        final byte[] array2 = toByteArray(collection);

        if (array.length == 0) {
            return array2;
        }

        final byte[] result = new byte[collection.size() + array.length];
        System.arraycopy(array, 0, result, 0, array.length);
        System.arraycopy(array2, 0, result, array.length, array2.length);
        return result;
    }

    public static char[] mergeArrayAndCollection(char[] array, Collection<Character> collection) {
        if (collection.isEmpty()) {
            return array;
        }

        final char[] array2 = toCharArray(collection);

        if (array.length == 0) {
            return array2;
        }

        final char[] result = new char[collection.size() + array.length];
        System.arraycopy(array, 0, result, 0, array.length);
        System.arraycopy(array2, 0, result, array.length, array2.length);
        return result;
    }

    public static float[] mergeArrayAndCollection(float[] array, Collection<Float> collection) {
        if (collection.isEmpty()) {
            return array;
        }

        final float[] array2 = toFloatArray(collection);

        if (array.length == 0) {
            return array2;
        }

        final float[] result = new float[collection.size() + array.length];
        System.arraycopy(array, 0, result, 0, array.length);
        System.arraycopy(array2, 0, result, array.length, array2.length);
        return result;
    }

    public static double[] mergeArrayAndCollection(double[] array, Collection<Double> collection) {
        if (collection.isEmpty()) {
            return array;
        }

        final double[] array2 = toDoubleArray(collection);

        if (array.length == 0) {
            return array2;
        }

        final double[] result = new double[collection.size() + array.length];
        System.arraycopy(array, 0, result, 0, array.length);
        System.arraycopy(array2, 0, result, array.length, array2.length);
        return result;
    }

    /*
     * --------------------------------
     * -------- CONTAINS ANY ----------
     * --------------------------------
     */

    public static <T> boolean containsAny(T[] base, T[] check) {
        for (T item : base)
            if (contains(check, item))
                return true;
        return false;
    }

    public static boolean containsAny(int[] base, int[] check) {
        for (int item : base)
            if (contains(check, item))
                return true;
        return false;
    }

    public static boolean containsAny(short[] base, short[] check) {
        for (short item : base)
            if (contains(check, item))
                return true;
        return false;
    }

    public static boolean containsAny(long[] base, long[] check) {
        for (long item : base)
            if (contains(check, item))
                return true;
        return false;
    }

    public static boolean containsAny(boolean[] base, boolean[] check) {
        for (boolean item : base)
            if (contains(check, item))
                return true;
        return false;
    }

    public static boolean containsAny(char[] base, char[] check) {
        for (char item : base)
            if (contains(check, item))
                return true;
        return false;
    }

    public static boolean containsAny(float[] base, float[] check) {
        for (float item : base)
            if (contains(check, item))
                return true;
        return false;
    }

    public static boolean containsAny(double[] base, double[] check) {
        for (double item : base)
            if (contains(check, item))
                return true;
        return false;
    }

    public static boolean containsAny(byte[] base, byte[] check) {
        for (byte item : base)
            if (contains(check, item))
                return true;
        return false;
    }

    

}
