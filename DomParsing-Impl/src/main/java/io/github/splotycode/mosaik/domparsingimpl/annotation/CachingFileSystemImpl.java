package io.github.splotycode.mosaik.domparsingimpl.annotation;

import io.github.splotycode.mosaik.domparsing.annotation.IEntryParser;
import io.github.splotycode.mosaik.runtime.application.Application;
import io.github.splotycode.mosaik.util.io.PathUtil;
import io.github.splotycode.mosaik.util.task.TaskExecutor;
import io.github.splotycode.mosaik.util.task.types.CompressingTask;

import java.io.File;
import java.util.*;

public class CachingFileSystemImpl<D> extends FileSystemImpl<D> {

    private Map<String, CachedFile> files = new HashMap<>();
    private final long compressDelay, maxCompress;
    private final TaskExecutor taskExecutor;

    public CachingFileSystemImpl(File root, IEntryParser entryParser, long compressDelay, long maxCompress, TaskExecutor taskExecutor) {
        super(root, entryParser);
        this.compressDelay = compressDelay;
        this.maxCompress = maxCompress;
        this.taskExecutor = taskExecutor;
        setup();
    }

    public CachingFileSystemImpl(String name, IEntryParser entryParser, long compressDelay, long maxCompress, TaskExecutor taskExecutor) {
        super(name, entryParser);
        this.compressDelay = compressDelay;
        this.maxCompress = maxCompress;
        this.taskExecutor = taskExecutor;
        setup();
    }

    private void setup() {
        Application.getGlobalShutdownManager().addLastTaskExecutor(taskExecutor);
        for (File file : root.listFiles()) {
            CachedFile cachedFile = new CachedFile(file);
            files.put(PathUtil.getFileNameWithoutEx(file), cachedFile);
            taskExecutor.runTask(cachedFile);
        }
    }

    @Override
    public void putEntry(String entryKey, D entry) {
        CachedFile file = files.get(entryKey);
        if (file == null) {
            file = new CachedFile(getFile(entryKey, true), entry);
            files.put(entryKey, file);
            taskExecutor.runTask(file);
        } else if (file.deleted) {
            file.deleted = false;
            file.obj = entry;
        } else if (file.obj != entry){
            file.obj = entry;
        }
        file.requestUpdate();
    }

    @Override
    public void deleteEntry(String key) {
        CachedFile file = files.get(key);
        file.deleted = true;
        file.requestUpdate();
    }

    @Override
    public D getEntry(String key) {
        return files.get(key).getObj();
    }

    @Override
    public Collection<D> getEntries() {
        List<D> entries = new ArrayList<>();
        for (CachedFile file : files.values()) {
            entries.add(file.getObj());
        }
        return entries;
    }

    public class CachedFile extends CompressingTask {

        private D obj;
        private String name;
        private boolean deleted = false;

        public CachedFile(File file) {
            super(compressDelay, maxCompress);
            name = PathUtil.getFileNameWithoutEx(file);
        }

        public CachedFile(File file, D obj) {
            super(compressDelay, maxCompress);
            this.obj = obj;
            name = PathUtil.getFileNameWithoutEx(file);
        }

        @Override
        public boolean execOnShutdown() {
            return true;
        }

        @Override
        public void run() {
            if (deleted) {
                deleted = false;
                CachingFileSystemImpl.super.deleteEntry(name);
            } else {
                CachingFileSystemImpl.super.putEntry(name, getObj());
            }
        }

        public D getObj() {
            if (obj == null) {
                obj = CachingFileSystemImpl.super.getEntry(name);
            }
            return obj;
        }

    }
}
