package me.david.webapi.response.content.file;

import me.david.davidlib.util.io.FileUtil;
import me.david.webapi.response.content.ResponseContent;

import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.ConcurrentHashMap;

public class CachedStaticFileContent implements ResponseContent {

    private static ConcurrentHashMap<File, byte[]> fileCache = new ConcurrentHashMap<>();

    private File file;

    public CachedStaticFileContent(String file) {
        this.file = new File(file);
    }

    public CachedStaticFileContent(File file) {
        this.file = file;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        byte[] bytes = fileCache.get(file);
        if (bytes == null) {
            bytes = FileUtil.loadFileBytes(file);
            fileCache.put(file, bytes);
        }
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public String getContentType() throws IOException {
        return Files.probeContentType(file.toPath());
    }
}
