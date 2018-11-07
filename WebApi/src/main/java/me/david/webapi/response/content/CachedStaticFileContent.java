package me.david.webapi.response.content;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

public class CachedStaticFileContent implements ResponseContent {

    private static ConcurrentHashMap<String, byte[]> fileCache = new ConcurrentHashMap<>();

    private String file;

    public CachedStaticFileContent(String file) {
        this.file = file;
    }

    public CachedStaticFileContent(File file) {
        this.file = file.getAbsolutePath();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        byte[] bytes = fileCache.get(file);
        if (bytes == null) {
            bytes = IOUtils.toByteArray(new FileInputStream(file));
            fileCache.put(file, bytes);
        }
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public String getContentType() throws IOException {
        return Files.probeContentType(Paths.get(file));
    }
}
