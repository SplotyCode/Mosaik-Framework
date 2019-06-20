package io.github.splotycode.mosaik.webapi.response.content.file;

import io.github.splotycode.mosaik.webapi.request.HandleRequestException;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.ManipulateableContent;
import lombok.AllArgsConstructor;
import io.github.splotycode.mosaik.util.io.ByteArrayInputStream;
import io.github.splotycode.mosaik.util.io.FileUtil;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.ResponseManipulator;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.StringManipulator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@AllArgsConstructor
public class FileResponseContent implements ManipulateableContent<FileResponseContent> {

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
            manipulator = new StringManipulator(FileUtil.loadFile(file, encoding));
        } catch (Throwable e) {
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
    public long lastModified() throws IOException {
        return file.lastModified();
    }

    @Override
    public ResponseManipulator manipulate() {
        return manipulator;
    }

    @Override
    public FileResponseContent self() {
        return this;
    }
}
