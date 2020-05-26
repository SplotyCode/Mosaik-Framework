package io.github.splotycode.mosaik.util.io.resource;

import lombok.AllArgsConstructor;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.OptionalLong;

/*
 * TODO copyTo(Appendable appendable)
 * TODO charSize(Charset charset)
 */
@AllArgsConstructor
public class NIOFileResource extends AbstractBiDirectionalResource {

    private Path path;

    @Override
    public InputStream openStream() throws IOException {
        return Files.newInputStream(path);
    }

    @Override
    public ByteBuffer getByteBuffer(boolean preferDirect) throws IOException {
        int size = (int) byteSize();
        ByteBuffer buffer = preferDirect ? ByteBuffer.allocateDirect(size) : ByteBuffer.allocate(size);
        try (SeekableByteChannel channel = Files.newByteChannel(path)) {
            channel.read(buffer);
        }
        return buffer;
    }

    @Override
    public byte[] readAll(boolean fresh) throws IOException {
        return readAll();
    }

    @Override
    public byte[] readAll() throws IOException {
        return Files.readAllBytes(path);
    }

    @Override
    public boolean avoidBufferedStream() {
        return false;
    }

    @Override
    public InputStream openPreferredBufferedStream() throws IOException {
        return new BufferedInputStream(openStream());
    }

    @Override
    public Reader openReader() throws IOException {
        return openBufferedReader();
    }

    @Override
    public BufferedReader openBufferedReader() throws IOException {
        return Files.newBufferedReader(path);
    }

    @Override
    public String readAsString() throws IOException {
        return new String(readAll());
    }

    @Override
    public Reader openReader(Charset charset) throws IOException {
        return openBufferedReader(charset);
    }

    @Override
    public BufferedReader openBufferedReader(Charset charset) throws IOException {
        return Files.newBufferedReader(path, charset);
    }

    @Override
    public String readAsString(Charset charset) throws IOException {
        return new String(readAll(), charset);
    }

    @Override
    public long copyTo(Appendable appendable) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long byteSize() throws IOException {
        return Files.size(path);
    }

    @Override
    public OptionalLong byteSizeIfPresent(boolean force) {
        return OptionalLong.empty();
    }

    @Override
    public long charSize(Charset charset) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public OutputStream openOutStream() throws IOException {
        return Files.newOutputStream(path);
    }

    @Override
    public boolean avoidBufferedOutStream() {
        return false;
    }

    @Override
    public OutputStream openPreferredBufferedOutStream() throws IOException {
        OutputStream stream = openOutStream();
        return stream instanceof BufferedOutputStream ?
                stream : new BufferedOutputStream(stream);
    }

    @Override
    public void write(ByteBuffer byteBuffer) throws IOException {
        //TODO buffer?
        byteBuffer.put(readAll());
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        Files.write(path, bytes);
    }

    @Override
    public long write(InputStream inputStream) throws IOException {
        return Files.copy(inputStream, path);
    }

    @Override
    public Writer openWriter() throws IOException {
        return openPreferredBufferedWriter();
    }

    @Override
    public boolean avoidBufferedWriter() {
        return false;
    }

    @Override
    public Writer openPreferredBufferedWriter() throws IOException {
        return Files.newBufferedWriter(path);
    }

    @Override
    public Writer openWriter(Charset charset) throws IOException {
        return openPreferredBufferedWriter(charset);
    }

    @Override
    public Writer openPreferredBufferedWriter(Charset charset) throws IOException {
        return Files.newBufferedWriter(path, charset);
    }
}
