package io.github.splotycode.mosaik.util.reflection.collector;

import io.github.splotycode.mosaik.util.reflection.classpath.ClassPath;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

public class CachedClassCollector extends ClassCollector {
    public static CachedClassCollector newInstance() {
        return new CachedClassCollector();
    }

    private final Map<ClassPath, ClassPathCache> cacheMap = new HashMap<>();

    public ClassPathCache getCache(ClassPath classPath) {
        synchronized (cacheMap) {
            return cacheMap.computeIfAbsent(classPath, path -> new ClassPathCache(classPath));
        }
    }

    @RequiredArgsConstructor
    private class ClassPathCache {
        private final ClassPath classPath;
        private volatile List<Class> classes;
        private volatile Collection instances;
        private volatile Class firstClass;
        private volatile long results = -1;

        public synchronized Class getFirstClass() {
            if (firstClass == null) {
                firstClass = CachedClassCollector.super.collectFirst(classPath);
            }
            return firstClass;
        }

        public synchronized Collection getInstances() {
            if (instances == null) {
                if (classes == null) {
                    instances = CachedClassCollector.super.collectAllInstances(classPath);
                } else {
                    instances = new ArrayList<>(classes.size());
                    for (Class clazz : classes) {
                        //noinspection unchecked
                        instances.add(createInstance(clazz));
                    }
                }
            }
            return instances;
        }

        public synchronized long getResults() {
            if (results == -1) {
                results = CachedClassCollector.super.totalResults(classPath);
            }
            return results;
        }

        public synchronized Collection<Class> getClasses() {
            if (classes == null) {
                classes = streamClasses(classPath).collect(Collectors.toList());
                results = classes.size();
                firstClass = classes.get(0);
            }
            return classes;
        }
    }

    @Override
    protected void reset() {
        cacheMap.clear();
    }

    @Override
    public Collection collectAllInstances(ClassPath classPath) {
        return getCache(classPath).getInstances();
    }

    @Override
    public Class collectFirst(ClassPath classPath) {
        return getCache(classPath).getFirstClass();
    }

    @Override
    public Collection<Class> collectAll(ClassPath classPath) {
        return getCache(classPath).getClasses();
    }

    @Override
    public long totalResults(ClassPath classPath) {
        return getCache(classPath).getResults();
    }
}
