package io.github.splotycode.mosaik.domparsing.parsing.output;

import io.github.splotycode.mosaik.domparsing.parsing.DomSourceType;
import io.github.splotycode.mosaik.util.io.ByteArrayInputStream;
import io.github.splotycode.mosaik.util.io.FileUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.io.InputStream;

@AllArgsConstructor
@Getter
public class DomStringOutput implements DomOutput {

    private String string;

    @Override
    public void writeFile(File file) {
        try {
            FileUtil.writeToFile(file, string);
        } catch (Throwable e) {
            throw new DomOutputException("Could not write string to file", e);
        }
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
