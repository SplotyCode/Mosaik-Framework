package me.david.davidlib.utils.io;

import me.david.davidlib.info.SystemInfo;
import me.david.davidlib.logger.Logger;
import me.david.davidlib.utils.StringUtil;
import me.david.davidlib.utils.condition.Condition;
import me.david.davidlib.utils.condition.Conditions;
import me.david.davidlib.utils.condition.Processor;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.util.*;

public final class FileUtil {

    private static File tempDirectory;
    private static final Logger logger = Logger.getInstance(FileUtil.class);

    public static File createTempFile(String prefix, String suffix, boolean deleteOnExit) throws IOException {
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
            if (prefix.endsWith(".") && suffix.startsWith(".")) {
                prefix = prefix.substring(0, prefix.length() - 1);
            }
            String name = prefix + suffix;
            file = new File(dir, name);
            boolean success = file.createNewFile();
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

    private static File getTempDirectory() {
        if (tempDirectory == null) {
            tempDirectory = getTempDirectory0();
        }
        return tempDirectory;
    }

    private static File getTempDirectory0() {
        return new File(System.getProperty("java.io.tmpdir"));
    }

    public static void setExecutableAttribute(String path, boolean executableFlag) throws IOException {
        File file = new File(path);
        if (!file.setExecutable(executableFlag) && file.canExecute() != executableFlag) {
            logger.warn("Setting Executable for '" + path + "' failed");
        }
    }

    public static String loadFile(File file) throws IOException {
        return loadFile(file, null);
    }

    public static String loadFile(File file, String encoding) throws IOException {
        InputStream stream = new FileInputStream(file);
        Reader reader = encoding == null ? new InputStreamReader(stream) : new InputStreamReader(stream, encoding);
        try {
            return IOUtil.loadText(reader, (int) file.length());
        } finally {
            reader.close();
        }
    }

    public static List<String> loadLines(File file) throws IOException {
        return loadLines(file, null);
    }

    public static List<String> loadLines(File file, String encoding) throws IOException {
        InputStream stream = new FileInputStream(file);
        try {
            InputStreamReader in = encoding == null ? new InputStreamReader(stream) : new InputStreamReader(stream, encoding);
            BufferedReader reader = new BufferedReader(in);
            try {
                return IOUtil.loadLines(reader);
            }
            finally {
                reader.close();
            }
        }
        finally {
            stream.close();
        }
    }

    public static boolean delete(File file) {
        try {
            Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) throws IOException {
                    delete(path);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path path, IOException e) throws IOException {
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

    private static void delete(Path path) throws IOException {
        boolean result = false;
        try {
            Files.deleteIfExists(path);
        } catch (AccessDeniedException ex) {
            try {
                File file = path.toFile();
                if (file == null) {
                    result = false;
                } else if (file.delete() || !file.exists()) {
                    result = true;
                }
            } catch (Throwable throwable) {
                result = false;
            }
        }
        if (!result) {
            throw new IOException("Failed to delete " + path.toString());
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


    public static void copy(File fromFile, File toFile) throws IOException {
        if (!ensureCanCreateFile(toFile)) {
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(toFile)) {
            try (FileInputStream fis = new FileInputStream(fromFile)) {
                IOUtil.copy(fis, fos);
            }
        }

        long timeStamp = fromFile.lastModified();
        if (timeStamp < 0) {
            logger.warn("Invalid timestamp " + timeStamp + " of '" + fromFile + "'");
        } else if (!toFile.setLastModified(timeStamp)) {
            logger.warn("Unable to set timestamp " + timeStamp + " to '" + toFile + "'");
        }
    }

    public static byte[] loadFileBytes(File file) throws IOException {
        byte[] bytes;
        try (InputStream stream = new FileInputStream(file)) {
            final long len = file.length();
            if (len < 0) {
                throw new IOException("File length reported negative, probably doesn't exist");
            }

            if (len > 20 * 1024 * 1024) {
                throw new IOException("File is to big: " + StringUtil.humanReadableBytes(len));
            }

            bytes = IOUtil.loadBytes(stream, (int) len);
        }
        return bytes;
    }

    public static void copyFileOrDir(File from, File to) throws IOException {
        if (from.isDirectory()) {
            copyDir(from, to, Conditions.alwaysTrue());
        } else {
            copy(from, to);
        }
    }

    public static void ensureExists(File dir) throws IOException {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Ensure existent failed");
        }
    }

    public static void copyDir(File fromDir, File toDir, Condition<File> filter) throws IOException {
        ensureExists(toDir);
        File[] files = fromDir.listFiles();
        if (files == null) throw new IOException("Could not list files from " + fromDir.getAbsolutePath());
        if (!fromDir.canRead()) throw new IOException("Can not read file");
        for (File file : files) {
            if (filter != null && !filter.check(file)) {
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

    public static boolean rename(File source, String newName) throws IOException {
        File target = new File(source.getParent(), newName);
        return source.renameTo(target);
    }

    public static void rename(File source, File target) throws IOException {
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
                e.printStackTrace();
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

    public static void appendToFile(File file, String text) throws IOException {
        writeToFile(file, text.getBytes(Charsets.UTF8), true);
    }

    public static void writeToFile(File file, byte[] text) throws IOException {
        writeToFile(file, text, false);
    }

    public static void writeToFile(File file, String text) throws IOException {
        writeToFile(file, text, false);
    }
    public static void writeToFile(File file, String text, boolean append) throws IOException {
        writeToFile(file, text.getBytes(Charsets.UTF8), append);
    }

    private static void writeToFile(File file, byte[] text, boolean append) throws IOException {
        createParentDirs(file);

        try (OutputStream stream = new FileOutputStream(file, append)) {
            stream.write(text, 0, text.length);
        }
    }

    public static boolean processFilesRecursively(File root, Processor<File> processor, final Processor<File> directoryFilter) {
        final LinkedList<File> queue = new LinkedList<File>();
        queue.add(root);
        while (!queue.isEmpty()) {
            final File file = queue.removeFirst();
            if (!processor.process(file)) return false;
            if (directoryFilter != null && (!file.isDirectory() || !directoryFilter.process(file))) continue;

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

    public static void setLastModified(File file, long timeStamp) throws IOException {
        if (!file.setLastModified(timeStamp)) {
            logger.warn(file.getPath());
        }
    }

    public static boolean visitFiles(File root, Processor<? super File> processor) {
        if (!processor.process(root)) {
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


    public static Map<String, String> loadProperties(Reader reader) throws IOException {
        final Map<String, String> map = new HashMap<>();

        new Properties() {
            @Override
            public synchronized Object put(Object key, Object value) {
                map.put(String.valueOf(key), String.valueOf(value));
                return super.put(key, value);
            }
        }.load(reader);

        return map;
    }

}
