package me.david.webapi.response.content;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CachedStatisFileContent implements ResponseContent {

    private static Map<String, ByteArrayInputStream> fileCache = new HashMap<>();

    private String file;

    public CachedStatisFileContent(String file) {
        this.file = file;
    }

    public CachedStatisFileContent(File file) {
        this.file = file.getAbsolutePath();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        ByteArrayInputStream stream = fileCache.get(file);
        if (stream == null) {
            stream = new ByteArrayInputStream(IOUtils.toByteArray(new FileInputStream(file)));
            fileCache.put(file, stream);
        }
        return stream;
    }
}
