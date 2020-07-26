package io.github.splotycode.mosaik.util.io.resource.copier;

import io.github.splotycode.mosaik.util.io.buffer.BufferProvider;
import io.github.splotycode.mosaik.util.io.copier.*;
import io.github.splotycode.mosaik.util.io.resource.AbstractWriteableResource;
import io.github.splotycode.mosaik.util.io.resource.ReadableResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public abstract class WriteableCopyDestinationBasedResource<D_B, D_C> extends AbstractWriteableResource<D_B, D_C> {
    protected ByteCopyDestination<D_B> byteDestination;
    protected CharCopyDestination<D_C> charDestination;

    public WriteableCopyDestinationBasedResource(BufferProvider bufferProvider,
                                                 ByteCopyDestination<D_B> byteDestination,
                                                 CharCopyDestination<D_C> charDestination) {
        super(bufferProvider);
        this.byteDestination = byteDestination;
        this.charDestination = charDestination;
    }

    @Override
    public ByteCopyDestination<D_B> byteDestination() {
        return byteDestination;
    }

    @Override
    public CharCopyDestination<D_C> charDestination() {
        return charDestination;
    }

    @Override
    public <S> long writeFrom(ByteCopySource<S> sourceType, S source) throws IOException {
        byte[] buffer = bufferProvider().provideBuffer();
        return sourceType.copyTo(source, openByteDestination(), byteDestination, buffer);
    }

    @Override
    public <S> long writeFrom(CharCopySource<S> sourceType, S source) throws IOException {
        char[] buffer = bufferProvider().provideCharBuffer();
        return sourceType.copyTo(source, openCharDestination(), charDestination, buffer);
    }

    @Override
    public long write(ByteBuffer byteBuffer) throws IOException {
        return writeFrom(CopySources.BYTE_BUFFER, byteBuffer);
    }

    @Override
    public long write(InputStream inputStream) throws IOException {
        return writeFrom(CopySources.STREAM, inputStream);
    }

    @Override
    public long write(ReadableResource resource) throws IOException {
        return resource.copyTo(this);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        D_B destination = openByteDestination();
        try {
            byteDestination.copyTo(bytes, destination, bytes.length);
        } finally {
            byteDestination.finish(destination);
        }
    }

    private void write(D_C destination, String str) throws IOException {
        try {
            char[] buffer = bufferProvider().provideCharBuffer();
            int length = str.length();
            int written = 0;
            while (written < length) {
                int size = Math.min(buffer.length, length - written);
                str.getChars(written, written + size, buffer, 0);
                charDestination.copyTo(buffer, destination, size);
                written += length;
            }
        } finally {
            charDestination.finish(destination);
        }
    }

    @Override
    public void write(CharSequence charSequence) throws IOException {
        write(openCharDestination(), charSequence.toString());
    }

    @Override
    public void write(CharSequence charSequence, Charset charset) throws IOException {
        write(openCharDestination(charset), charSequence.toString());
    }
}
