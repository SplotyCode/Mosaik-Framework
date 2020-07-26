package io.github.splotycode.mosaik.util.reflection.classpath;

import io.github.splotycode.mosaik.util.collection.FilteredIterator;
import io.github.splotycode.mosaik.util.collection.SimpleIterator;
import io.github.splotycode.mosaik.util.io.resource.ClassPathResource;
import io.github.splotycode.mosaik.util.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ClassPath {
    public static ClassPath fromClassLoader(ClassLoader classLoader) {
        return fromClassLoaders(classLoader);
    }

    public static ClassPath fromClassLoaders(ClassLoader... classLoaders) {
        ClassPath classPath = new ClassPath();
        for (ClassLoader classLoader : classLoaders) {
            classPath.addClassLoader(classLoader, true);
        }
        return classPath;
    }

    protected final Logger logger = Logger.getInstance(getClass());
    protected final LinkedBlockingDeque<ClassLoaderSupport> classLoaderSupports = new LinkedBlockingDeque<>();
    protected HashSet<ClassLoader> classLoaders = new HashSet<>();
    protected TreeMap<String, ClassPathResource> resources = new TreeMap<>();
    protected Loader loader = new Loader();
    protected ClassPathStats stats = new ClassPathStats(this);

    {
        addClassLoaderSupport(URLClassLoaderSupport.INSTANCE);
        addClassLoaderSupport(BuiltinClassLoader9Support.INSTANCE);
    }

    class Loader {
        private HashSet<String> scannedClassPathEntries = new HashSet<>();

        public void addResource(ClassPathResource resource) {
            resources.put(resource.getPath(), resource);
        }

        public void addResource(ClassLoader classLoader, String fullPath) {
            ClassPathResource current = resources.get(fullPath);
            if (current == null || current.getLoader() != classLoader) {
                resources.put(fullPath, new ClassPathResource(classLoader, fullPath));
            }
        }

        public boolean scanSourceDirectory(ClassPathVisitor visitor, File directory, ClassLoader loader) {
            return scanSourceDirectory(visitor, directory, loader, "");
        }

        public boolean scanSourceDirectory(ClassPathVisitor visitor, File directory, ClassLoader loader, String prefix) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                String name = prefix + file.getName();
                if (file.isDirectory()) {
                    VisitorAction action = visitor.visitPackage(name);
                    switch (action) {
                        case TERMINATE:
                            return true;
                        case CONTINUE:
                            scanSourceDirectory(visitor, file, loader, name + "/");
                            break;
                    }
                } else {
                    VisitorAction action = visitor.visitResource(name);
                    switch (action) {
                        case TERMINATE:
                            return true;
                        case CONTINUE:
                            addResource(loader, name);
                            break;
                    }
                }
            }
            return false;
        }

        public boolean scanJarFile(ClassPathVisitor visitor, JarFile jarFile, ClassLoader loader) {
            Enumeration<JarEntry> entries = jarFile.entries();
            String skippingDirectory = null;
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String fullName = entry.getName();
                if (skippingDirectory != null) {
                    if (fullName.startsWith(skippingDirectory)) {
                        continue;
                    } else {
                        skippingDirectory = null;
                    }
                }
                if (entry.isDirectory()) {
                    VisitorAction action = visitor.visitPackage(fullName);
                    switch (action) {
                        case TERMINATE:
                            return true;
                        case SKIP:
                            skippingDirectory = fullName;
                            break;
                    }
                } else {
                    VisitorAction action = visitor.visitResource(fullName);
                    switch (action) {
                        case TERMINATE:
                            return true;
                        case CONTINUE:
                            addResource(loader, fullName);
                            break;
                    }
                }
            }
            return false;
        }

        public boolean scanClasspathEntry(ClassPathVisitor visitor, ClassLoader classLoader, File file) {
            if (scannedClassPathEntries.add(file.getAbsolutePath())) {
                if (file.isDirectory()) {
                    return scanSourceDirectory(visitor, file, classLoader);
                } else if (file.getName().endsWith(".jar")) {
                    try (JarFile jar = new JarFile(file)) {
                        ClassPathUtil.collectJarMetaPaths(file, jar, metaPath ->
                                scanClasspathEntry(visitor, classLoader, metaPath)
                        );
                        if (scanJarFile(visitor, jar, classLoader)) {
                            return true;
                        }
                    } catch (MalformedURLException ex) {
                        throw new ClassPathConfigurationException(file.getAbsolutePath() + " from classloader " + classLoader +
                                " has am invalid url in the manifest", ex);
                    } catch (IOException ex) {
                        throw new ClassPathConfigurationException("Failed to open jar " + file.getAbsolutePath() +
                                " with loader " + classLoader, ex);
                    }
                } else {
                    logger.warn("Invalid file found in classpath " + file.getAbsolutePath() + " must be a jar file or directory");
                }
            }
            return false;
        }
    }

    public void addClassLoaderSupport(ClassLoaderSupport classLoaderSupport) {
        classLoaderSupports.addFirst(classLoaderSupport);
    }

    private Optional<ClassLoaderSupport> getCLSupport(ClassLoader loader) {
        for (ClassLoaderSupport support : classLoaderSupports) {
            if (support.supports(loader)) {
                return Optional.of(support);
            }
        }
        return Optional.empty();
    }

    public synchronized void addClassLoader(ClassLoader classLoader, boolean recursive) {
        ClassLoader parent;
        if (recursive && (parent = classLoader.getParent()) != null) {
            addClassLoader(parent, true);
        }
        classLoaders.add(classLoader);
    }

    public synchronized void load(ClassPathVisitor visitor) {
        for (ClassLoader loader : classLoaders) {
            Optional<ClassLoaderSupport> support = getCLSupport(loader);
            if (support.isPresent()) {
                ClassLoaderSupport cls = support.get();
                if (cls.load(this.loader, loader, visitor)) {
                    stats.reset();
                }
            } else {
                logger.warn("Could not find ClassLoaderSupport for " + loader + " (" + loader.getClass().getSimpleName() + ")");
            }
        }
    }

    public void loadPackage(String packageName) {
        load(new ClassPathVisitor() {
            @Override
            public VisitorAction visitPackage(String packagePath) {
                return packagePath.startsWith(packageName) ? VisitorAction.CONTINUE : VisitorAction.SKIP;
            }

            @Override
            public VisitorAction visitResource(String fullName) {
                return VisitorAction.CONTINUE;
            }
        });
    }

    public ClassPathStats stats() {
        return stats;
    }

    public void unloadAll() {
        resources.clear();
        stats.reset();
    }

    public void loadAll() {
        load(ClassPathVisitor.ACCEPT_ALL);
    }

    public void loadAllResources() {
        load(ClassPathVisitor.ONLY_RESOURCES);
    }

    public Stream<ClassPathResource> resources() {
        FilteredIterator<ClassPathResource> iterator = new FilteredIterator<>(resources.values().iterator(),
                ClassPathResource::isResource);
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                iterator, Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.NONNULL), false);
    }

    public Stream<ClassPathResource> classes() {
        FilteredIterator<ClassPathResource> iterator = new FilteredIterator<>(resources.values().iterator(),
                ClassPathResource::isResource);
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                iterator, Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.NONNULL), false);
    }

    public Stream<ClassPathResource> search(String query) {
        Iterator<ClassPathResource> iter = new SimpleIterator.NotNullSimpleIterator<ClassPathResource>() {
            private Map.Entry<String, ClassPathResource> entry;

            @Override
            protected ClassPathResource provideNext0() {
                if (entry == null) {
                    entry = resources.ceilingEntry(query);
                } else {
                    entry = resources.higherEntry(entry.getKey());
                }
                return entry == null ? null : entry.getValue();
            }
        };
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                iter, Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.NONNULL), false);
    }
}
