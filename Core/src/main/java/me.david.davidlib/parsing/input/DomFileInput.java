package me.david.davidlib.parsing.input;

import lombok.Getter;
import me.david.davidlib.parsing.DomSourceType;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;

@Getter
public class DomFileInput implements DomInput {

    protected File file;

    public DomFileInput(File file) {
        this.file = file;
    }

    @Override
    public byte[] getBytes() {
        try {
            return IOUtils.toByteArray(getStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getString() {
        try {
            return IOUtils.toString(getStream(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public InputStream getStream() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DomSourceType getType() {
        return DomSourceType.FILE;
    }
}
