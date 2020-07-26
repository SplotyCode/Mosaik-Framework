package io.github.splotycode.mosaik.util.io.resource.file;

import io.github.splotycode.mosaik.util.io.IOUtil;
import io.github.splotycode.mosaik.util.io.buffer.BufferProvider;
import io.github.splotycode.mosaik.util.io.copier.CopyDestinations;
import io.github.splotycode.mosaik.util.io.copier.CopySources;
import io.github.splotycode.mosaik.util.io.resource.*;
import io.github.splotycode.mosaik.util.io.resource.copier.ReadableCopySourceBasedResource;
import io.github.splotycode.mosaik.util.io.resource.copier.WriteableCopyDestinationBasedResource;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/*
 * On Java8 there is no charset parameter in FileReader and FileWriter so we can not use those
 */
public class FileResource extends CompoundResource<FileOutputStream, Writer, FileInputStream, Reader> {
    private final File file;

    public FileResource(File file) {
        this(file, AbstractResource.DEFAULT_INTERNAL_BUFFER_PROVIDER);
    }

    public FileResource(File file, BufferProvider bufferProvider) {
        this(file, bufferProvider, bufferProvider);
    }

    public FileResource(File file, BufferProvider writeBufferProvider,
                        BufferProvider readBufferProvider) {
        super(null, null);
        this.file = file;
        writableResource = new WriteableFileResource(writeBufferProvider);
        readableResource = new ReadableFileResource(readBufferProvider);
    }

    public class WriteableFileResource extends WriteableCopyDestinationBasedResource<FileOutputStream, Writer> {
        WriteableFileResource(BufferProvider bufferProvider) {
            super(bufferProvider, CopyDestinations.emptyOutputStream(), CopyDestinations.EMPTY_WRITER);
        }

        @Override
        public FileOutputStream openByteDestination() throws IOException {
            return new FileOutputStream(file);
        }

        @Override
        public FileOutputStream openOutStream() throws IOException {
            return openByteDestination();
        }

        @Override
        public FileWriter openCharDestination() throws IOException {
            return new FileWriter(file);
        }

        @Override
        public Writer openCharDestination(Charset charset) throws IOException {
            return new OutputStreamWriter(openOutStream(), charset);
        }

        @Override
        public long write(ByteBuffer byteBuffer) throws IOException {
            try (FileOutputStream outputStream = openOutStream()) {
                return outputStream.getChannel().write(byteBuffer);
            }
        }

        @Override
        public Writer openWriter() throws IOException {
            return openCharDestination();
        }

        @Override
        public Writer openWriter(Charset charset) throws IOException {
            return openCharDestination(charset);
        }
    }

    public class ReadableFileResource extends ReadableCopySourceBasedResource<FileInputStream, Reader> {
        ReadableFileResource(BufferProvider bufferProvider) {
            super(bufferProvider, CopySources.READER, CopySources.STREAM);
        }

        @Override
        public ByteBuffer getByteBuffer(boolean preferDirect) throws IOException {
            ByteBuffer buffer = IOUtil.allocateBytebuf(preferDirect, (int) byteSize());
            try (FileInputStream inputStream = openStream()) {
                FileChannel channel = inputStream.getChannel();
                channel.read(buffer);
                channel.close();
            }
            return buffer;
        }

        @Override
        public long byteSize() {
            return file.length();
        }

        @Override
        public SizeComputationComplexity byteSizeComplexity() {
            return SizeComputationComplexity.RETRIEVABLE;
        }

        @Override
        public FileInputStream openStream() throws IOException {
            return openByteSource();
        }

        @Override
        public FileInputStream openByteSource() throws IOException {
            return new FileInputStream(file);
        }

        @Override
        public FileReader openCharSource() throws IOException {
            return new FileReader(file);
        }

        @Override
        public Reader openCharSource(Charset charset) throws IOException {
            return openReader(charset);
        }
    }
}
