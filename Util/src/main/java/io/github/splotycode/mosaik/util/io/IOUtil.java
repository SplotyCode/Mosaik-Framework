package io.github.splotycode.mosaik.util.io;

import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.exception.ResourceNotFoundException;
import io.github.splotycode.mosaik.util.reflection.ClassFinderHelper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IOUtil {

    public static final int THREAD_LOCAL_BUFFER_LENGTH = 1024 * 20;
    private static final ThreadLocal<byte[]> BUFFER = ThreadLocal.withInitial(() -> new byte[THREAD_LOCAL_BUFFER_LENGTH]);

    public static String loadText(Reader reader, int length) {
        char[] chars = new char[length];
        int count = 0;
        while (count < chars.length) {
            try {
                int n = reader.read(chars, count, chars.length - count);
                if (n > 0) {
                    count += n;
                }
            } catch (IOException e) {
                ExceptionUtil.throwRuntime(e);
            }
        }
        if (count == chars.length) {
            return new String(chars);
        }
        char[] newChars = new char[count];
        System.arraycopy(chars, 0, newChars, 0, count);
        return new String(newChars);
    }

    public static List<String> loadLines(InputStream stream) {
        return loadLines(stream, StandardCharsets.UTF_8);
    }

    public static List<String> loadLines(URL url) {
        return loadLines(url, StandardCharsets.UTF_8);
    }

    public static List<String> loadLines(URL url, Charset charset) {
        try (InputStream stream = url.openStream()) {
            return loadLines(stream, charset);
        } catch (IOException ex) {
            ExceptionUtil.throwRuntime(ex);
            return null;
        }
    }

    public static void loadLines(URL url, Consumer<String> callback) {
        loadLines(url, callback, StandardCharsets.UTF_8);
    }

    public static void loadLines(URL url, Consumer<String> callback, Charset charset) {
        try (InputStream stream = url.openStream()) {
            loadLines(stream, callback, charset);
        } catch (IOException ex) {
            ExceptionUtil.throwRuntime(ex);
        }
    }

    public static void loadLines(InputStream stream, Consumer<String> callback) {
        loadLines(stream, callback, StandardCharsets.UTF_8);
    }

    public static List<String> loadLines(InputStream stream, Charset charset) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, charset))) {
            return loadLines(br);
        } catch (IOException e) {
            ExceptionUtil.throwRuntime(e);
        }
        return Collections.emptyList();
    }

    public static void loadLines(InputStream stream, Consumer<String> callback, Charset charset) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, charset))) {
            loadLines(br, callback);
        } catch (IOException e) {
            ExceptionUtil.throwRuntime(e);
        }
    }

    public static List<String> loadLines(BufferedReader reader) {
        List<String> lines = new ArrayList<>();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException ex) {
            ExceptionUtil.throwRuntime(ex);
        }
        return lines;
    }

    public static void loadLines(BufferedReader reader, Consumer<String> callback) {
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                callback.accept(line);
            }
        } catch (IOException ex) {
            ExceptionUtil.throwRuntime(ex);
        }
    }

    public static byte[] loadBytes(InputStream stream) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        copy(stream, buffer);
        return buffer.toByteArray();
    }

    public static byte[] loadBytes(InputStream stream, int length) {
        if (length == 0) {
            return ArrayUtil.EMPTY_BYTE_ARRAY;
        }
        byte[] bytes = new byte[length];
        int count = 0;
        while (count < length) {
            try {
                int n = stream.read(bytes, count, length - count);
                if (n <= 0) break;
                count += n;
            } catch (IOException e) {
                ExceptionUtil.throwRuntime(e);
            }
        }
        return bytes;
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) {
        try {
            if (inputStream instanceof FileInputStream && outputStream instanceof FileOutputStream) {
                final FileChannel fromChannel = ((FileInputStream) inputStream).getChannel();
                try {
                    final FileChannel toChannel = ((FileOutputStream) outputStream).getChannel();
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
        } catch (IOException ex) {
            ExceptionUtil.throwRuntime(ex);
        }
    }

    public static String loadText(final InputStream input) {
        return loadText(input, StandardCharsets.UTF_8);
    }

    public static String loadText(final InputStream input, final Charset encoding) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = input.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString(encoding.name());
        } catch (IOException ex) {
            ExceptionUtil.throwRuntime(ex);
            return null;
        }
    }

    public static byte[] loadFirstAndClose(InputStream stream, int maxLength) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            try {
                copy(stream, maxLength, buffer);
            } finally {
                stream.close();
            }
        } catch (IOException ex) {
            ExceptionUtil.throwRuntime(ex);
        }
        return buffer.toByteArray();
    }

    public static void copy(InputStream inputStream, long maxSize, OutputStream outputStream) {
        final byte[] buffer = BUFFER.get();
        long toRead = maxSize;
        try {
            while (toRead > 0) {
                int read = inputStream.read(buffer, 0, (int) Math.min(buffer.length, toRead));
                if (read < 0) break;
                toRead -= read;
                outputStream.write(buffer, 0, read);
            }
        } catch (IOException ex) {
            ExceptionUtil.throwRuntime(ex);
        }
    }

    public static String resourceToText(final String name) {
        return resourceToText(name, StandardCharsets.UTF_8);
    }

    public static String resourceToText(final String name, final Charset encoding) {
        return resourceToText(name, encoding, null);
    }

    public static String resourceToText(final String name, final Charset encoding, final ClassLoader classLoader) {
        return loadText(resourceToURL(name, classLoader), encoding);
    }

    public static URL resourceToURL(final String name) {
        return resourceToURL(name, null);
    }

    public static URL resourceToURL(String name, ClassLoader classLoader) {
        if (!name.startsWith("/") && classLoader == null) name = "/" + name;
        boolean triedUser = classLoader == null && ClassFinderHelper.getClassLoader() != null;
        if (triedUser) {
            classLoader = ClassFinderHelper.getClassLoader();
        }
        URL resource = classLoader == null ?
                IOUtil.class.getResource(name) :
                classLoader.getResource(name);
        if (resource == null) {
            if (triedUser && (resource = IOUtil.class.getResource(name)) != null) {
                return resource;
            }
            throw new RuntimeException("Resource not found: " + name);
        }
        return resource;
    }

    public static String loadText(final URL url, final Charset encoding) {
        try (InputStream inputStream = url.openStream()) {
            return loadText(inputStream, encoding);
        } catch (IOException e) {
            ExceptionUtil.throwRuntime(e);
            return null;
        }
    }

    public static byte[] loadBytes(final URL url) {
        try (InputStream inputStream = url.openStream()) {
            return loadBytes(inputStream);
        } catch (IOException e) {
            ExceptionUtil.throwRuntime(e);
            return null;
        }
    }

    public static InputStream toInputStream(final String input) {
        return toInputStream(input, Charset.defaultCharset());
    }

    public static InputStream toInputStream(final String input, final Charset encoding) {
        return new ByteArrayInputStream(input.getBytes(encoding));
    }

    public static byte[] toByteArray(final InputStream input) {
        try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            copy(input, output);
            return output.toByteArray();
        } catch (IOException e) {
            ExceptionUtil.throwRuntime(e);
            return null;
        }
    }

    private static FileSystem getFileSystem(URI uri) throws IOException {
        try {
            return FileSystems.getFileSystem(uri);
        } catch(FileSystemNotFoundException e) {
            Map<String, String> env = new HashMap<>();
            env.put("create", "true");
            return FileSystems.newFileSystem(uri, env);
        }
    }

    public static Path getResourcePath(String path, boolean closeFileSystem) {
        URL url = IOUtil.class.getResource(path);
        if (url == null) {
            throw new ResourceNotFoundException("Resource not found: " + path);
        }

        FileSystem fileSystem = null;
        try {
            URI uri = url.toURI();
            fileSystem = getFileSystem(uri);
            return Paths.get(uri);
        } catch (URISyntaxException | IOException e) {
            ExceptionUtil.throwRuntime(e);
        } finally {
            if (closeFileSystem && fileSystem != null) {
                String scheme = fileSystem.provider().getScheme().toLowerCase();
                if (scheme.equals("zip") || scheme.equals("jar")) {
                    try {
                        fileSystem.close();
                    } catch (IOException e) {
                        ExceptionUtil.throwRuntime(e);
                    }
                }
            }
        }
        return null;
    }

    public static Path getResourcePath(String path) {
        return getResourcePath(path, false);
    }

}
