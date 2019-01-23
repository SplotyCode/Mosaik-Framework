package io.github.splotycode.mosaik.domparsing.parsing.input;

import io.github.splotycode.mosaik.domparsing.parsing.DomSourceType;
import lombok.Getter;
import io.github.splotycode.mosaik.util.io.IOUtil;

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
