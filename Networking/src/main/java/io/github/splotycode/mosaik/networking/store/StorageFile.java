package io.github.splotycode.mosaik.networking.store;

import java.io.RandomAccessFile;

public interface StorageFile {

    RandomAccessFile getFile();

    void lock();
    void unLock();

    default RandomAccessFile getAndLock() {
        lock();
        return getFile();
    }


}
