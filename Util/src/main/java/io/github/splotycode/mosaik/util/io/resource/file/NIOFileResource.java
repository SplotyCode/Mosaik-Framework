package io.github.splotycode.mosaik.util.io.resource.file;

import io.github.splotycode.mosaik.util.io.IOUtil;
import io.github.splotycode.mosaik.util.io.LineSeparator;
import io.github.splotycode.mosaik.util.io.buffer.BufferProvider;
import io.github.splotycode.mosaik.util.io.copier.*;
import io.github.splotycode.mosaik.util.io.resource.*;
import io.github.splotycode.mosaik.util.io.resource.copier.ReadableCopySourceBasedResource;
import io.github.splotycode.mosaik.util.io.resource.copier.WriteableCopyDestinationBasedResource;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class NIOFileResource extends CompoundResource<OutputStream, BufferedWriter, InputStream, BufferedReader> {
    private final Path path;

    public NIOFileResource(Path path) {
        this(path, AbstractResource.DEFAULT_INTERNAL_BUFFER_PROVIDER);
    }

    public NIOFileResource(Path path, BufferProvider bufferProvider) {
        this(path, bufferProvider, bufferProvider);
    }

    public NIOFileResource(Path path, BufferProvider writeBufferProvider,
                        BufferProvider readBufferProvider) {
        super(null, null);
        this.path = path;
        writableResource = new WriteableFileResource(writeBufferProvider);
        readableResource = new ReadableFileResource(readBufferProvider);
    }

    public class WriteableFileResource extends WriteableCopyDestinationBasedResource<OutputStream, BufferedWriter> {
        WriteableFileResource(BufferProvider bufferProvider) {
            super(bufferProvider, CopyDestinations.emptyOutputStream(), CopyDestinations.emptyWriter());
        }

        @Override
        public OutputStream openOutStream() throws IOException {
            return Files.newOutputStream(path);
        }

        @Override
        public BufferedWriter openWriter() throws IOException {
            return Files.newBufferedWriter(path);
        }

        @Override
        public BufferedWriter openWriter(Charset charset) throws IOException {
            return Files.newBufferedWriter(path, charset);
        }

        @Override
        public BufferedWriter openPreferredBufferedWriter() throws IOException {
            return openWriter();
        }

        @Override
        public BufferedWriter openPreferredBufferedWriter(Charset charset) throws IOException {
            return openWriter(charset);
        }

        @Override
        public OutputStream openByteDestination() throws IOException {
            return openOutStream();
        }

        @Override
        public BufferedWriter openCharDestination() throws IOException {
            return openWriter();
        }

        @Override
        public BufferedWriter openCharDestination(Charset charset) throws IOException {
            return openWriter(charset);
        }

        @Override
        public boolean avoidBufferedOutStream() {
            return true;
        }

        @Override
        public long write(ByteBuffer byteBuffer) throws IOException {
            try (SeekableByteChannel channel = Files.newByteChannel(path)) {
                return channel.write(byteBuffer);
            }
        }

        @Override
        public void write(byte[] bytes) throws IOException {
            Files.write(path, bytes);
        }

        @Override
        public long write(InputStream inputStream) throws IOException {
            return Files.copy(inputStream, path);
        }
    }

    public class ReadableFileResource extends ReadableCopySourceBasedResource<InputStream, BufferedReader> {
        ReadableFileResource(BufferProvider bufferProvider) {
            super(bufferProvider, CopySources.READER, CopySources.STREAM);
        }

        @Override
        public InputStream openByteSource() throws IOException {
            return openStream();
        }

        @Override
        public BufferedReader openCharSource() throws IOException {
            return openReader();
        }

        @Override
        public BufferedReader openCharSource(Charset charset) throws IOException {
            return openReader(charset);
        }

        @Override
        public InputStream openStream() throws IOException {
            return Files.newInputStream(path);
        }

        @Override
        public ByteBuffer getByteBuffer(boolean preferDirect) throws IOException {
            ByteBuffer buffer = IOUtil.allocateBytebuf(preferDirect, (int) byteSize());
            try (SeekableByteChannel channel = Files.newByteChannel(path)) {
                channel.read(buffer);
            }
            return buffer;
        }

        @Override
        public BufferedReader openReader() throws IOException {
            return Files.newBufferedReader(path);
        }

        @Override
        public BufferedReader openReader(Charset charset) throws IOException {
            return Files.newBufferedReader(path, charset);
        }

        @Override
        public BufferedReader openBufferedReader() throws IOException {
            return openReader();
        }

        @Override
        public BufferedReader openBufferedReader(Charset charset) throws IOException {
            return openReader(charset);
        }

        @Override
        public byte[] readAll() throws IOException {
            return Files.readAllBytes(path);
        }

        @Override
        public long byteSize() throws IOException {
            return Files.size(path);
        }

        @Override
        public SizeComputationComplexity byteSizeComplexity() {
            return SizeComputationComplexity.RETRIEVABLE;
        }

        @Override
        public Stream<String> readLines() throws IOException {
            return Files.lines(path);
        }

        @Override
        public Stream<String> readLines(LineSeparator lineSeparator) throws IOException {
            return lineSeparator.streamLines(openBufferedReader());
        }

        @Override
        public Stream<String> readLines(Charset charset) throws IOException {
            return Files.lines(path, charset);
        }

        @Override
        public Stream<String> readLines(Charset charset, LineSeparator lineSeparator) throws IOException {
            return lineSeparator.streamLines(openBufferedReader(charset));
        }

        @Override
        public String readAsString() throws IOException {
            return new String(readAll());
        }

        @Override
        public String readAsString(Charset charset) throws IOException {
            return new String(readAll(), charset);
        }
    }
}
