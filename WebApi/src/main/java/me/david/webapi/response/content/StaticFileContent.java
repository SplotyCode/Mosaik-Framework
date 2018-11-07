package me.david.webapi.response.content;

import lombok.AllArgsConstructor;

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
    public String getContentType() throws IOException {
        return Files.probeContentType(file.toPath());
    }
}
