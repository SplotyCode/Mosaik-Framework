package me.david.davidlib.util.info;

import me.david.davidlib.util.cache.Cache;
import me.david.davidlib.util.cache.DefaultCaches;

import java.io.File;

public final class SystemProperties {

    private static Cache<File> userHome = DefaultCaches.getNormalValueResolverCache(cache -> getUserHome0());
    private static Cache<String> lineSeparator = DefaultCaches.getNormalValueResolverCache(cache -> getLineSeparator0());
    private static Cache<File> javaHome = DefaultCaches.getNormalValueResolverCache(cache -> getJavaHome0());
    private static Cache<File> userName = DefaultCaches.getNormalValueResolverCache(cache -> getUserHome0());
    private static Cache<File> workingDirectory = DefaultCaches.getNormalValueResolverCache(cache -> getWorkingDirectory0());


    private static File getUserHome0() {
        return new File(System.getProperty("user.home"));
    }

    public static File getUserHome() {
        return userHome.getValue();
    }

    private static String getLineSeparator0() {
        return System.getProperty("line.separator");
    }

    public static String getLineSeparator() {
        return lineSeparator.getValue();
    }

    private static File getJavaHome0() {
        return new File(System.getProperty("java.home"));
    }

    public static File getJavaHome() {
        return javaHome.getValue();
    }

    private static String getUserName0() {
        return System.getProperty("user.name");
    }

    public static File getUserName() {
        return userName.getValue();
    }

    private static File getWorkingDirectory0() {
        return new File(".");
    }

    public static File getWorkingDirectory() {
        return workingDirectory.getValue();
    }
}
