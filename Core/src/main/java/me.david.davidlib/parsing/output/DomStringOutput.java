package me.david.davidlib.parsing.output;

import me.david.davidlib.io.ByteArrayInputStream;
import me.david.davidlib.parsing.DomSourceType;
import me.david.davidlib.parsing.output.DomOutput;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DomStringOutput implements DomOutput {

    private String string;

    @Override
    public void writeFile(File file) {
        if (!file.getParentFile().exists()) file.mkdirs();
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            IOUtils.write(getBytes(), new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getString() {
        return string;
    }

    @Override
    public byte[] getBytes() {
        return string.getBytes();
    }

    @Override
    public InputStream getStream() {
        return new ByteArrayInputStream(getBytes());
    }

    @Override
    public DomSourceType getSourceType() {
        return DomSourceType.STRING;
    }
}
