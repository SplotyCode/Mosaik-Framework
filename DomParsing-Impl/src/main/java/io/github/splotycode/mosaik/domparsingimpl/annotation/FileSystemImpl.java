package io.github.splotycode.mosaik.domparsingimpl.annotation;

import io.github.splotycode.mosaik.domparsing.annotation.DomEntry;
import io.github.splotycode.mosaik.domparsing.annotation.FileSystem;
import io.github.splotycode.mosaik.domparsing.annotation.IEntry;
import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.domparsing.parsing.ParsingManager;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.util.io.FileUtil;
import io.github.splotycode.mosaik.util.io.PathUtil;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileSystemImpl<D extends IEntry> implements FileSystem<D> {

    @Getter protected File root;
    @Getter protected Class<D> entryClass;

    public FileSystemImpl(Class<D> entryClass) {
        this(new File(LinkBase.getInstance().getLink(Links.PATH_MANAGER).getMainDirectory(), "save/" + entryClass.getAnnotation(DomEntry.class).value() + "/"), entryClass);
    }

    public FileSystemImpl(File root, Class<D> entryClass) {
        FileUtil.createDirectory(root);
        this.root = root;
        this.entryClass = entryClass;
    }

    @Override
    public D getEntry(String key) {
        return getEntry(key, null);
    }

    @Override
    public D getEntry(String fileKey, D def) {
        try {
            File file = new File(root, fileKey + ".kv");
            if (!file.exists()) return def;

            Document document = LinkBase.getInstance().getLink(Links.PARSING_MANAGER).parseDocument(file);

            D entry = entryClass.newInstance();

           entry.write(document);
            return entry;
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return def;
        }
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
            ParsingManager parsingManager = LinkBase.getInstance().getLink(Links.PARSING_MANAGER);
            parsingManager.writeToFile(entry.read(), file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Collection<D> getEntries() {
        List<D> entries = new ArrayList<>();
        for (File file : root.listFiles()) {
            entries.add(getEntry(PathUtil.getFileNameWithoutEx(file.getName())));
        }
        return entries;
    }

}
