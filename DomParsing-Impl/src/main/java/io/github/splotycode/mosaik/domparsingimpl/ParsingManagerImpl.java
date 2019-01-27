package io.github.splotycode.mosaik.domparsingimpl;

import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.io.FileUtil;
import lombok.Getter;
import io.github.splotycode.mosaik.domparsing.parsing.ParsingHandle;
import io.github.splotycode.mosaik.domparsing.parsing.ParsingManager;
import io.github.splotycode.mosaik.domparsing.parsing.input.DomFileInput;
import io.github.splotycode.mosaik.domparsing.parsing.input.DomInput;
import io.github.splotycode.mosaik.domparsing.parsing.input.DomStreamInput;
import io.github.splotycode.mosaik.domparsing.parsing.input.DomUrlInput;
import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.io.PathUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ParsingManagerImpl implements ParsingManager {

    @Getter private List<ParsingHandle> handles = new ArrayList<>();

    @Override
    public <P extends ParsingHandle> P getHandleByClass(Class<P> clazz) {
        for (ParsingHandle handle : handles) {
            if (clazz == handle.getClass()) {
                return (P) handle;
            }
        }
        throw new NoHandleFoundException(clazz.getName() + " is not in " + StringUtil.join(handles, obj -> obj.getClass().getName(), ", "));
    }

    @Override
    public <P extends Document> P parseDocument(DomInput input, ParsingHandle<P> handle) {
        return handle.getParser(input).parse(input);
    }

    @Override
    public <P extends Document> P parseDocument(DomInput input, Class<? extends ParsingHandle<P>> handleClazz) {
        return parseDocument(input, getHandleByClass(handleClazz));
    }

    @Override
    public Document parseDocument(File file) {
        DomInput input = new DomFileInput(file);
        ParsingHandle handle = handles.stream().filter(cHandle -> PathUtil.extensionEquals(file.getName(), cHandle.getFileTypes())).findFirst().orElseThrow(NoHandleFoundException::new);
        return parseDocument(input, handle);
    }

    @Override
    public <P extends Document> P parseDocument(File file, ParsingHandle<P> handle) {
        return parseDocument(new DomFileInput(file), handle);
    }

    @Override
    public <P extends Document> P parseDocument(File file, Class<? extends ParsingHandle<P>> handle) {
        return parseDocument(new DomFileInput(file), handle);
    }

    @Override
    public Document parseDocument(URL url) {
        DomUrlInput input = new DomUrlInput(url);
        ParsingHandle handle = handles.stream().filter(cHandle -> {
            try {
                return ArrayUtil.contains(cHandle.getMimeTypes(), input.getConnection().getContentType());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }).findFirst().orElseThrow(NoHandleFoundException::new);
        return parseDocument(input, handle);
    }

    @Override
    public <P extends Document> P parseDocument(URL url, ParsingHandle<P> handle) {
        return parseDocument(new DomUrlInput(url), handle);
    }

    @Override
    public <P extends Document> P parseDocument(URL url, Class<? extends ParsingHandle<P>> handle) {
        return parseDocument(new DomUrlInput(url), handle);
    }

    @Override
    public Document parseResourceFile(String file) {
        DomInput input = new DomStreamInput(ParsingManagerImpl.class.getResourceAsStream(file));
        ParsingHandle handle = handles.stream().filter(cHandle -> PathUtil.extensionEquals(file, cHandle.getFileTypes())).findFirst().orElseThrow(NoHandleFoundException::new);
        return parseDocument(input, handle);
    }

    @Override
    public <P extends Document> P parseDocument(String file, ParsingHandle<P> handle) {
        return parseDocument(new DomStreamInput(ParsingManagerImpl.class.getResourceAsStream(file)), handle);
    }

    @Override
    public <P extends Document> P parseDocument(String file, Class<? extends ParsingHandle<P>> handle) {
        return parseDocument(new DomStreamInput(ParsingManagerImpl.class.getResourceAsStream(file)), handle);
    }

    @Override
    public <D extends Document> String writeToText(D document, ParsingHandle<D> handle) {
        return handle.getWriter().toText(document);
    }

    @Override
    public <D extends Document> String writeToText(D document, Class<? extends ParsingHandle<D>> handleClazz) {
        return writeToText(document, getHandleByClass(handleClazz));
    }

    @Override
    public <D extends Document> void writeToFile(D document, File file, ParsingHandle<D> handle) throws IOException {
        FileUtil.writeToFile(file, writeToText(document, handle));
    }

    @Override
    public <D extends Document> void writeToFile(D document, File file, Class<? extends ParsingHandle<D>> handleClazz) throws IOException {
        FileUtil.writeToFile(file, writeToText(document, handleClazz));
    }

    @Override
    public <D extends Document> void writeToFile(D document, File file) throws IOException {
        writeToFile(document, file, document.getHandle());
    }

    @Override
    public Collection<ParsingHandle> getList() {
        return handles;
    }

    @Override
    public Class<ParsingHandle> getObjectClass() {
        return ParsingHandle.class;
    }

    public static class NoHandleFoundException extends RuntimeException {

        public NoHandleFoundException() {
        }

        public NoHandleFoundException(String s) {
            super(s);
        }

        public NoHandleFoundException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public NoHandleFoundException(Throwable throwable) {
            super(throwable);
        }

        public NoHandleFoundException(String s, Throwable throwable, boolean b, boolean b1) {
            super(s, throwable, b, b1);
        }
    }

}
