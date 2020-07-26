package io.github.splotycode.mosaik.util.reflection.classpath;

public interface ClassLoaderSupport {

    boolean supports(ClassLoader classLoader);

    boolean load(ClassPath.Loader classPathLoader, ClassLoader classLoader, ClassPathVisitor visitor);

}
