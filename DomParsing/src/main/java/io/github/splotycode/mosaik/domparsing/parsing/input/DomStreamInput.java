package io.github.splotycode.mosaik.domparsing.parsing.input;

import io.github.splotycode.mosaik.domparsing.parsing.DomSourceType;
import io.github.splotycode.mosaik.util.io.IOUtil;
import lombok.AllArgsConstructor;

import java.io.InputStream;

@AllArgsConstructor
public class DomStreamInput implements DomInput {

    private InputStream stream;

    @Override
    public byte[] getBytes() {
        try {
            return IOUtil.toByteArray(stream);
        } catch (Throwable e) {
            throw new DomInputException("Could not convert stream to byte array", e);
        }
    }

    @Override
    public String getString() {
        try {
            return IOUtil.loadText(stream);
        } catch (Throwable e) {
            throw new DomInputException("Could not load text from stream", e);
        }
    }

    @Override
    public InputStream getStream() {
        return stream;
    }

    @Override
    public DomSourceType getType() {
        return DomSourceType.STREAM;
    }
}
