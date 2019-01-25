package io.github.splotycode.mosaik.domparsingimpl.annotation;

import io.github.splotycode.mosaik.util.io.PathUtil;
import io.github.splotycode.mosaik.util.task.TaskExecutor;
import io.github.splotycode.mosaik.util.task.types.CompressingTask;

import java.io.File;
import java.util.*;

public class CachingFileSystemImpl<D> extends FileSystemImpl<D> {

    private Map<String, CachedFile> files = new HashMap<>();
    private final long compressDelay;
    private final TaskExecutor taskExecutor;

    public CachingFileSystemImpl(Class<D> entryClass, long compressDelay, TaskExecutor taskExecutor) {
        super(entryClass);
        this.compressDelay = compressDelay;
        this.taskExecutor = taskExecutor;
        setup();
    }

    public CachingFileSystemImpl(File root, Class<D> entryClass, long compressDelay, TaskExecutor taskExecutor) {
        super(root, entryClass);
        this.compressDelay = compressDelay;
        this.taskExecutor = taskExecutor;
        setup();
    }

    private void setup() {
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
            file = new CachedFile(new File(root, entryKey + ".kv"), entry);
            files.put(entryKey, file);
            taskExecutor.runTask(file);
        }
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

        public CachedFile(File file) {
            super(compressDelay);
            name = PathUtil.getFileNameWithoutEx(file);
        }

        public CachedFile(File file, D obj) {
            super(compressDelay);
            this.obj = obj;
            name = PathUtil.getFileNameWithoutEx(file);
        }

        @Override
        public void run() {
            CachingFileSystemImpl.super.putEntry(name, getObj());
        }

        public D getObj() {
            if (obj == null) {
                obj = CachingFileSystemImpl.super.getEntry(name);
            }
            return obj;
        }
    }
}
