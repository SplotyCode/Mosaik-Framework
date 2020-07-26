package io.github.splotycode.mosaik.util.io.copier;

import io.github.splotycode.mosaik.util.Reference;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CopyDestinations {

    // ByteDestinations

    public static ByteCopyDestination<Reference<Integer>> BYTE_COUNTER = new ByteCopyDestination<Reference<Integer>>() {
        @Override
        public Reference<Integer> create(int initialSize) throws IOException {
            return new Reference<>();
        }

        @Override
        public void copyTo(byte[] source, Reference<Integer> destination, int length) throws IOException {
            destination.setValue(destination.getValue() + length);
        }
    };

    public static final OutputStreamDestination EMPTY_OUTPUT_STREAM = OutputStreamDestination.EMPTY;
    public static final OutputStreamDestination<ByteArrayOutputStream> BYTE_ARRAY_OUTPUT_STREAM = OutputStreamDestination.BYTE_ARRAY;

    public static <S extends OutputStream> OutputStreamDestination<S> emptyOutputStream() {
        return OutputStreamDestination.empty();
    }

    public static <O extends OutputStream> OutputStreamDestination<O> wrapStream(O stream) {
        return OutputStreamDestination.wrapStream(stream);
    }

    public static final ByteBufferDestination EMPTY_BYTE_BUFFER = ByteBufferDestination.EMPTY;

    public static ByteBufferDestination byteBuffer(boolean direct) {
        return ByteBufferDestination.create(direct);
    }

    // CharDestinations

    public static CharCopyDestination<Reference<Integer>> CHAR_COUNTER = new CharCopyDestination<Reference<Integer>>() {
        @Override
        public Reference<Integer> create(int initialSize) throws IOException {
            return new Reference<>();
        }

        @Override
        public void copyTo(char[] source, Reference<Integer> destination, int length) throws IOException {
            destination.setValue(destination.getValue() + length);
        }
    };

    public static CharCopyDestination<Appendable> APPENDALE = new CharCopyDestination<Appendable>() {
        @Override
        public Appendable create(int initialSize) throws IOException {
            return new StringBuilder(initialSize);
        }

        @Override
        public void copyTo(char[] source, Appendable destination, int length) throws IOException {
            destination.append(new String(source), 0, length);
        }
    };

    public static final CharCopyDestination<Writer> EMPTY_WRITER = new CharCopyDestination<Writer>() {
        @Override
        public Writer create(int initialSize) throws IOException {
            throw new IllegalStateException("Destination is empty");
        }

        @Override
        public void copyTo(char[] source, Writer destination, int length) throws IOException {
            destination.write(source, 0, length);
        }
    };

    public static <D extends Writer> CharCopyDestination<D> emptyWriter() {
        //noinspection unchecked
        return (CharCopyDestination<D>) EMPTY_WRITER;
    }
}
