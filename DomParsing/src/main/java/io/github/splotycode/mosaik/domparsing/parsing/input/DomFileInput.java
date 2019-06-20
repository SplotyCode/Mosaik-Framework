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
        } catch (Throwable e) {
            throw new DomInputException("Could not convert stream to byte array", e);
        }
    }

    @Override
    public String getString() {
        try {
            return IOUtil.loadText(getStream());
        } catch (Throwable e) {
            throw new DomInputException("Could not convert stream to text", e);
        }
    }

    @Override
    public InputStream getStream() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new DomInputException("Could not find file" + file.getAbsolutePath(), e);
        }
    }

    @Override
    public DomSourceType getType() {
        return DomSourceType.FILE;
    }
}
