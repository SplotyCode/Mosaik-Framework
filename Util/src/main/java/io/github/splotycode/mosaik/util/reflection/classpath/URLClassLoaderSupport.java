package io.github.splotycode.mosaik.util.reflection.classpath;

import io.github.splotycode.mosaik.util.logger.Logger;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class URLClassLoaderSupport implements ClassLoaderSupport {

    private final Logger LOGGER = Logger.getInstance(URLClassLoaderSupport.class);

    @Override
    public boolean supports(ClassLoader classLoader) {
        return classLoader instanceof URLClassLoader;
    }

    @Override
    public boolean load(ClassPath.Loader classPathLoader, ClassLoader classLoader, ClassPathVisitor visitor) {
        for (URL url : ((URLClassLoader) classLoader).getURLs()) {
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
}
