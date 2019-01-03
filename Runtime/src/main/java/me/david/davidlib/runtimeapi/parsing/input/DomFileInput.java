package me.david.davidlib.runtimeapi.parsing.input;

import lombok.Getter;
import me.david.davidlib.runtimeapi.parsing.DomSourceType;
import me.david.davidlib.util.io.Charsets;
import me.david.davidlib.util.io.IOUtil;

import java.io.*;

@Getter
public class DomFileInput implements DomInput {

    protected File file;

    public DomFileInput(File file) {
        this.file = file;
    }

    @Override
    public byte[] getBytes() {
        try {
            return IOUtil.toByteArray(getStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getString() {
        try {
            return IOUtil.loadText(getStream());
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
