package io.github.splotycode.mosaik.domparsing.annotation;

import java.io.File;

public interface FileSystemProvider {

    <P extends DomResolver> FileSystem<P> provide(File root, Class<P> clazz);

}
