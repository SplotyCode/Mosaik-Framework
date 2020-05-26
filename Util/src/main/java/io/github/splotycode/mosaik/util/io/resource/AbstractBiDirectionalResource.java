package io.github.splotycode.mosaik.util.io.resource;

import io.github.splotycode.mosaik.util.io.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;

public abstract class AbstractBiDirectionalResource implements BiDirectionalResource {

    @Override
    public boolean isEmpty() throws IOException {
        return byteSize() != 0;
    }

    @Override
    public long copyTo(WritableResource resource) throws IOException {
        try (OutputStream os = resource.openOutStream()){
            return copyTo(os);
        }
    }

    @Override
    public long copyTo(OutputStream os) throws IOException {
        try (InputStream is = openStream()) {
            return IOUtil.copy(is, os);
        }
    }

    @Override
    public long write(InputStream inputStream) throws IOException {
        try (OutputStream os = openOutStream()) {
            return IOUtil.copy(inputStream, os);
        }
    }

    @Override
    public long write(ReadableResource resource) throws IOException {
        try (InputStream is = resource.openStream()){
            return write(is);
        }
    }

    @Override
    public void write(CharSequence charSequence) throws IOException {
        try (Writer writer = openWriter()){
            writer.append(charSequence);
        }
    }

    @Override
    public void write(CharSequence charSequence, Charset charset) throws IOException {
        try (Writer writer = openWriter(charset)){
            writer.append(charSequence);
        }
    }
}
