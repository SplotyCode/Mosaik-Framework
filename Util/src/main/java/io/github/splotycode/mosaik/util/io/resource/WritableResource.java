package io.github.splotycode.mosaik.util.io.resource;

import io.github.splotycode.mosaik.util.io.copier.ByteCopyDestination;
import io.github.splotycode.mosaik.util.io.copier.ByteCopySource;
import io.github.splotycode.mosaik.util.io.copier.CharCopyDestination;
import io.github.splotycode.mosaik.util.io.copier.CharCopySource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public interface WritableResource<D_B, D_C> extends Resource {
    ByteCopyDestination<D_B> byteDestination();
    CharCopyDestination<D_C> charDestination();

    D_B openByteDestination() throws IOException;
    D_C openCharDestination() throws IOException;
    D_C openCharDestination(Charset charset) throws IOException;

    OutputStream openOutStream() throws IOException;
    boolean avoidBufferedOutStream();
    OutputStream openPreferredBufferedOutStream() throws IOException;

    <S> long writeFrom(ByteCopySource<S> sourceType, S source) throws IOException;
    <S> long writeFrom(CharCopySource<S> sourceType, S source) throws IOException;
    long write(ByteBuffer byteBuffer) throws IOException;
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

    @Override
    default boolean readable() {
        return false;
    }
}
