package io.github.splotycode.mosaik.util.reflection.classpath;

import io.github.splotycode.mosaik.util.ExceptionUtil;
import io.github.splotycode.mosaik.util.io.FileUtil;
import io.github.splotycode.mosaik.util.io.PathUtil;
import lombok.Getter;

import java.io.File;

public class Resource {

    @Getter
    private final String path;
    @Getter
    private final ClassLoader loader;
    private Class<?> clazz;

    public Resource(String path, ClassLoader loader) {
        this.path = path;
        this.loader = loader;
    }

    public boolean isClass() {
        return path.endsWith(".class");
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

    public void export(File file) {
        FileUtil.copyResource(path, file, loader);
    }

    public Class<?> load() {
        if (clazz == null) {
            try {
                clazz = loader.loadClass(javaName());
           } catch (ClassNotFoundException e) {
                ExceptionUtil.throwRuntime(e);
            }
        }
        return clazz;
    }
}
