package io.github.splotycode.mosaik.util.io.resource.copier;

import io.github.splotycode.mosaik.util.io.buffer.BufferProvider;
import io.github.splotycode.mosaik.util.io.buffer.BufferProviders;
import io.github.splotycode.mosaik.util.io.copier.*;
import io.github.splotycode.mosaik.util.io.resource.AbstractReadableResource;
import io.github.splotycode.mosaik.util.io.resource.SizeComputationComplexity;
import io.github.splotycode.mosaik.util.io.resource.WritableResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public abstract class ReadableCopySourceBasedResource<S_B, S_C> extends AbstractReadableResource<S_B, S_C> {
    public static BufferProvider DEFAULT_BUFFER_PROVIDER = BufferProviders.createThreadLocal(1024 * 512);

    protected CharCopySource<? super S_C> charSource;
    protected ByteCopySource<? super S_B> byteSource;

    public ReadableCopySourceBasedResource(BufferProvider bufferProvider,
                                           CharCopySource<? super S_C> charSource, ByteCopySource<? super S_B> byteSource) {
        super(bufferProvider);
        this.charSource = charSource;
        this.byteSource = byteSource;
    }

    @Override
    public CharCopySource<? super S_C> charSource() throws IOException {
        return charSource;
    }

    @Override
    public ByteCopySource<? super S_B> byteSource() throws IOException {
        return byteSource;
    }

    @Override
    public ByteBuffer getByteBuffer(boolean preferDirect) throws IOException {
        return copyTo(CopyDestinations.byteBuffer(preferDirect));
    }

    @Override
    public byte[] readAll() throws IOException {
        return copyTo(CopyDestinations.BYTE_ARRAY_OUTPUT_STREAM).toByteArray();
    }

    @Override
    public Reader openReader() throws IOException {
        //TODO should we use buffered streams here?
        return new InputStreamReader(openStream());
    }

    @Override
    public Reader openReader(Charset charset) throws IOException {
        //TODO should we use buffered streams here?
        return new InputStreamReader(openStream(), charset);
    }

    @Override
    public String readAsString() throws IOException {
        return new String(readAll(false));
    }

    @Override
    public String readAsString(Charset charset) throws IOException {
        return new String(readAll(false), charset);
    }

    @Override
    public long copyTo(OutputStream os) throws IOException {
        byte[] buffer = bufferProvider().provideBuffer();
        return byteSource().copyTo(openByteSource(), os, CopyDestinations.emptyOutputStream(), buffer);
    }

    @Override
    public long copyTo(Appendable appendable) throws IOException {
        char[] buffer = bufferProvider().provideCharBuffer();
        return charSource().copyTo(openCharSource(), appendable, CopyDestinations.APPENDALE, buffer);
    }

    @Override
    public <LD_B, LD_C> long copyTo(WritableResource<LD_B, LD_C> resource) throws IOException {
        if (nativeFormat().hasChars() && resource.nativeFormat().hasChars()) {
            char[] buffer = bufferProvider().provideCharBuffer();
            int initialSize = (int) optimalInitialSize(buffer.length);

            LD_C destination = resource.charDestination().create(initialSize);
            return charSource().copyTo(openCharSource(), destination, resource.charDestination(), buffer);
        }

        byte[] buffer = bufferProvider().provideBuffer();
        int initialSize = (int) optimalInitialSize(buffer.length);

        LD_B destination = resource.byteDestination().create(initialSize);
        return byteSource().copyTo(openByteSource(), destination, resource.byteDestination(), buffer);
    }

    @Override
    public long byteSize() throws IOException {
        return copyTo(CopyDestinations.BYTE_COUNTER).getValue();
    }

    @Override
    public SizeComputationComplexity byteSizeComplexity() throws IOException {
        return SizeComputationComplexity.COUNTABLE;
    }

    @Override
    public long charSize(Charset charset) throws IOException {
        return copyTo(CopyDestinations.CHAR_COUNTER).getValue();
    }

    @Override
    public SizeComputationComplexity charSizeComplexity() throws IOException {
        return SizeComputationComplexity.COUNTABLE;
    }
}
