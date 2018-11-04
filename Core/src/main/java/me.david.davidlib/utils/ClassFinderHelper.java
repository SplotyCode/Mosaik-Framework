package me.david.davidlib.utils;

import com.google.common.reflect.ClassPath;
import me.david.davidlib.cache.Cache;
import me.david.davidlib.cache.CacheBuilder;
import me.david.davidlib.cache.complex.ComplexCache;
import me.david.davidlib.cache.complex.resolver.CacheValueResolver;
import me.david.davidlib.cache.complex.validator.CacheValidator;
import me.david.davidlib.cache.complex.validator.TimeValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public final class ClassFinderHelper {

    private static String[] nonUserPrefixes = new String[] {
            "org.eclipse",
            "javax",
            "javafx",
            "sun",
            "org.jboss",
            "java",
            "com.google",
            "io.netty",
            "lombok",
            "org.apache",
            "com.sun",
            "com.oracle",
            "org.checkerframework",
            "jdk",
            "org.ietf",
            "org.omg",
            "org.w3c",
            "org.xml",
            "com.intellij",
            "org.jcp",
            "org.classpath",
            "org.GNOME",
            "org.codehaus",
            "netscape.javascript"
    };

    private static ComplexCache<Collection<Class<?>>> userClassesCache = new CacheBuilder<Collection<Class<?>>>().normal().setValidator(new TimeValidator<>(2 * 60 * 1000)).setResolver((CacheValueResolver<Collection<Class<?>>>) cache -> {
        Collection<Class<?>> list = new ArrayList<>();
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(Thread.currentThread().getContextClassLoader()).getAllClasses()) {
                boolean skip = false;
                String packageName = classInfo.getPackageName();
                for (String prefix : nonUserPrefixes) {
                    if (packageName.startsWith(prefix)) {
                        skip = true;
                        break;
                    }
                }
                if (skip) continue;
                list.add(classInfo.load());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }).build();

    public static Collection<Class<?>> getUserClasses() {
        return userClassesCache.getValue();
    }

}
