package io.github.splotycode.mosaik.util.io;

import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.condition.Conditions;
import io.github.splotycode.mosaik.util.info.SystemInfo;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.util.reflection.ClassFinderHelper;
import io.github.splotycode.mosaik.util.reflection.ClassPath;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtil {

    public static final File[] EMPTY_FILE_ARRAY = new File[0];

    private static File tempDirectory;
    private static final Logger logger = Logger.getInstance(FileUtil.class);

    public static File createTempFile(String prefix, String suffix, boolean deleteOnExit) {
        final File dir = getTempDirectory();

        dir.mkdirs();

        if (prefix.length() < 3) {
            prefix = (prefix + "___").substring(0, 3);
        }
        if (suffix == null) {
            suffix = "";
        }
        // normalize and use only the file name from the prefix
        prefix = new File(prefix).getName();

        int i = 0;
        File file;

        while (true) {
            String prefixName = prefix +  (i == 0 ? "" : i);
            if (prefixName.endsWith(".") && prefixName.startsWith(".")) {
                prefixName = prefixName.substring(0, prefixName.length() - 1);
            }
            String name = prefixName + suffix;
            file = new File(dir, name);
            boolean success = create(file);
            if (success) {
                break;
            }
            i++;
        }

        if (deleteOnExit) {
            file.deleteOnExit();
        }
        return file;
    }

    private static boolean create(File file) {
        try {
            return file.createNewFile();
        } catch (IOException e) {
            ExceptionUtil.throwRuntime(e);
            return false;
        }
    }

    private static File getTempDirectory() {
        if (tempDirectory == null) {
            tempDirectory = getTempDirectory0();
        }
        return tempDirectory;
    }

    private static File getTempDirectory0() {
        return new File(System.getProperty("java.io.tmpdir"));
    }

    public static void setExecutableAttribute(String path, boolean executableFlag) {
        File file = new File(path);
        if (!file.setExecutable(executableFlag) && file.canExecute() != executableFlag) {
            logger.warn("Setting Executable for '" + path + "' failed");
        }
    }

    public static void resourceToFile(final String name, final File file) {
        try {
            writeToFile(file, IOUtil.resourceToURL(name).openStream());
        } catch (IOException e) {
            ExceptionUtil.throwRuntime(e);
        }
    }

    public static String loadFile(File file) {
        return loadFile(file, (Charset) null);
    }

    public static String loadFile(File file, String charset) {
        return loadFile(file, charset == null? null : Charset.forName(charset));
    }

    public static String loadFile(File file, Charset encoding) {
        try (InputStream stream = new FileInputStream(file);
             Reader reader = encoding == null ?
                     new InputStreamReader(stream) :
                     new InputStreamReader(stream, encoding)) {
            return IOUtil.loadText(reader, (int) file.length());
        } catch (IOException e) {
            ExceptionUtil.throwRuntime(e);
            return null;
        }
    }

    public static List<String> loadLines(File file) {
        return loadLines(file, (Charset) null);
    }

    public static List<String> loadLines(File file, Charset charset) {
        try (InputStream stream = new FileInputStream(file)) {
            return IOUtil.loadLines(stream, charset);
        } catch (IOException e) {
            ExceptionUtil.throwRuntime(e);
            return null;
        }
    }

    public static void loadLines(File file, Consumer<String> callback) {
        loadLines(file, callback, null);
    }

    public static void loadLines(File file, Consumer<String> callback, Charset charset) {
        try (InputStream stream = new FileInputStream(file)) {
            IOUtil.loadLines(stream, callback, charset);
        } catch (IOException e) {
            ExceptionUtil.throwRuntime(e);
        }
    }

    public static boolean delete(File file) {
        try {
            Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) {
                    delete(path);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path path, IOException e) {
                    delete(path);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            logger.info(e);
            return false;
        }
        return true;
    }

    private static void delete(Path path) {
        boolean result = false;
        try {
            Throwable error = null;
            try {
                result = Files.deleteIfExists(path);
            } catch (AccessDeniedException ex) {
                error = ex;
                try {
                    File file = path.toFile();
                    if (file != null && (file.delete() || !file.exists())) {
                        result = true;
                    }
                } catch (Throwable throwable) {
                    error = throwable;
                }
            }
            if (!result) {
                throw new IOException("Failed to delete " + path.toString(), error);
            }
        } catch (IOException e) {
            ExceptionUtil.throwRuntime(e);
        }
    }

    public static boolean ensureCanCreateFile(File file) {
        if (file.exists()) return file.canWrite();
        if (!createIfNotExists(file)) return false;
        return delete(file);
    }

    public static boolean createIfNotExists(File file) {
        if (file.exists()) return true;
        try {
            if (!createParentDirs(file)) return false;

            OutputStream s = new FileOutputStream(file);
            s.close();
            return true;
        } catch (IOException e) {
            logger.info(e);
            return false;
        }
    }

    public static boolean createParentDirs(File file) {
        if (!file.exists()) {
            final File parentFile = file.getParentFile();
            if (parentFile != null) {
                return createDirectory(parentFile);
            }
        }
        return true;
    }

    public static boolean createDirectory(File path) {
        return path.isDirectory() || path.mkdirs();
    }


    public static void copy(File fromFile, File toFile) {
        if (!ensureCanCreateFile(toFile)) {
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(toFile)) {
            try (FileInputStream fis = new FileInputStream(fromFile)) {
                IOUtil.copy(fis, fos);
            }
        } catch (IOException ex) {
            ExceptionUtil.throwRuntime(ex);
        }

        long timeStamp = fromFile.lastModified();
        if (timeStamp < 0) {
            logger.warn("Invalid timestamp " + timeStamp + " of '" + fromFile + "'");
        } else if (!toFile.setLastModified(timeStamp)) {
            logger.warn("Unable to set timestamp " + timeStamp + " to '" + toFile + "'");
        }
    }

    public static byte[] loadFileBytes(File file) {
        byte[] bytes = null;
        try (InputStream stream = new FileInputStream(file)) {
            final long len = file.length();
            if (len < 0) {
                throw new IOException("File length reported negative, probably doesn't exist");
            }

            if (len > 20 * 1024 * 1024) {
                throw new IOException("File is to big: " + StringUtil.humanReadableBytes(len));
            }

            bytes = IOUtil.loadBytes(stream, (int) len);
        } catch (IOException ex) {
            ExceptionUtil.throwRuntime(ex);
        }
        return bytes;
    }

    public static void copyFileOrDir(File from, File to) {
        if (from.isDirectory()) {
            copyDir(from, to, Conditions.alwaysTrue());
        } else {
            copy(from, to);
        }
    }

    public static void ensureExists(File dir) {
        if (!dir.exists() && !dir.mkdirs()) {
            ExceptionUtil.throwRuntime(new IOException("Ensure existent failed"));
        }
    }

    public static void copyDir(File fromDir, File toDir, Predicate<File> filter) {
        ensureExists(toDir);
        File[] files = fromDir.listFiles();
        if (files == null) ExceptionUtil.throwRuntime(new IOException("Could not list files from " + fromDir.getAbsolutePath()));
        if (!fromDir.canRead()) ExceptionUtil.throwRuntime(new IOException("Can not read file"));
        for (File file : files) {
            if (filter != null && !filter.test(file)) {
                continue;
            }
            if (file.isDirectory()) {
                copyDir(file, new File(toDir, file.getName()), filter);
            }
            else {
                copy(file, new File(toDir, file.getName()));
            }
        }
    }

    public static boolean rename(File source, String newName) {
        File target = new File(source.getParent(), newName);
        return source.renameTo(target);
    }

    public static void rename(File source, File target) {
        if (source.renameTo(target)) return;
        if (!source.exists()) return;

        copy(source, target);
        delete(source);
    }

    public static boolean canExecute(File file) {
        return file.canExecute();
    }

    public static boolean canWrite(String path) {
        if (SystemInfo.isWindows) {
            try {
                DosFileAttributes attributes = Files.readAttributes(Paths.get(path), DosFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
                return attributes.isDirectory() || !attributes.isReadOnly();
            } catch (IOException e) {
                ExceptionUtil.throwRuntime(e);
            }
        }
        return new File(path).canWrite();
    }

    public static void setReadOnlyAttribute(String path, boolean readOnlyFlag) {
        boolean writableFlag = !readOnlyFlag;
        if (!new File(path).setWritable(writableFlag, false) && canWrite(path) != writableFlag) {
            logger.warn("Can't set writable attribute of '" + path + "' to '" + readOnlyFlag + "'");
        }
    }

    public static void appendToFile(File file, String text) {
        writeToFile(file, text.getBytes(StandardCharsets.UTF_8), true);
    }

    public static void writeToFile(File file, byte[] text) {
        writeToFile(file, text, false);
    }

    public static void writeToFile(File file, String text) {
        writeToFile(file, text, false);
    }
    public static void writeToFile(File file, String text, boolean append) {
        writeToFile(file, text.getBytes(StandardCharsets.UTF_8), append);
    }

    public static void writeToFile(File file, InputStream stream) {
        createParentDirs(file);

        try {
            Files.copy(stream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            ExceptionUtil.throwRuntime(e);
        }
    }

    private static void writeToFile(File file, byte[] text, boolean append) {
        createParentDirs(file);

        if (!file.exists()) {
            create(file);
        }

        try (FileOutputStream stream = new FileOutputStream(file, append)) {
            stream.write(text);
        } catch (IOException ex) {
            ExceptionUtil.throwRuntime(ex);
        }
    }

    public static boolean processFilesRecursively(File root, Predicate<File> processor, final Predicate<File> directoryFilter) {
        final LinkedList<File> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            final File file = queue.removeFirst();
            if (!processor.test(file)) return false;
            if (directoryFilter != null && (!file.isDirectory() || !directoryFilter.test(file))) continue;

            final File[] children = file.listFiles();
            if (children != null) {
                Collections.addAll(queue, children);
            }
        }
        return true;
    }

    public static File findFirstThatExist(String... paths) {
        for (String path : paths) {
            if (!StringUtil.isEmpty(path)) {
                File file = new File(path);
                if (file.exists()) return file;
            }
        }

        return null;
    }

    public static void setLastModified(File file, long timeStamp) {
        if (!file.setLastModified(timeStamp)) {
            logger.warn(file.getPath());
        }
    }

    public static boolean visitFiles(File root, Predicate<? super File> processor) {
        if (!processor.test(root)) {
            return false;
        }

        File[] children = root.listFiles();
        if (children != null) {
            for (File child : children) {
                if (!visitFiles(child, processor)) {
                    return false;
                }
            }
        }

        return true;
    }


    public static Map<String, String> loadProperties(Reader reader) {
        final Map<String, String> map = new HashMap<>();

        try {
            new Properties() {
                @Override
                public synchronized Object put(Object key, Object value) {
                    map.put(String.valueOf(key), String.valueOf(value));
                    return super.put(key, value);
                }
            }.load(reader);
        } catch (IOException e) {
            ExceptionUtil.throwRuntime(e);
        }

        return map;
    }


    public static void copyResource(String resource, File destination) {
        copyResource(resource, destination, null);
    }

    public static void copyResource(String resource, File destination, ClassLoader loader) {
        try (InputStream is = IOUtil.resourceToURL(resource, loader).openStream()) {
            writeToFile(destination, is);
        } catch (IOException e) {
            ExceptionUtil.throwRuntime(e);
        }
    }

    public static void listFiles(File root, boolean recursive, Consumer<File> callback) {
        if (recursive) {
            try {
                Files.walkFileTree(root.toPath(), new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) {
                        callback.accept(path.toFile());
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                /* impossible */
                ExceptionUtil.throwRuntime(e);
            }
        } else {
            File[] files = root.listFiles();
            if (files != null) {
                for (File file : files) {
                    callback.accept(file);
                }
            }
        }
    }

    public static void copyResources(String path, File root, boolean recursive) {
        new ClassPath(ClassFinderHelper.getClassLoader()).resources(resource -> {
            if (resource.inPackage(path) && (recursive || !resource.getPath().substring(path.length()).contains("/"))) {
                resource.export(new File(root, resource.name()));
            }
        });
    }

}
