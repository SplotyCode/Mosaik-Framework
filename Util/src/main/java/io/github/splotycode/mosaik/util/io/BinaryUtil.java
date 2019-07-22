package io.github.splotycode.mosaik.util.io;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BinaryUtil {

    public static long readLong(byte[] bytes) {
        return readLong(bytes, 0);
    }

    public static long readLong(byte[] bytes, int offset) {
        return ((long)bytes[offset] << 56) +
               ((long)(bytes[offset + 1] & 255) << 48) +
               ((long)(bytes[offset + 2] & 255) << 40) +
               ((long)(bytes[offset + 3] & 255) << 32) +
               ((long)(bytes[offset + 4] & 255) << 24) +
               ((bytes[offset + 5] & 255) << 16) +
               ((bytes[offset + 6] & 255) <<  8) +
               ((bytes[offset + 7] & 255));
    }

    public static byte[] writeLong(long value) {
        byte[] bytes = new byte[8];
        writeLong(bytes, value);
        return bytes;
    }

    public static void writeLong(byte[] bytes, long value) {
        writeLong(bytes, 0, value);
    }

    public static void writeLong(byte[] bytes, int offset, long value) {
        bytes[offset] = (byte)(value >>> 56);
        bytes[offset + 1] = (byte)(value >>> 48);
        bytes[offset + 2] = (byte)(value >>> 40);
        bytes[offset + 3] = (byte)(value >>> 32);
        bytes[offset + 4] = (byte)(value >>> 24);
        bytes[offset + 5] = (byte)(value >>> 16);
        bytes[offset + 6] = (byte)(value >>>  8);
        bytes[offset + 7] = (byte)(value);
    }

    public static int readInt(byte[] bytes) {
        return readInt(bytes, 0);
    }

    public static int readInt(byte[] bytes, int offset) {
        return ((bytes[offset] << 24) +
               (bytes[offset + 1] << 16) +
               (bytes[offset + 2] << 8) +
               (bytes[offset + 3]));
    }

    public static byte[] writeInt(int value) {
        byte[] bytes = new byte[4];
        writeInt(bytes, value);
        return bytes;
    }

    public static void writeInt(byte[] bytes, int value) {
        writeLong(bytes, 0, value);
    }

    public static void writeInt(byte[] bytes, int offset, int value) {
        bytes[offset] = (byte) ((value >>> 24) & 0xFF);
        bytes[offset] = (byte) ((value >>> 16) & 0xFF);
        bytes[offset] = (byte) ((value >>>  8) & 0xFF);
        bytes[offset] = (byte) (value & 0xFF);
    }

    public static float readFloat(byte[] bytes) {
        return readFloat(bytes, 0);
    }

    public static float readFloat(byte[] bytes, int offset) {
        return Float.intBitsToFloat(readInt(bytes, offset));
    }

    public static byte[] writeFloat(float value) {
        byte[] bytes = new byte[4];
        writeFloat(bytes, value);
        return bytes;
    }

    public static void writeFloat(byte[] bytes, float value) {
        writeFloat(bytes, 0, value);
    }

    public static void writeFloat(byte[] bytes, int offset, float value) {
        writeInt(bytes, offset, Float.floatToIntBits(value));
    }

    public static double readDouble(byte[] bytes) {
        return readDouble(bytes, 0);
    }

    public static double readDouble(byte[] bytes, int offset) {
        return Double.longBitsToDouble(readLong(bytes, offset));
    }

    public static byte[] writeDouble(double value) {
        byte[] bytes = new byte[8];
        writeDouble(bytes, value);
        return bytes;
    }

    public static void writeDouble(byte[] bytes, double value) {
        writeDouble(bytes, 0, value);
    }

    public static void writeDouble(byte[] bytes, int offset, double value) {
        writeLong(bytes, offset, Double.doubleToLongBits(value));
    }

    public static short readShort(byte[] bytes) {
        return readShort(bytes, 0);
    }

    public static short readShort(byte[] bytes, int offset) {
        return (short) ((bytes[offset] << 8) + (bytes[offset + 1]));
    }

    public static byte[] writeShort(short value) {
        byte[] bytes = new byte[2];
        writeShort(bytes, value);
        return bytes;
    }

    public static void writeShort(byte[] bytes, short value) {
        writeLong(bytes, 0, value);
    }

    public static void writeShort(byte[] bytes, int offset, short value) {
        bytes[offset] = (byte) ((value >>> 8) & 0xFF);
        bytes[offset + 1] = (byte) ((value) & 0xFF);
    }

    public static char readChar(byte[] bytes) {
        return readChar(bytes, 0);
    }

    public static char readChar(byte[] bytes, int offset) {
        return (char) ((bytes[offset] << 8) + (bytes[offset + 1]));
    }

    public static byte[] writeChar(char value) {
        byte[] bytes = new byte[2];
        writeChar(bytes, value);
        return bytes;
    }

    public static void writeChar(byte[] bytes, char value) {
        writeChar(bytes, 0, value);
    }

    public static void writeChar(byte[] bytes, int offset, char value) {
        bytes[offset] = (byte) ((value >>> 8) & 0xFF);
        bytes[offset + 1] = (byte) ((value) & 0xFF);
    }

}
