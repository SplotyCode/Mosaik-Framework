package io.github.splotycode.mosaik.domparsing.annotation;

import io.github.splotycode.mosaik.util.task.TaskExecutor;

import java.io.File;

public interface FileSystemProvider {

    FileSystem provide(File root, IEntryParser parser);

    FileSystem provide(String name, IEntryParser parser);

    FileSystem provideCashing(File root, IEntryParser parser, long compressDelay, long maxCompress, TaskExecutor taskExecutor);

    FileSystem provideCashing(String name, IEntryParser parser, long compressDelay, long maxCompress, TaskExecutor taskExecutor);

}
