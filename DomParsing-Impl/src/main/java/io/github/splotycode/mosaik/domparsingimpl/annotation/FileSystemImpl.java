package io.github.splotycode.mosaik.domparsingimpl.annotation;

import io.github.splotycode.mosaik.domparsing.annotation.FileSystem;
import io.github.splotycode.mosaik.domparsing.annotation.IEntryParser;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.util.io.FileUtil;
import io.github.splotycode.mosaik.util.io.PathUtil;
import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
            return (D) entryParser.toObject(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            return def;
        }
    }

    @Override
    public D getEntry(String fileKey, D def) {
        return getEntry(getFile(fileKey, false), def);
    }

    protected File getFile(String key, boolean validate) {
        if (!validate || PathUtil.validAndNoUpwardTravel(root, key)) {
            return new File(root, key + ".kv");
        }
        throw new IllegalArgumentException("Invalid key");
    }

    @Override
    public void deleteEntry(String key) {
        FileUtil.delete(getFile(key, false));
    }

    @Override
    public void putEntry(String entryKey, D entry) {
        FileUtil.writeToFile(getFile(entryKey, true), entryParser.fromObject(entry));
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
