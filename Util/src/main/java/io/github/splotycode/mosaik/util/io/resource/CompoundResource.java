package io.github.splotycode.mosaik.util.io.resource;

import io.github.splotycode.mosaik.util.io.LineSeparator;
import io.github.splotycode.mosaik.util.io.buffer.BufferProvider;
import io.github.splotycode.mosaik.util.io.copier.ByteCopyDestination;
import io.github.splotycode.mosaik.util.io.copier.ByteCopySource;
import io.github.splotycode.mosaik.util.io.copier.CharCopyDestination;
import io.github.splotycode.mosaik.util.io.copier.CharCopySource;
import lombok.AllArgsConstructor;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.OptionalLong;
import java.util.stream.Stream;

@AllArgsConstructor
public class CompoundResource<D_B, D_C, S_B, S_C> implements BiResource<D_B, D_C, S_B, S_C> {

    protected WritableResource<D_B, D_C> writableResource;
    protected ReadableResource<S_B, S_C> readableResource;

    @Override
    public BufferProvider bufferProvider() {
        BufferProvider bufferProvider = readableResource.bufferProvider();
        if (bufferProvider == null) {
            return writableResource.bufferProvider();
        }
        return bufferProvider;
    }

    @Override
    public ByteCopyDestination<D_B> byteDestination() {
        return writableResource.byteDestination();
    }

    @Override
    public CharCopyDestination<D_C> charDestination() {
        return writableResource.charDestination();
    }

    @Override
    public D_B openByteDestination() throws IOException {
        return writableResource.openByteDestination();
    }

    @Override
    public D_C openCharDestination() throws IOException {
        return writableResource.openCharDestination();
    }

    @Override
    public D_C openCharDestination(Charset charset) throws IOException {
        return writableResource.openCharDestination(charset);
    }

    @Override
    public OutputStream openOutStream() throws IOException {
        return writableResource.openOutStream();
    }

    @Override
    public boolean avoidBufferedOutStream() {
        return writableResource.avoidBufferedOutStream();
    }

    @Override
    public OutputStream openPreferredBufferedOutStream() throws IOException {
        return writableResource.openPreferredBufferedOutStream();
    }

    @Override
    public <S> long writeFrom(ByteCopySource<S> sourceType, S source) throws IOException {
        return writableResource.writeFrom(sourceType, source);
    }

    @Override
    public <S> long writeFrom(CharCopySource<S> sourceType, S source) throws IOException {
        return writableResource.writeFrom(sourceType, source);
    }

    @Override
    public long write(ByteBuffer byteBuffer) throws IOException {
        return writableResource.write(byteBuffer);
    }

    @Override
    public long write(InputStream inputStream) throws IOException {
        return writableResource.write(inputStream);
    }

    @Override
    public long write(ReadableResource resource) throws IOException {
        return writableResource.write(resource);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        writableResource.write(bytes);
    }

    @Override
    public void write(CharSequence charSequence) throws IOException {
        writableResource.write(charSequence);
    }

    @Override
    public void write(CharSequence charSequence, Charset charset) throws IOException {
        writableResource.write(charSequence, charset);
    }

    @Override
    public Writer openWriter() throws IOException {
        return writableResource.openWriter();
    }

    @Override
    public boolean avoidBufferedWriter() {
        return writableResource.avoidBufferedWriter();
    }

    @Override
    public Writer openPreferredBufferedWriter() throws IOException {
        return writableResource.openPreferredBufferedWriter();
    }

    @Override
    public Writer openWriter(Charset charset) throws IOException {
        return writableResource.openWriter(charset);
    }

    @Override
    public Writer openPreferredBufferedWriter(Charset charset) throws IOException {
        return writableResource.openPreferredBufferedWriter(charset);
    }

    @Override
    public ByteCopySource<? super S_B> byteSource() throws IOException {
        return readableResource.byteSource();
    }

    @Override
    public CharCopySource<? super S_C> charSource() throws IOException {
        return readableResource.charSource();
    }

    @Override
    public S_B openByteSource() throws IOException {
        return readableResource.openByteSource();
    }

    @Override
    public S_C openCharSource() throws IOException {
        return readableResource.openCharSource();
    }

    @Override
    public S_C openCharSource(Charset charset) throws IOException {
        return readableResource.openCharSource(charset);
    }

    @Override
    public <D> D copyTo(ByteCopyDestination<D> destination) throws IOException {
        return readableResource.copyTo(destination);
    }

    @Override
    public <D> D copyTo(CharCopyDestination<D> destination) throws IOException {
        return readableResource.copyTo(destination);
    }

    @Override
    public long optimalInitialSize(int bufferSize) throws IOException {
        return readableResource.optimalInitialSize(bufferSize);
    }

    @Override
    public InputStream openStream() throws IOException {
        return readableResource.openStream();
    }

    @Override
    public ByteBuffer getByteBuffer(boolean preferDirect) throws IOException {
        return readableResource.getByteBuffer(preferDirect);
    }

    @Override
    public byte[] readAll(boolean fresh) throws IOException {
        return readableResource.readAll(fresh);
    }

    @Override
    public byte[] readAll() throws IOException {
        return readableResource.readAll();
    }

    @Override
    public boolean avoidBufferedStream() {
        return readableResource.avoidBufferedStream();
    }

    @Override
    public InputStream openPreferredBufferedStream() throws IOException {
        return readableResource.openPreferredBufferedStream();
    }

    @Override
    public Reader openReader() throws IOException {
        return readableResource.openReader();
    }

    @Override
    public Reader openReader(Charset charset) throws IOException {
        return readableResource.openReader(charset);
    }

    @Override
    public BufferedReader openBufferedReader() throws IOException {
        return readableResource.openBufferedReader();
    }

    @Override
    public BufferedReader openBufferedReader(Charset charset) throws IOException {
        return readableResource.openBufferedReader(charset);
    }

    @Override
    public String readAsString() throws IOException {
        return readableResource.readAsString();
    }

    @Override
    public String readAsString(Charset charset) throws IOException {
        return readableResource.readAsString(charset);
    }

    @Override
    public long copyTo(OutputStream os) throws IOException {
        return readableResource.copyTo(os);
    }

    @Override
    public long copyTo(Appendable appendable) throws IOException {
        return readableResource.copyTo(appendable);
    }

    @Override
    public <LD_B, LD_C> long copyTo(WritableResource<LD_B, LD_C> resource) throws IOException {
        return readableResource.copyTo(resource);
    }

    @Override
    public boolean isEmpty() throws IOException {
        return readableResource.isEmpty();
    }

    @Override
    public long byteSize() throws IOException {
        return readableResource.byteSize();
    }

    @Override
    public SizeComputationComplexity byteSizeComplexity() throws IOException {
        return readableResource.byteSizeComplexity();
    }

    @Override
    public OptionalLong byteSizeIfPresent(SizeComputationComplexity computationComplexity) throws IOException {
        return readableResource.byteSizeIfPresent(computationComplexity);
    }

    @Override
    public long charSize(Charset charset) throws IOException {
        return readableResource.charSize(charset);
    }

    @Override
    public OptionalLong charSizeIfPresent(Charset charset, SizeComputationComplexity computationComplexity) throws IOException {
        return readableResource.charSizeIfPresent(charset, computationComplexity);
    }

    @Override
    public SizeComputationComplexity charSizeComplexity() throws IOException {
        return readableResource.charSizeComplexity();
    }

    @Override
    public Stream<String> readLines() throws IOException {
        return readableResource.readLines();
    }

    @Override
    public Stream<String> readLines(LineSeparator lineSeparator) throws IOException {
        return readableResource.readLines(lineSeparator);
    }

    @Override
    public Stream<String> readLines(Charset charset) throws IOException {
        return readableResource.readLines(charset);
    }

    @Override
    public Stream<String> readLines(Charset charset, LineSeparator lineSeparator) throws IOException {
        return readableResource.readLines(charset, lineSeparator);
    }

    @Override
    public boolean readable() {
        return readableResource.readable();
    }

    @Override
    public boolean writable() {
        return readableResource.writable();
    }
}
