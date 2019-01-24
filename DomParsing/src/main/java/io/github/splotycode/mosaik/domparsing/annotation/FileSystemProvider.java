package io.github.splotycode.mosaik.domparsing.annotation;

import java.io.File;

public interface FileSystemProvider {

    <P> FileSystem<P> provide(File root, Class<P> clazz);

    <P> FileSystem<P> provide(Class<P> clazz);

}
