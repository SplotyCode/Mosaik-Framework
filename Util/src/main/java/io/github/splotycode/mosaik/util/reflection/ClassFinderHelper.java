package io.github.splotycode.mosaik.util.reflection;

import com.google.common.reflect.ClassPath;
import io.github.splotycode.mosaik.util.cache.complex.resolver.CacheValueResolver;
import io.github.splotycode.mosaik.util.cache.complex.validator.TimeValidator;
import lombok.Getter;
import io.github.splotycode.mosaik.util.cache.CacheBuilder;
import io.github.splotycode.mosaik.util.cache.complex.ComplexCache;
import io.github.splotycode.mosaik.util.logger.Logger;

import java.io.IOException;
import java.util.*;

public final class ClassFinderHelper {

    private static Logger logger = Logger.getInstance(ClassFinderHelper.class);

    private static Set<String> nonUserPrefixes = new HashSet<>();
    @Getter private static long totalClassCount;

    private static ComplexCache<Collection<Class<?>>> userClassesCache = new CacheBuilder<Collection<Class<?>>>().normal().setValidator(new TimeValidator<>(2 * 60 * 1000)).setResolver((CacheValueResolver<Collection<Class<?>>>) cache -> {
        Collection<Class<?>> list = new ArrayList<>();
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(Thread.currentThread().getContextClassLoader()).getAllClasses()) {
                totalClassCount++;
                boolean skip = false;
                String packageName = classInfo.getPackageName();
                for (String prefix : nonUserPrefixes) {
                    if (packageName.startsWith(prefix)) {
                        skip = true;
                        break;
                    }
                }
                if (skip) continue;
                try {
                    list.add(classInfo.load());
                } catch (UnsupportedClassVersionError ex) {
                    logger.warn(classInfo.getName() + " has an unsupported class version (" + ex.getMessage() + ")");
                }catch (Throwable ex) {
                    new FailedToLoadClassException("Failed to load class '" + classInfo.getName() + "'if you want to skip it use @SkipPath", ex).printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
