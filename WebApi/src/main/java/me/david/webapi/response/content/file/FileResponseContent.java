package me.david.webapi.response.content.file;

import lombok.AllArgsConstructor;
import me.david.davidlib.io.ByteArrayInputStream;
import me.david.webapi.response.content.manipulate.ManipulateableContent;
import me.david.webapi.response.content.manipulate.ResponseManipulator;
import me.david.webapi.response.content.manipulate.StringManipulator;
import me.david.webapi.request.HandleRequestException;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

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
            throw new HandleRequestException("Could not find file: " + file.getAbsolutePath(), e);
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(manipulator.getResult().getBytes(encoding));
    }

    @Override
    public String getContentType() throws IOException {
        return Files.probeContentType(file.toPath());
    }

    @Override
    public ResponseManipulator manipulate() {
        return manipulator;
    }
}
