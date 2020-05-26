package io.github.splotycode.mosaik.util.io.resource;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.OptionalLong;

public interface ReadableResource extends Resource {

    InputStream openStream() throws IOException;
    ByteBuffer getByteBuffer(boolean preferDirect) throws IOException;
    byte[] readAll(boolean fresh) throws IOException;
    byte[] readAll() throws IOException;

    boolean avoidBufferedStream();
    InputStream openPreferredBufferedStream() throws IOException;

    Reader openReader() throws IOException;
    BufferedReader openBufferedReader() throws IOException;
    String readAsString() throws IOException;
    Reader openReader(Charset charset) throws IOException;
    BufferedReader openBufferedReader(Charset charset) throws IOException;
    String readAsString(Charset charset) throws IOException;

    long copyTo(OutputStream os) throws IOException;
    long copyTo(Appendable appendable) throws IOException;
    long copyTo(WritableResource resource) throws IOException;

    boolean isEmpty() throws IOException;
    long byteSize() throws IOException;
    OptionalLong byteSizeIfPresent(boolean force);
    long charSize(Charset charset) throws IOException;

    @Override
    default boolean readable(){
        return true;
    }
}
