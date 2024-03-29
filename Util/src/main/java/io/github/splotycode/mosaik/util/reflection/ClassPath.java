package io.github.splotycode.mosaik.util.reflection;

import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.collection.MultiHashMap;
import io.github.splotycode.mosaik.util.io.FileUtil;
import io.github.splotycode.mosaik.util.io.PathUtil;
import io.github.splotycode.mosaik.util.logger.Logger;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Deprecated
public class ClassPath {
    private Logger logger = Logger.getInstance(ClassPath.class);

    @Getter private HashMap<File, ClassLoader> paths = new HashMap<>();
    @Getter private MultiHashMap<ClassLoader, Resource> resources = new MultiHashMap<>();
    private HashSet<String> scanned = new HashSet<>();

    public static class Resource {
        @Getter private final String path;
        @Getter private final ClassLoader loader;
        private Class<?> clazz;

        public Resource(String path, ClassLoader loader) {
            this.path = path;
            this.loader = loader;
        }

        public InputStream openInputStream() {
            return loader.getResourceAsStream(path);
        }

        public boolean isClass() {
            return path.endsWith(".class");
        }

        public String javaName() {
            return PathUtil.getFileNameWithoutEx(path).replace('/', '.');
        }

        public String name() {
            return PathUtil.getFileName(path);
        }

        public boolean inPackage(String path) {
            return this.path.startsWith(path);
        }

        public void export(File file) {
            FileUtil.copyResource(path, file, loader);
        }

        public Class<?> load() {
            if (clazz == null) {
                try {
                    clazz = loader.loadClass(javaName());
                } catch (ClassNotFoundException e) {
                    ExceptionUtil.throwRuntime(e);
                }
            }
            return clazz;
        }
    }

    private static Set<File> getMetaPaths(File file, JarFile jarFile) throws IOException {
        String classpathAttribute = jarFile.getManifest().getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
        if (classpathAttribute == null) return Collections.emptySet();
        Set<File> classpath = new HashSet<>();
        for (String path : classpathAttribute.split(" ")) {
            if (!StringUtil.isEmptyDeep(path)) {
                URL url = new URL(file.toURI().toURL(), path);
                classpath.add(new File(url.getFile()));
            }
        }
        return classpath;
    }

    public ClassPath(ClassLoader loader) {
        getClassPaths(loader);
        for (Map.Entry<File, ClassLoader> pathEntry : paths.entrySet()) {
            File file = pathEntry.getKey();
            try {
                scan(file, pathEntry.getValue());
            } catch (IOException e) {
                ExceptionUtil.throwRuntime(e);
            }
        }
    }

    protected void getClassPaths(ClassLoader loader) {
        ClassLoader parent = loader.getParent();
        if (parent != null) {
            getClassPaths(parent);
        }
        if (loader instanceof URLClassLoader) {
            URLClassLoader urlLoader = (URLClassLoader) loader;
            for (URL url : urlLoader.getURLs()) {
                if (url.getProtocol().equalsIgnoreCase("file")) {
                    paths.putIfAbsent(new File(url.getFile()), loader);
                } else {
                    logger.debug("Unknown protocol type " + url.getProtocol() + " from classloader " + urlLoader);
                }
            }
        } else {
            logger.warn("Unknown classloader type: " + loader  + " (" + loader.getClass().getSimpleName() + ")");
        }
    }

    private void scanSourceDirectory(File dir, ClassLoader loader, String current) {
        for (File file : dir.listFiles()) {
            String name = current + file.getName();
            if (file.isDirectory()) {
                scanSourceDirectory(file, loader, name + "/");
            } else {
                resources.addToList(loader, new Resource(name, loader));
            }
        }
    }

    private void scan(File file, ClassLoader classLoader) throws IOException {
        String path = file.getAbsolutePath();
        if (scanned.add(path)) {
            if (file.isDirectory()) {
                scanSourceDirectory(file, classLoader, "");
            } else if (file.getName().endsWith(".jar")) {
                try (JarFile jar = new JarFile(file)) {
                    for (File classPath : getMetaPaths(file, jar)) {
                        scan(classPath, classLoader);
                    }
                    Enumeration entries = jar.entries();
                    while(entries.hasMoreElements()) {
                        JarEntry entry = (JarEntry)entries.nextElement();
                        if (!entry.isDirectory()) {
                            resources.addToList(classLoader, new Resource(entry.getName(), classLoader));
                        }
                    }
                }
            }
        }
    }

    public void resources(Consumer<Resource> consumer) {
        for (Iterator<Resource> it = resources.allValues(); it.hasNext(); ) {
            Resource resource = it.next();
            if (!resource.isClass()) {
                consumer.accept(resource);
            }
        }
    }

    public void classes(Consumer<Resource> consumer) {
        for (Iterator<Resource> it = resources.allValues(); it.hasNext(); ) {
            Resource resource = it.next();
            if (resource.isClass()) {
                consumer.accept(resource);
            }
        }
    }

}
