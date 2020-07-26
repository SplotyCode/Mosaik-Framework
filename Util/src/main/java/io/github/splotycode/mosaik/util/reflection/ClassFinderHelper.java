package io.github.splotycode.mosaik.util.reflection;

import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.cache.CacheBuilder;
import io.github.splotycode.mosaik.util.cache.complex.ComplexCache;
import io.github.splotycode.mosaik.util.cache.complex.resolver.CacheValueResolver;
import io.github.splotycode.mosaik.util.cache.complex.validator.TimeValidator;
import io.github.splotycode.mosaik.util.logger.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Deprecated
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassFinderHelper {

    private static Logger logger = Logger.getInstance(ClassFinderHelper.class);

    private static ClassLoader classLoader;

    public static void setClassLoader(ClassLoader classLoader) {
        ClassFinderHelper.classLoader = classLoader;
    }

    private static Set<String> nonUserPrefixes = new HashSet<>();
    @Getter private static long totalClassCount;

    private static boolean debugUserClasses = !StringUtil.isEmpty(System.getenv("debug-user-classes"));

    public static ClassLoader getClassLoader() {
        if (classLoader == null) {
            ClassLoader thread = Thread.currentThread().getContextClassLoader();
            if (thread == null) {
                return ClassFinderHelper.class.getClassLoader();
            }
            return thread;
        }
        return classLoader;
    }

    private static boolean skip(String path) {
        for (String prefix : nonUserPrefixes) {
            if (path.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private static ComplexCache<Collection<Class<?>>> userClassesCache = new CacheBuilder<Collection<Class<?>>>().normal().setValidator(new TimeValidator<>(2 * 60 * 1000)).setResolver((CacheValueResolver<Collection<Class<?>>>) cache -> {
        Collection<Class<?>> list = new ArrayList<>();
        new ClassPath(getClassLoader()).classes(resource -> {
            totalClassCount++;
            String name = resource.javaName();
            if (skip(name)) return;
            try {
                list.add(resource.load());
            } catch (UnsupportedClassVersionError ex) {
                logger.warn(name + " has an unsupported class version (" + ex.getMessage() + ")");
            } catch (Throwable ex) {
                new FailedToLoadClassException("Failed to load class '" + name + "' if you want to skip it use @SkipPath", ex).printStackTrace();
            }
        });
        return list;
    }).build();

    public static Set<String> getSkippedPaths() {
        return nonUserPrefixes;
    }

    public static class FailedToLoadClassException extends RuntimeException {

        public FailedToLoadClassException() {
        }

        public FailedToLoadClassException(String s) {
            super(s);
        }

        public FailedToLoadClassException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public FailedToLoadClassException(Throwable throwable) {
            super(throwable);
        }

        public FailedToLoadClassException(String s, Throwable throwable, boolean b, boolean b1) {
            super(s, throwable, b, b1);
        }
    }

    public static void registerSkippedPath(String path) {
        nonUserPrefixes.add(path);
        userClassesCache.clear();
    }

    public static Collection<Class<?>> getUserClasses() {
        return userClassesCache.getValue();
    }

}
