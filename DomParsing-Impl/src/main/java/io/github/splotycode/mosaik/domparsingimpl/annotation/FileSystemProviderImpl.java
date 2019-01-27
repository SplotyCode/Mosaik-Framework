package io.github.splotycode.mosaik.domparsingimpl.annotation;

import io.github.splotycode.mosaik.domparsing.annotation.FileSystem;
import io.github.splotycode.mosaik.domparsing.annotation.FileSystemProvider;
import io.github.splotycode.mosaik.domparsing.annotation.IEntryParser;
import io.github.splotycode.mosaik.util.task.TaskExecutor;

import java.io.File;

public class FileSystemProviderImpl implements FileSystemProvider {

    @Override
    public FileSystem provide(File root, IEntryParser entryParser) {
        return new FileSystemImpl<>(root, entryParser);
    }

    @Override
    public FileSystem provide(String name, IEntryParser entryParser) {
        return new FileSystemImpl<>(name, entryParser);
    }

    @Override
    public FileSystem provideCashing(String name, IEntryParser entryParser, long compressDelay, long maxCompress, TaskExecutor taskExecutor) {
        return new CachingFileSystemImpl<>(name, entryParser, compressDelay, maxCompress, taskExecutor);
    }

    @Override
    public FileSystem provideCashing(File root, IEntryParser entryParser, long compressDelay, long maxCompress, TaskExecutor taskExecutor) {
        return new CachingFileSystemImpl<>(root, entryParser, compressDelay, maxCompress, taskExecutor);
    }

}
