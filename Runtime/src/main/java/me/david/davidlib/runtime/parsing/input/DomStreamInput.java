package me.david.davidlib.runtime.parsing.input;

import lombok.AllArgsConstructor;
import me.david.davidlib.runtime.parsing.DomSourceType;
import me.david.davidlib.util.io.Charsets;
import me.david.davidlib.util.io.IOUtil;

import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
public class DomStreamInput implements DomInput {

    private InputStream stream;

    @Override
    public byte[] getBytes() {
        try {
            return IOUtil.toByteArray(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getString() {
        try {
            return IOUtil.loadText(stream, Charsets.UTF8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
