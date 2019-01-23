package io.github.splotycode.mosaik.domparsing.parsing.input;

import io.github.splotycode.mosaik.domparsing.parsing.DomSourceType;
import lombok.Getter;
import io.github.splotycode.mosaik.util.io.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DomUrlInput implements DomInput {

    @Getter protected URL url;

    public DomUrlInput(URL url) {
        this.url = url;
    }

    protected URLConnection connection;

    @Override
    public byte[] getBytes() {
        try {
            return IOUtil.toByteArray(getStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getString() {
        try {
            return IOUtil.loadText(getStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public InputStream getStream() {
        try (InputStream inputStream = getConnection().getInputStream()) {
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public URLConnection getConnection() throws IOException {
        return connection == null ? (connection = url.openConnection()) : connection;
    }

    @Override
    public DomSourceType getType() {
        return DomSourceType.URL;
    }
}
