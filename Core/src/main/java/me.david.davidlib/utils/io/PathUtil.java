package me.david.davidlib.utils.io;

import lombok.Getter;
import me.david.davidlib.cache.Cache;
import me.david.davidlib.cache.DefaultCaches;
import me.david.davidlib.info.SystemInfo;

import java.io.File;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class PathUtil {

    public static String getFileName(String path) {
        if (path == null || path.length() == 0) {
            return "";
        }

        char c = path.charAt(path.length() - 1);
        int end = c == '/' || c == '\\' ? path.length() - 1 : path.length();
        int start = Math.max(path.lastIndexOf('/', end - 1), path.lastIndexOf('\\', end - 1)) + 1;
        return path.substring(start, end);
    }

    @Getter private static final Set<String> WINDOWS_NAMES = new HashSet<>(Arrays.asList(
            "CON", "PRN", "AUX", "NUL",
            "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9",
            "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"));

    @Getter private static final String WINDOWS_CHARS = "<>:\"|?*";

    public static boolean isValidFileName(String name) {
        if (name.length() == 0 || name.equals(".") || name.equals("..")) {
            return false;
        }

        for (int i = 0; i < name.length(); i++) {
            if (!isValidFileNameChar(name.charAt(i))) {
                return false;
            }
        }

        if (name.length() >= 3 && name.length() <= 4 && WINDOWS_NAMES.contains(name.toUpperCase(Locale.US))) {
            return false;
        }

        Charset cs = Charset.forName("UTF-16");
        return cs.canEncode() && cs.newEncoder().canEncode(name);
    }

    private static boolean isValidFileNameChar(char c) {
        if (c == '/' || c == '\\') return false;
        if (c < 32 || WINDOWS_CHARS.indexOf(c) >= 0) return false;
        if (c == ';') return false;
        return true;
    }

    public static String getExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index < 0) return "";
        return fileName.substring(index + 1);
    }

    public static boolean extensionEquals(String path, String... extensions) {
        Cache<Boolean> noEx = DefaultCaches.getNormalValueResolverCache(cache -> {
            int lastSlash = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
            return path.indexOf('.', lastSlash + 1) == -1;
        });
        for (String extension : extensions) {
            int extLen = extension.length();
            if (extLen == 0) {
                return noEx.getValue();
            }
            int extStart = path.length() - extLen;
            if (extStart >= 1 && path.charAt(extStart - 1) == '.' && path.regionMatches(false, extStart, extension, 0, extLen)) {
                return true;
            }
        }
        return false;
    }

    public static String toSystemIndependentName(String fileName) {
        return fileName.replace('\\', '/');
    }

    public static String getRelativePath(String basePath, String filePath) {
        return getRelativePath(new File(basePath), new File(filePath));
    }

    public static String getRelativePath(File basePath, File filePath) {
        return getRelativePath(basePath.toURI(), filePath.toURI());
    }

    public static String getRelativePath(URI basePath, URI filePath) {
        return basePath.relativize(filePath).getPath();
    }

    public static String normalize(String path) {
        int start = 0;
        boolean separator = false;
        if (SystemInfo.isWindows) {
            if (path.startsWith("//")) {
                start = 2;
                separator = true;
            }
            else if (path.startsWith("\\\\")) {
                return normalizeTail(0, path, false);
            }
        }

        for (int i = start; i < path.length(); ++i) {
            final char c = path.charAt(i);
            if (c == '/') {
                if (separator) {
                    return normalizeTail(i, path, true);
                }
                separator = true;
            }
            else if (c == '\\') {
                return normalizeTail(i, path, separator);
            }
            else {
                separator = false;
            }
        }

        return path;
    }

    private static String normalizeTail(int prefixEnd, String path, boolean separator) {
        final StringBuilder result = new StringBuilder(path.length());
        result.append(path, 0, prefixEnd);
        int start = prefixEnd;
        if (start==0 && SystemInfo.isWindows && (path.startsWith("//") || path.startsWith("\\\\"))) {
            start = 2;
            result.append("//");
            separator = true;
        }

        for (int i = start; i < path.length(); ++i) {
            final char c = path.charAt(i);
            if (c == '/' || c == '\\') {
                if (!separator) result.append('/');
                separator = true;
            }
            else {
                result.append(c);
                separator = false;
            }
        }

        return result.toString();
    }

}
