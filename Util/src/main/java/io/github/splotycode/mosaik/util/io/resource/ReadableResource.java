package io.github.splotycode.mosaik.util.io.resource;

import io.github.splotycode.mosaik.util.io.LineSeparator;
import io.github.splotycode.mosaik.util.io.copier.ByteCopyDestination;
import io.github.splotycode.mosaik.util.io.copier.ByteCopySource;
import io.github.splotycode.mosaik.util.io.copier.CharCopyDestination;
import io.github.splotycode.mosaik.util.io.copier.CharCopySource;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.OptionalLong;
import java.util.stream.Stream;

public interface ReadableResource<S_B, S_C> extends Resource {
    ByteCopySource<? super S_B> byteSource() throws IOException;
    CharCopySource<? super S_C> charSource() throws IOException;
    S_B openByteSource() throws IOException;
    S_C openCharSource() throws IOException;
    S_C openCharSource(Charset charset) throws IOException;
    <D> D copyTo(ByteCopyDestination<D> destination) throws IOException;
    <D> D copyTo(CharCopyDestination<D> destination) throws IOException;

    long optimalInitialSize(int bufferSize) throws IOException;

    InputStream openStream() throws IOException;
    ByteBuffer getByteBuffer(boolean preferDirect) throws IOException;
    byte[] readAll(boolean fresh) throws IOException;
    byte[] readAll() throws IOException;

    boolean avoidBufferedStream();
    InputStream openPreferredBufferedStream() throws IOException;

    Reader openReader() throws IOException;
    Reader openReader(Charset charset) throws IOException;
    BufferedReader openBufferedReader() throws IOException;
    BufferedReader openBufferedReader(Charset charset) throws IOException;
    String readAsString() throws IOException;
    String readAsString(Charset charset) throws IOException;

    long copyTo(OutputStream os) throws IOException;
    long copyTo(Appendable appendable) throws IOException;
    <LD_B, LD_C> long copyTo(WritableResource<LD_B, LD_C> resource) throws IOException;

    boolean isEmpty() throws IOException;
    long byteSize() throws IOException;
    SizeComputationComplexity byteSizeComplexity() throws IOException;
    OptionalLong byteSizeIfPresent(SizeComputationComplexity computationComplexity) throws IOException;
    long charSize(Charset charset) throws IOException;
    OptionalLong charSizeIfPresent(Charset charset, SizeComputationComplexity computationComplexity) throws IOException;
    SizeComputationComplexity charSizeComplexity() throws IOException;

    Stream<String> readLines() throws IOException;
    Stream<String> readLines(LineSeparator lineSeparator) throws IOException;
    Stream<String> readLines(Charset charset) throws IOException;
    Stream<String> readLines(Charset charset, LineSeparator lineSeparator) throws IOException;

    @Override
    default boolean readable(){
        return true;
    }

    @Override
    default boolean writable() {
        return false;
    }
}
