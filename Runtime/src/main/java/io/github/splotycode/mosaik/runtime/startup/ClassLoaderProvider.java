package io.github.splotycode.mosaik.runtime.startup;

public interface ClassLoaderProvider {

    class DefaultClassLoaderProvider implements ClassLoaderProvider {

        @Override
        public ClassLoader getClassLoader() {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            return classLoader == null ? DefaultClassLoaderProvider.class.getClassLoader() : classLoader;
        }
    }

    ClassLoader getClassLoader();

}
