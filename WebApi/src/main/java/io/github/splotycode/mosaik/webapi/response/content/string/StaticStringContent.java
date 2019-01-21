package io.github.splotycode.mosaik.webapi.response.content.string;

import io.github.splotycode.mosaik.util.io.ByteArrayInputStream;
import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StaticStringContent implements ResponseContent {

    private Charset charset;
    private String str;

    public StaticStringContent(String str) {
        charset = StandardCharsets.UTF_8;
        this.str = str;
    }

    public StaticStringContent(String str, String charset) {
        this.charset = Charset.forName(charset);
        this.str = str;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(str.getBytes(charset));
    }
}
