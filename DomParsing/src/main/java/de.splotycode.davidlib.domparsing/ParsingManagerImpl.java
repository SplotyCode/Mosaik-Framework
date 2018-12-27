package de.splotycode.davidlib.domparsing;

import lombok.Getter;
import me.david.davidlib.link.ParsingManager;
import me.david.davidlib.parsing.ParsingHandle;
import me.david.davidlib.parsing.input.DomFileInput;
import me.david.davidlib.parsing.input.DomInput;
import me.david.davidlib.parsing.input.DomStreamInput;
import me.david.davidlib.parsing.input.DomUrlInput;
import me.david.davidlib.storage.Document;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;

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
        ParsingHandle handle = handles.stream().filter(cHandle -> FilenameUtils.isExtension(file.getName(), cHandle.getFileTypes())).findFirst().orElseThrow(NoHandleFound::new);
        return parseDocument(input, handle);
    }

    @Override
    public Document parseDocument(URL url) {
        DomUrlInput input = new DomUrlInput(url);
        ParsingHandle handle = handles.stream().filter(cHandle -> {
            try {
                return ArrayUtils.contains(cHandle.getMimeTypes(), input.getConnection().getContentType());
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
        ParsingHandle handle = handles.stream().filter(cHandle -> FilenameUtils.isExtension(file, cHandle.getFileTypes())).findFirst().orElseThrow(NoHandleFound::new);
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
