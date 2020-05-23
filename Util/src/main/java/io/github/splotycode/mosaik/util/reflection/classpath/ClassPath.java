package io.github.splotycode.mosaik.util.reflection.classpath;

import io.github.splotycode.mosaik.util.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassPath {

    protected final Logger logger = Logger.getInstance(getClass());
    protected final CopyOnWriteArrayList<ClassLoaderSupport> classLoaderSupports = new CopyOnWriteArrayList<>();
    protected ArrayList<ClassLoader> classLoaders = new ArrayList<>();
    protected HashMap<String, Resource> resources = new HashMap<>();
    protected Loader loader = new Loader();

    public class Loader {

        private HashSet<String> scannedClassPathEntries = new HashSet<>();

        public void addResource(Resource resource) {
            resources.put(resource.getPath(), resource);
        }

        public void addResource(ClassLoader classLoader, String fullPath) {
            addResource(new Resource(fullPath, classLoader));
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
        classLoaderSupports.add(classLoaderSupport);
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

    public synchronized void load(ClassPathVisitor visitor) throws IOException {
        for (ClassLoader loader : classLoaders) {
            Optional<ClassLoaderSupport> support = getCLSupport(loader);
            if (support.isPresent()) {
                ClassLoaderSupport cls = support.get();
                cls.load(this.loader, loader, visitor);
            } else {
                logger.warn("Could not find ClassLoaderSupport for " + loader + " (" + loader.getClass().getSimpleName() + ")");
            }
        }
    }

    public void loadPackage(String packageName) throws IOException {
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

    public void loadAll() throws IOException {
        load(ClassPathVisitor.ACCEPT_ALL);
    }
}
