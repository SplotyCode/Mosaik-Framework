package io.github.splotycode.mosaik.networking.store;

public interface FileProvider {

    Iterable<StorageFile> allIndexFiles();
    Iterable<StorageFile> allBlockFiles();

    StorageFile getIndex(int bucket, int hash);
    StorageFile getBlock(long block);

}
