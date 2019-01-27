package io.github.splotycode.mosaik.domparsing.annotation;

import io.github.splotycode.mosaik.util.task.TaskExecutor;

import java.io.File;

public interface FileSystemProvider {

    <P extends IEntry> FileSystem<P> provide(File root, Class<P> clazz);

    <P extends IEntry> FileSystem<P> provide(Class<P> clazz);

    <P extends IEntry> FileSystem<P> provideCashing(File root, Class<P> clazz, long compressDelay, long maxCompress, TaskExecutor taskExecutor);

    <P extends IEntry> FileSystem<P> provideCashing(Class<P> clazz, long compressDelay, long maxCompress, TaskExecutor taskExecutor);

}
