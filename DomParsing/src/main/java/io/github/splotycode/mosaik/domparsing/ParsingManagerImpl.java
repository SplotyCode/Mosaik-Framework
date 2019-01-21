package io.github.splotycode.mosaik.domparsing;

import lombok.Getter;
import io.github.splotycode.mosaik.runtime.parsing.ParsingHandle;
import io.github.splotycode.mosaik.runtime.parsing.ParsingManager;
import io.github.splotycode.mosaik.runtime.parsing.input.DomFileInput;
import io.github.splotycode.mosaik.runtime.parsing.input.DomInput;
import io.github.splotycode.mosaik.runtime.parsing.input.DomStreamInput;
import io.github.splotycode.mosaik.runtime.parsing.input.DomUrlInput;
import io.github.splotycode.mosaik.runtime.storage.Document;
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
    public Document parseDocument(DomInput input, ParsingHandle handle) {
        return handle.getParser(input).parse(input);
    }

    @Override
    public Document parseDocument(File file) {
        DomInput input = new DomFileInput(file);
        ParsingHandle handle = handles.stream().filter(cHandle -> PathUtil.extensionEquals(file.getName(), cHandle.getFileTypes())).findFirst().orElseThrow(NoHandleFound::new);
        return parseDocument(input, handle);
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
        }).findFirst().orElseThrow(NoHandleFound::new);
        return parseDocument(input, handle);
    }

    @Override
    public Document parseResourceFile(String file) {
        DomInput input = new DomStreamInput(ParsingManagerImpl.class.getResourceAsStream(file));
        ParsingHandle handle = handles.stream().filter(cHandle -> PathUtil.extensionEquals(file, cHandle.getFileTypes())).findFirst().orElseThrow(NoHandleFound::new);
        return parseDocument(input, handle);
    }

    @Override
    public Collection<ParsingHandle> getList() {
        return handles;
    }

    @Override
    public Class<ParsingHandle> getObjectClass() {
        return ParsingHandle.class;
    }

    public static class NoHandleFound extends RuntimeException {

        public NoHandleFound() {
        }

        public NoHandleFound(String s) {
            super(s);
        }

        public NoHandleFound(String s, Throwable throwable) {
            super(s, throwable);
        }

        public NoHandleFound(Throwable throwable) {
            super(throwable);
        }

        public NoHandleFound(String s, Throwable throwable, boolean b, boolean b1) {
            super(s, throwable, b, b1);
        }
    }

}
