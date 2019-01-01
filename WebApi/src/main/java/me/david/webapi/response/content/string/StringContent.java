package me.david.webapi.response.content.string;

import me.david.davidlib.utils.io.ByteArrayInputStream;
import me.david.davidlib.utils.io.Charsets;
import me.david.webapi.response.content.manipulate.ManipulateableContent;
import me.david.webapi.response.content.manipulate.ResponseManipulator;
import me.david.webapi.response.content.manipulate.StringManipulator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class StringContent implements ManipulateableContent {

    private Charset charset;

    private StringManipulator manipulator;

    public StringContent(String str) {
        charset = Charsets.UTF8;
        manipulator = new StringManipulator(str);
    }

    public StringContent(String str, String charset) {
        this.charset = Charset.forName(charset);
        manipulator = new StringManipulator(str);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(manipulator.getResult().getBytes(charset));
    }

    @Override
    public ResponseManipulator manipulate() {
        return manipulator;
    }
}
