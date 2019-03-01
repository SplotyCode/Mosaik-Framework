package io.github.splotycode.mosaik.webapi.response.content.file;

import lombok.AllArgsConstructor;
import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@AllArgsConstructor
public class StaticFileContent implements ResponseContent {

    private File file;

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public long lastModified() throws IOException {
        return file.lastModified();
    }

    @Override
    public String getContentType() throws IOException {
        return Files.probeContentType(file.toPath());
    }
}
