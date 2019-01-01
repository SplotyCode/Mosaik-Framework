package me.david.davidlib.parsing.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.david.davidlib.parsing.DomSourceType;
import me.david.davidlib.utils.io.ByteArrayInputStream;
import me.david.davidlib.utils.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
@Getter
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
            FileUtil.writeToFile(file, string);
        } catch (IOException e) {
            e.printStackTrace();
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
