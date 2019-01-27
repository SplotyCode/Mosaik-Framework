package io.github.splotycode.mosaik.domparsingimpl.annotation;

import io.github.splotycode.mosaik.domparsing.annotation.FileSystem;
import io.github.splotycode.mosaik.domparsing.annotation.IEntryParser;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.util.io.FileUtil;
import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileSystemImpl<D> implements FileSystem<D> {

    @Getter protected File root;
    @Getter protected IEntryParser entryParser;

    public FileSystemImpl(String name, IEntryParser entryParser) {
        this(new File(LinkBase.getInstance().getLink(Links.PATH_MANAGER).getMainDirectory(), "save/" + name), entryParser);
    }

    public FileSystemImpl(File root, IEntryParser entryParser) {
        FileUtil.createDirectory(root);
        this.root = root;
        this.entryParser = entryParser;
    }

    @Override
    public D getEntry(String key) {
        return getEntry(key, null);
    }

    private D getEntry(File file, D def) {
        try {
            if (!file.exists()) return def;
            return (D) entryParser.toObject(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return def;
        }
    }

    @Override
    public D getEntry(String fileKey, D def) {
        return getEntry(new File(root, fileKey + ".kv"), def);
    }

    @Override
    public void deleteEntry(String key) {
        File file = new File(root, key + ".kv");
        FileUtil.delete(file);
    }

    @Override
    public void putEntry(String entryKey, D entry) {
        try {
            File file = new File(root, entryKey + ".kv");
            FileUtil.writeToFile(file, entryParser.fromObject(entry));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Collection<D> getEntries() {
        List<D> entries = new ArrayList<>();
        for (File file : root.listFiles()) {
            entries.add(getEntry(file, null));
        }
        return entries;
    }

}
