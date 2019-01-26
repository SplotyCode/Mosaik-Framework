package io.github.splotycode.mosaik.domparsing.annotation;

import java.io.File;
import java.util.Collection;

public interface FileSystem<D> {

    File getRoot();
    Class<D> getEntryClass();

    D getEntry(String key);

    D getEntry(String fileKey, D def);

    void deleteEntry(String key);

    void putEntry(String key, D entry);

    Collection<D> getEntries();

}
