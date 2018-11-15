package me.david.webapi.response.content;

import me.david.davidlib.io.ByteArrayInputStream;
import me.david.webapi.response.content.manipulate.ManipulateableContent;
import me.david.webapi.response.content.manipulate.ResponseManipulator;
import me.david.webapi.response.content.manipulate.StringManipulator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class StringResponseContent implements ManipulateableContent {

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private Charset charset;
    private String str;

    private StringManipulator manipulator;

    public StringResponseContent(String str) {
        charset = UTF_8;
        setStr(str);
    }

    public void setStr(String str) {
        this.str = str;
        manipulator = new StringManipulator(str);
    }

    public StringResponseContent(String str, String charset) {
        this.charset = Charset.forName(charset);
        this.str = str;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(str.getBytes(charset));
    }

    @Override
    public ResponseManipulator manipulate() {
        return manipulator;
    }
}
