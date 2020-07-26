package io.github.splotycode.mosaik.runtime.startup;

public interface ClassLoaderProvider {
    enum DefaultClassLoaderProvider implements ClassLoaderProvider {
        INSTANCE;

        @Override
        public ClassLoader[] getClassLoaders() {
            ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
            ClassLoader appClassLoader = DefaultClassLoaderProvider.class.getClassLoader();
            if (threadClassLoader == null || threadClassLoader == appClassLoader) {
                return new ClassLoader[] {appClassLoader};
            }
            return new ClassLoader[] {threadClassLoader, appClassLoader};
        }
    }

    ClassLoader[] getClassLoaders();
}
