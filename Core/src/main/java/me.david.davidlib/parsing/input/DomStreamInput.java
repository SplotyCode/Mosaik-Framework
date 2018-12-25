package me.david.davidlib.parsing.input;

import lombok.AllArgsConstructor;
import me.david.davidlib.parsing.DomSourceType;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
public class DomStreamInput implements DomInput {

    private InputStream stream;

    @Override
    public byte[] getBytes() {
        try {
            return IOUtils.toByteArray(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getString() {
        try {
            return IOUtils.toString(stream, "UTF-8");
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
