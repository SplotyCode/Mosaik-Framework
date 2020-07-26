package io.github.splotycode.mosaik.util.io.resource;

import io.github.splotycode.mosaik.util.cache.LazyLoad;
import io.github.splotycode.mosaik.util.io.PathUtil;
import io.github.splotycode.mosaik.util.io.buffer.BufferProvider;
import lombok.Getter;
import lombok.SneakyThrows;

public class ClassPathResource extends InputStreamResource {
    @Getter private final ClassLoader loader;
    @Getter private final String path;

    private LazyLoad<Class<?>> clazz = new LazyLoad<Class<?>>() {
        @Override
        @SneakyThrows
        protected Class<?> initialize() {
            return loader.loadClass(javaName());
        }
    };

    public ClassPathResource(ClassLoader loader, String path) {
        this(AbstractResource.DEFAULT_INTERNAL_BUFFER_PROVIDER, AvailableTrust.UNSURE,
                loader, path);
    }

    public ClassPathResource(BufferProvider bufferProvider, AvailableTrust trustAvailableEstimation,
                             ClassLoader loader, String path) {
        super(bufferProvider, trustAvailableEstimation);
        this.loader = loader;
        this.path = path;
    }

    public boolean isClass() {
        return path.endsWith(".class");
    }

    public boolean isResource() {
        return !isClass();
    }

    public String javaName() {
        return PathUtil.getFileNameWithoutEx(path).replace('/', '.');
    }

    public String name() {
        return PathUtil.getFileName(path);
    }

    public boolean inPackage(String path) {
        return this.path.startsWith(path);
    }

    public Class loadClass() {
        return clazz.getValue();
    }
}
