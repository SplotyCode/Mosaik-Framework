package me.david.davidlib.utils.io;

import me.david.davidlib.logger.Logger;
import me.david.davidlib.utils.array.ArrayUtil;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public final class IOUtil {

    public static final int THREAD_LOCAL_BUFFER_LENGTH = 1024 * 20;
    private static final ThreadLocal<byte[]> BUFFER = ThreadLocal.withInitial(() -> new byte[THREAD_LOCAL_BUFFER_LENGTH]);

    private static Logger logger = Logger.getInstance(IOUtil.class);

    public static String loadText(Reader reader, int length) {
        char[] chars = new char[length];
        int count = 0;
        while (count < chars.length) {
            int n = 0;
            try {
                n = reader.read(chars, count, chars.length - count);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (n <= 0) break;
            count += n;
        }
        if (count == chars.length) {
            return new String(chars);
        }
        char[] newChars = new char[count];
        System.arraycopy(chars, 0, newChars, 0, count);
        return new String(newChars);
    }

    public static List<String> loadLines(BufferedReader reader) throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    public static byte[] loadBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        copy(stream, buffer);
        return buffer.toByteArray();
    }

    public static byte[] loadBytes(InputStream stream, int length) throws IOException {
        if (length == 0) {
            return ArrayUtil.EMPTY_BYTE_ARRAY;
        }
        byte[] bytes = new byte[length];
        int count = 0;
        while (count < length) {
            int n = stream.read(bytes, count, length - count);
            if (n <= 0) break;
            count += n;
        }
        return bytes;
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        if (inputStream instanceof FileInputStream && outputStream instanceof FileOutputStream) {
            final FileChannel fromChannel = ((FileInputStream)inputStream).getChannel();
            try {
                final FileChannel toChannel = ((FileOutputStream)outputStream).getChannel();
                try {
                    fromChannel.transferTo(0, Long.MAX_VALUE, toChannel);
                } finally {
                    toChannel.close();
                }
            } finally {
                fromChannel.close();
            }
        } else {
            final byte[] buffer = BUFFER.get();
            while (true) {
                int read = inputStream.read(buffer);
                if (read < 0) break;
                outputStream.write(buffer, 0, read);
            }
        }
    }

    public static byte[] loadFirstAndClose(InputStream stream, int maxLength) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            IOUtil.copy(stream, maxLength, buffer);
        }
        finally {
            stream.close();
        }
        return buffer.toByteArray();
    }

    public static void copy(InputStream inputStream, long maxSize, OutputStream outputStream) throws IOException {
        final byte[] buffer = BUFFER.get();
        long toRead = maxSize;
        while (toRead > 0) {
            int read = inputStream.read(buffer, 0, (int)Math.min(buffer.length, toRead));
            if (read < 0) break;
            toRead -= read;
            outputStream.write(buffer, 0, read);
        }
    }

}
