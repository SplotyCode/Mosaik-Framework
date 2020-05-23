package io.github.splotycode.mosaik.util.reflection.classpath;

import io.github.splotycode.mosaik.util.logger.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public class BuiltinClassLoader9Support implements ClassLoaderSupport {

    private static final Logger LOGGER = Logger.getInstance(BuiltinClassLoader9Support.class);

    private Class<?> builtinClassLoaderClass;
    private Field builtinClassLoaderUCPField;
    private Field ucpPathField;
    private boolean supported;

    public BuiltinClassLoader9Support() {
        try {
            builtinClassLoaderClass = Class.forName("jdk.internal.loader.BuiltinClassLoader");
            builtinClassLoaderUCPField = builtinClassLoaderClass.getDeclaredField("ucp");
            builtinClassLoaderUCPField.setAccessible(true);
            ucpPathField = Class.forName("jdk.internal.loader.URLClassPath").getDeclaredField("path");
            ucpPathField.setAccessible(true);
            supported = true;
        } catch (ClassNotFoundException | NoSuchFieldException e) {

        }
    }

    @Override
    public boolean supports(ClassLoader classLoader) {
        return builtinClassLoaderClass.isAssignableFrom(classLoader.getClass());
    }

    private boolean unsupportedWorkaround(ClassPath.Loader classPathLoader, ClassLoader classLoader,
                                          ClassPathVisitor visitor) {
        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);

        ArrayList<URL> urlEntries = new ArrayList<>(classpathEntries.length);
        for (String url : classpathEntries) {
            try {
                urlEntries.add(new URL(url));
            } catch (MalformedURLException e) {
                throw new ClassPathConfigurationException("Url: '" + url + "' from property java.class.path is malformed", e);
            }
        }
        return loadUrls(classPathLoader, classLoader, visitor, urlEntries);
    }

    private boolean loadUrls(ClassPath.Loader classPathLoader, ClassLoader classLoader,
                          ClassPathVisitor visitor, Collection<URL> urls) {
        for (URL url : urls) {
            if (url.getProtocol().equalsIgnoreCase("file")) {
                File file = new File(url.getFile());
                if (classPathLoader.scanClasspathEntry(visitor, classLoader, file)) {
                    return true;
                }
            } else {
                LOGGER.debug("Unknown protocol type " + url.getProtocol() + " from classloader " + classLoader);
            }
        }
        return false;
    }

    @Override
    public boolean load(ClassPath.Loader classPathLoader, ClassLoader classLoader, ClassPathVisitor visitor) {
        if (supported) {
            try {
                Object ucp = builtinClassLoaderUCPField.get(classLoader);
                if (ucp != null) {
                    return loadUrls(classPathLoader, classLoader, visitor, (ArrayList<URL>) ucpPathField.get(ucp));
                } else {
                    LOGGER.warn("URLClassPath of " + classLoader + " is null");
                }
            } catch (IllegalAccessException e) {
                LOGGER.warn("Failed get get paths from " + classLoader, e);
            }
        }
        return unsupportedWorkaround(classPathLoader, classLoader, visitor);
    }
}
