package io.github.splotycode.mosaik.runtime.startup;

public interface ClassLoaderProvider {

    enum DefaultClassLoaderProvider implements ClassLoaderProvider {

        INSTANCE;

        @Override
        public ClassLoader getClassLoader() {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            return classLoader == null ? DefaultClassLoaderProvider.class.getClassLoader() : classLoader;
        }
    }

    ClassLoader getClassLoader();

}
