package me.david.webapi.response.content;

import lombok.AllArgsConstructor;
import me.david.webapi.response.content.manipulate.ManipulateableContent;
import me.david.webapi.response.content.manipulate.ResponseManipulator;
import me.david.webapi.response.content.manipulate.StringManipulator;
import org.apache.commons.io.IOUtils;

import java.io.*;

@AllArgsConstructor
public class FileResponseContent implements ManipulateableContent {

    private File file;
    private StringManipulator manipulator;
    private String encoding;

    public FileResponseContent(String file) {
        this(file, "UTF-8");
    }

    public FileResponseContent(File file) {
        this(file, "UTF-8");
    }

    public FileResponseContent(String file, String encoding) {
        this(new File(file), encoding);
    }

    public FileResponseContent(File file, String encoding) {
        this.encoding = encoding;
        this.file = file;
        try {
            manipulator = new StringManipulator(IOUtils.toString(new FileInputStream(this.file), encoding));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return IOUtils.toInputStream(manipulator.getResult(), encoding);
    }

    @Override
    public ResponseManipulator manipulate() {
        return manipulator;
    }
}
