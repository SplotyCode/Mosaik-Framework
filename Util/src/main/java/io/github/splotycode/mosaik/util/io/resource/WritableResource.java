package io.github.splotycode.mosaik.util.io.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public interface WritableResource extends Resource {

    OutputStream openOutStream() throws IOException;
    boolean avoidBufferedOutStream();
    OutputStream openPreferredBufferedOutStream() throws IOException;

    void write(ByteBuffer byteBuffer) throws IOException;
    long write(InputStream inputStream) throws IOException;
    long write(ReadableResource resource) throws IOException;
    void write(byte[] bytes) throws IOException;

    void write(CharSequence charSequence) throws IOException;
    void write(CharSequence charSequence, Charset charset) throws IOException;

    Writer openWriter() throws IOException;
    boolean avoidBufferedWriter();
    Writer openPreferredBufferedWriter() throws IOException;

    Writer openWriter(Charset charset) throws IOException;
    Writer openPreferredBufferedWriter(Charset charset) throws IOException;

    @Override
    default boolean writable() {
        return true;
    }
}
