package io.github.splotycode.mosaik.webapi.response.content.file;

import io.github.splotycode.mosaik.util.io.ByteArrayInputStream;
import io.github.splotycode.mosaik.util.io.FileUtil;
import io.github.splotycode.mosaik.webapi.request.HandleRequestException;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.ManipulateableContent;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.ResponseManipulator;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.StringManipulator;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class CachedFileResponseContent implements ManipulateableContent {

    private static Map<File, String> files = new HashMap<>();

    private File file;
    private StringManipulator manipulator;
    private String encoding;

    public CachedFileResponseContent(String file) {
        this(file, "UTF-8");
    }

    public CachedFileResponseContent(File file) {
        this(file, "UTF-8");
    }

    public CachedFileResponseContent(String file, String encoding) {
        this(new File(file), encoding);
    }

    public CachedFileResponseContent(File file, String encoding) {
        this.encoding = encoding;
        this.file = file;
        try {
            String content = files.get(file);
            if (content == null) {
                content = FileUtil.loadFile(file, encoding);
                files.put(file, content);
            }
            manipulator = new StringManipulator(content);
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
