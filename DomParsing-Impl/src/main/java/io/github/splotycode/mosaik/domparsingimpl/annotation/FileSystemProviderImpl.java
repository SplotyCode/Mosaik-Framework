package io.github.splotycode.mosaik.domparsingimpl.annotation;

import io.github.splotycode.mosaik.domparsing.annotation.FileSystem;
import io.github.splotycode.mosaik.domparsing.annotation.FileSystemProvider;

import java.io.File;

public class FileSystemProviderImpl implements FileSystemProvider {

    @Override
    public <P> FileSystem<P> provide(File root, Class<P> clazz) {
        return new FileSystemImpl<>(root, clazz);
    }

    @Override
    public <P> FileSystem<P> provide(Class<P> clazz) {
        return new FileSystemImpl<>(clazz);
    }

}
