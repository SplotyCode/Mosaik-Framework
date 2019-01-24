package io.github.splotycode.mosaik.domparsingimpl.annotation;

import io.github.splotycode.mosaik.domparsing.annotation.DomEntry;
import io.github.splotycode.mosaik.domparsing.annotation.FileSystem;
import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.domparsing.dom.Node;
import io.github.splotycode.mosaik.domparsing.parsing.ParsingManager;
import io.github.splotycode.mosaik.domparsingimpl.readers.keyvalue.KeyValueHandle;
import io.github.splotycode.mosaik.domparsingimpl.readers.keyvalue.dom.KeyNode;
import io.github.splotycode.mosaik.domparsingimpl.readers.keyvalue.dom.KeyValueDocument;
import io.github.splotycode.mosaik.domparsingimpl.readers.keyvalue.dom.ValueNode;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.util.io.FileUtil;
import io.github.splotycode.mosaik.util.io.PathUtil;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class FileSystemImpl<D> implements FileSystem<D> {

    private static Map<String, EntryData> data = new HashMap<>();

    @Getter private File root;
    @Getter private Class<D> entryClass;

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

    public static EntryData getData(Class clazz) {
        EntryData entry = data.get(clazz.getName());
        if (entry == null) {
            entry = new EntryData(clazz);
            data.put(clazz.getName(), entry);
        }
        return entry;
    }

    @Override
    public D getEntry(String fileKey, D def) {
        try {
            File file = new File(root, fileKey + ".kv");
            if (!file.exists()) return def;

            EntryData entryData = getData(entryClass);
            Document document = LinkBase.getInstance().getLink(Links.PARSING_MANAGER).parseDocument(file);

            D entry = entryClass.newInstance();

            for (Node node : document.getNodes()) {
                String key = node.name();
                String value = document.getNode(key).name();

                Field field = entry.getClass().getDeclaredField(entryData.getFieldName(key));
                field.setAccessible(true);
                field.set(entry, LinkBase.getInstance().getLink(TransformerManager.LINK).transform(value, field.getType()));
            }
            return entry;
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return def;
        }
    }

    @Override
    public void putEntry(String entryKey, D entry) {
        try {
            File file = new File(root, entryKey + ".kv");
            ParsingManager parsingManager = LinkBase.getInstance().getLink(Links.PARSING_MANAGER);
            KeyValueDocument document = new KeyValueDocument();
            for (Map.Entry<String, String> node : getData(entryClass).entrySet()) {
                KeyNode key = new KeyNode(node.getKey());
                Field field = entry.getClass().getDeclaredField(node.getValue());
                field.setAccessible(true);
                document.installKeyValue(key, new ValueNode(key, LinkBase.getInstance().getLink(TransformerManager.LINK).transform(field.get(entry), String.class)));
            }
            parsingManager.writeToFile(document, file, KeyValueHandle.class);
        } catch (ReflectiveOperationException | IOException ex) {
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
