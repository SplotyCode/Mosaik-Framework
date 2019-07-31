package io.github.splotycode.mosaik.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CodecUtil {

    public static final MessageDigest MD_5, SHA_1, SHA_256, SHA_512;

    static {
        MD_5 = getDigest("MD5");
        SHA_1 = getDigest("SHA-1");
        SHA_256 = getDigest("SHA-256");
        SHA_512 = getDigest("SHA-512");
    }

    /**
     * Gets a MessageDigest by Name
     * @param algorithm the algorithm that the the digest should have
     * @return the MessageDigest
     */
    public static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException var2) {
            throw new IllegalArgumentException(var2);
        }
    }

    /**
     * Decodes a InputStream to a byte array
     * @param digest the digest that should be used when decoding
     * @param data the InputStream that should be decoded
     * @return the decoded byte array
     */
    public static byte[] toBytes(MessageDigest digest, InputStream data) throws IOException {
        byte[] buffer = new byte[1024];

        for(int read = data.read(buffer, 0, 1024); read > -1; read = data.read(buffer, 0, 1024)) {
            digest.update(buffer, 0, read);
        }

        return digest.digest();
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * Converts a byte array to a hex encoded String
     * @param bytes the byte array that should be encoded
     * @return the hex encoded string
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] decodeHex(String encoded) {
        byte[] out = new byte[encoded.length() >> 1];
        for (int i = 0, j = 0; j < encoded.length(); i++, j++) {
            out[i] = (byte) (((Character.digit(encoded.charAt(j), 16) << 4) |
                               Character.digit(encoded.charAt(++j), 16))
                    & 0xFF);
        }
        return out;
    }

    /*
     * -------
     * M D 5
     * --------
     */

    /**
     * Converts to md5
     * @param data the data that should encoded
     */
    public static byte[] md5(byte[] data) {
        return MD_5.digest(data);
    }

    /**
     * Converts to md5
     * @param data the input stream that should encoded
     */
    public static byte[] md5(InputStream data) throws IOException {
        return toBytes(MD_5, data);
    }

    /**
     * Converts to md5
     * @param data the data that should encoded
     */
    public static byte[] md5(String data) {
        return md5(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Converts to md5 and then hex it
     * @param data the data that should encoded
     */
    public static String md5Hex(byte[] data) {
        return bytesToHex(md5(data));
    }

    /**
     * Converts to md5 and then hex it
     * @param data the data that should encoded
     */
    public static String md5Hex(InputStream data) throws IOException {
        return bytesToHex(md5(data));
    }

    /**
     * Converts to md5 and then hex it
     * @param data the data that should encoded
     */
    public static String md5Hex(String data) {
        return bytesToHex(md5(data));
    }

    /*
     * -------
     * SHA 1
     * --------
     */

    /**
     * Converts to sha1
     * @param data the data that should encoded
     */
    public static byte[] sha1(byte[] data) {
        return SHA_1.digest(data);
    }

    /**
     * Converts to sha1
     * @param data the data that should encoded
     */
    public static byte[] sha1(InputStream data) throws IOException {
        return toBytes(SHA_1, data);
    }

    /**
     * Converts to sha1
     * @param data the data that should encoded
     */
    public static byte[] sha1(String data) {
        return sha1(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Converts to sha1 and then hex it
     * @param data the data that should encoded
     */
    public static String sha1Hex(byte[] data) {
        return bytesToHex(sha1(data));
    }

    /**
     * Converts to sha1 and then hex it
     * @param data the data that should encoded
     */
    public static String sha1Hex(InputStream data) throws IOException {
        return bytesToHex(sha1(data));
    }

    /**
     * Converts to sha1 and then hex it
     * @param data the data that should encoded
     */
    public static String sha1Hex(String data) {
        return bytesToHex(sha1(data));
    }

    /*
     * -------
     * SHA 256
     * --------
     */

    /**
     * Converts to sha256
     * @param data the data that should encoded
     */
    public static byte[] sha256(byte[] data) {
        return SHA_256.digest(data);
    }

    /**
     * Converts to sha256
     * @param data the data that should encoded
     */
    public static byte[] sha256(InputStream data) throws IOException {
        return toBytes(SHA_256, data);
    }

    /**
     * Converts to sha256
     * @param data the data that should encoded
     */
    public static byte[] sha256(String data) {
        return sha256(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Converts to sha256 and then hex it
     * @param data the data that should encoded
     */
    public static String sha256Hex(byte[] data) {
        return bytesToHex(sha256(data));
    }

    /**
     * Converts to sha256 and then hex it
     * @param data the data that should encoded
     */
    public static String sha256Hex(InputStream data) throws IOException {
        return bytesToHex(sha256(data));
    }

    /**
     * Converts to sha256 and then hex it
     * @param data the data that should encoded
     */
    public static String sha256Hex(String data) {
        return bytesToHex(sha256(data));
    }

    /*
     * -------
     * SHA 512
     * --------
     */

    /**
     * Converts to sha512
     * @param data the data that should encoded
     */
    public static byte[] sha512(byte[] data) {
        return SHA_512.digest(data);
    }

    /**
     * Converts to sha512
     * @param data the data that should encoded
     */
    public static byte[] sha512(InputStream data) throws IOException {
        return toBytes(SHA_512, data);
    }

    /**
     * Converts to sha512
     * @param data the data that should encoded
     */
    public static byte[] sha512(String data) {
        return sha512(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Converts to sha512 and then hex it
     * @param data the data that should encoded
     */
    public static String sha512Hex(byte[] data) {
        return bytesToHex(sha512(data));
    }

    /**
     * Converts to sha512 and then hex it
     * @param data the data that should encoded
     */
    public static String sha512Hex(InputStream data) throws IOException {
        return bytesToHex(sha512(data));
    }

    /**
     * Converts to sha512 and then hex it
     * @param data the data that should encoded
     */
    public static String sha512Hex(String data) {
        return bytesToHex(sha512(data));
    }
}
