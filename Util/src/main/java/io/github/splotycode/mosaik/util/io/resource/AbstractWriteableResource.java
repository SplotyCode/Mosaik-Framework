package io.github.splotycode.mosaik.util.io.resource;

import io.github.splotycode.mosaik.util.io.WriterOutputStream;
import io.github.splotycode.mosaik.util.io.buffer.BufferProvider;

import java.io.*;
import java.nio.charset.Charset;

public abstract class AbstractWriteableResource<D_B, D_C> extends AbstractResource implements WritableResource<D_B, D_C> {
    public AbstractWriteableResource(BufferProvider bufferProvider) {
        super(bufferProvider);
    }

    @Override
    public Writer openWriter() throws IOException {
        return new OutputStreamWriter(openOutStream());
    }

    @Override
    public Writer openWriter(Charset charset) throws IOException {
        return new OutputStreamWriter(openOutStream(), charset);
    }

    @Override
    public OutputStream openOutStream() throws IOException {
        return WriterOutputStream
                .builder(openWriter())
                .withBufferProvider(bufferProvider())
                .build();
    }

    @Override
    public OutputStream openPreferredBufferedOutStream() throws IOException {
        return avoidBufferedOutStream() ? openOutStream() : new BufferedOutputStream(openOutStream());
    }

    @Override
    public Writer openPreferredBufferedWriter() throws IOException {
        return avoidBufferedWriter() ? openWriter() : new BufferedWriter(openWriter());
    }

    @Override
    public Writer openPreferredBufferedWriter(Charset charset) throws IOException {
        return avoidBufferedWriter() ? openWriter(charset) : new BufferedWriter(openWriter(charset));
    }

    @Override
    public boolean avoidBufferedOutStream() {
        return false;
    }

    @Override
    public boolean avoidBufferedWriter() {
        return false;
    }
}
