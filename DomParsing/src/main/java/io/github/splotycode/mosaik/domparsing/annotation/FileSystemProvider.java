package io.github.splotycode.mosaik.domparsing.annotation;

import io.github.splotycode.mosaik.util.task.TaskExecutor;

import java.io.File;

public interface FileSystemProvider {

    <P> FileSystem<P> provide(File root, Class<P> clazz);

    <P> FileSystem<P> provide(Class<P> clazz);

    <P> FileSystem<P> provideCashing(File root, Class<P> clazz, long compressDelay, TaskExecutor taskExecutor);

    <P> FileSystem<P> provideCashing(Class<P> clazz, long compressDelay, TaskExecutor taskExecutor);

}
