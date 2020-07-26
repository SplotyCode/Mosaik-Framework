package io.github.splotycode.mosaik.util.io.resource;

import io.github.splotycode.mosaik.util.io.LineSeparator;
import io.github.splotycode.mosaik.util.io.ReaderInputStream;
import io.github.splotycode.mosaik.util.io.buffer.BufferProvider;
import io.github.splotycode.mosaik.util.io.copier.ByteCopyDestination;
import io.github.splotycode.mosaik.util.io.copier.CharCopyDestination;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.OptionalLong;
import java.util.stream.Stream;

public abstract class AbstractReadableResource<S_B, S_C> extends AbstractResource implements ReadableResource<S_B, S_C> {
    public AbstractReadableResource(BufferProvider bufferProvider) {
        super(bufferProvider);
    }

    @Override
    public <D> D copyTo(CharCopyDestination<D> destination) throws IOException {
        char[] buffer = bufferProvider().provideCharBuffer();
        int initialSize = (int) optimalInitialSize(buffer.length);
        return charSource().copyTo(openCharSource(), destination, buffer, initialSize);
    }

    @Override
    public <D> D copyTo(ByteCopyDestination<D> destination) throws IOException {
        byte[] buffer = bufferProvider().provideBuffer();
        int initialSize = (int) optimalInitialSize(buffer.length);
        return byteSource().copyTo(openByteSource(), destination, buffer, initialSize);
    }

    @Override
    public byte[] readAll(boolean fresh) throws IOException {
        return readAll();
    }

    @Override
    public InputStream openStream() throws IOException {
        return ReaderInputStream
                .builder(openReader())
                .withBufferProvider(bufferProvider())
                .build();
    }

    @Override
    public Reader openReader() throws IOException {
        return new InputStreamReader(openStream());
    }

    @Override
    public Reader openReader(Charset charset) throws IOException {
        return new InputStreamReader(openStream(), charset);
    }

    @Override
    public InputStream openPreferredBufferedStream() throws IOException {
        return avoidBufferedStream() ? openStream() : new BufferedInputStream(openStream());
    }

    @Override
    public BufferedReader openBufferedReader() throws IOException {
        return new BufferedReader(openReader());
    }

    @Override
    public BufferedReader openBufferedReader(Charset charset) throws IOException {
        return new BufferedReader(openReader(charset));
    }

    @Override
    public OptionalLong byteSizeIfPresent(SizeComputationComplexity computationComplexity) throws IOException {
        return byteSizeComplexity().hasReached(computationComplexity) ?
                OptionalLong.of(byteSize()) :
                OptionalLong.empty();
    }

    @Override
    public OptionalLong charSizeIfPresent(Charset charset, SizeComputationComplexity computationComplexity) throws IOException {
        return charSizeComplexity().hasReached(computationComplexity) ?
                OptionalLong.of(charSize(charset)) :
                OptionalLong.empty();
    }

    @Override
    public boolean isEmpty() throws IOException {
        if (byteSizeComplexity().hasReached(charSizeComplexity())) {
            return byteSize() != 0;
        }
        return charSize(StandardCharsets.UTF_16) != 0;
    }

    @Override
    public Stream<String> readLines() throws IOException {
        BufferedReader br = openBufferedReader();
        return br.lines().onClose(() -> {
            try {
                br.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    @Override
    public Stream<String> readLines(LineSeparator lineSeparator) throws IOException {
        return lineSeparator.streamLines(openBufferedReader());
    }

    @Override
    public Stream<String> readLines(Charset charset) throws IOException {
        BufferedReader br = openBufferedReader(charset);
        return br.lines().onClose(() -> {
            try {
                br.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    @Override
    public Stream<String> readLines(Charset charset, LineSeparator lineSeparator) throws IOException {
        return lineSeparator.streamLines(openBufferedReader(charset));
    }

    @Override
    public long optimalInitialSize(int bufferSize) throws IOException {
        return byteSizeIfPresent(SizeComputationComplexity.RETRIEVABLE).orElse(bufferSize * 2);
    }

    @Override
    public boolean avoidBufferedStream() {
        return false;
    }
}
