package io.github.splotycode.mosaik.util.io.resource;

import io.github.splotycode.mosaik.util.io.FileUtil;
import lombok.AllArgsConstructor;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.OptionalLong;

@AllArgsConstructor
public class FileResource extends AbstractBiDirectionalResource {

    private File file;

    @Override
    public FileInputStream openStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public ByteBuffer getByteBuffer(boolean preferDirect) throws IOException {
        int size = (int) byteSize();
        ByteBuffer buffer = preferDirect ? ByteBuffer.allocateDirect(size) : ByteBuffer.allocate(size);
        try (FileInputStream is = openStream()){
            is.getChannel().read(buffer);
        }
        return buffer;
    }

    @Override
    public byte[] readAll(boolean fresh) throws IOException {
        return readAll();
    }

    @Override
    public byte[] readAll() throws IOException {
        return FileUtil.loadFileBytes(file);
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
        return new FileReader(file);
    }

    @Override
    public BufferedReader openBufferedReader() throws IOException {
        return new BufferedReader(openReader());
    }

    @Override
    public String readAsString() throws IOException {
        return FileUtil.loadFile(file);
    }

    @Override
    public Reader openReader(Charset charset) throws IOException {
        /*
         * On Java8 there is no charset parameter in FileReader
         */
        return new InputStreamReader(new FileInputStream(file), charset);
    }

    @Override
    public BufferedReader openBufferedReader(Charset charset) throws IOException {
        return new BufferedReader(openReader(charset));
    }

    @Override
    public String readAsString(Charset charset) throws IOException {
        return FileUtil.loadFile(file, charset);
    }

    @Override
    public long copyTo(Appendable appendable) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long byteSize() throws IOException {
        return file.length();
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
        return new FileOutputStream(file);
    }

    @Override
    public boolean avoidBufferedOutStream() {
        return false;
    }

    @Override
    public OutputStream openPreferredBufferedOutStream() throws IOException {
        return new BufferedOutputStream(openOutStream());
    }

    @Override
    public void write(ByteBuffer byteBuffer) throws IOException {
        //TODO buffer?
        byteBuffer.put(readAll());
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        FileUtil.writeToFile(file, bytes);
    }

    @Override
    public Writer openWriter() throws IOException {
        return new FileWriter(file);
    }

    @Override
    public boolean avoidBufferedWriter() {
        return false;
    }

    @Override
    public Writer openPreferredBufferedWriter() throws IOException {
        return new BufferedWriter(openWriter());
    }

    @Override
    public Writer openWriter(Charset charset) throws IOException {
        return new OutputStreamWriter(openOutStream(), charset);
    }

    @Override
    public Writer openPreferredBufferedWriter(Charset charset) throws IOException {
        return new BufferedWriter(openWriter(charset));
    }
}
