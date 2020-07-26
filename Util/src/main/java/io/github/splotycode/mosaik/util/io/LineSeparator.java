package io.github.splotycode.mosaik.util.io;

import io.github.splotycode.mosaik.util.collection.SimpleIterator;
import io.github.splotycode.mosaik.util.info.OperationSystem;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.Reader;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Getter
public enum LineSeparator {

    LF("\n"),
    CRLF("\r\n"),
    CR("\r");

    public static LineSeparator getDefault() {
        return OperationSystem.current().getSeparator();
    }

    private final String separator;
    private char[] chars;

    LineSeparator(String separator) {
        this.separator = separator;
        chars = separator.toCharArray();
    }

    public Stream<String> streamLines(Reader reader) {
        char[] separator = getChars();
        StringBuilder builder = new StringBuilder();
        Iterator<String> iter = new SimpleIterator<String>() {
            @Override
            @SneakyThrows
            protected boolean provideNext() {
                int currentChar;
                int separatorIndex = 0;
                while ((currentChar = reader.read()) != -1) {
                    if (currentChar == separator[separatorIndex]) {
                        separatorIndex++;
                        if (separatorIndex == separator.length) {
                            builder.setLength(builder.length() - separator.length + 1);
                            next = builder.toString();
                            builder.setLength(0);
                            return true;
                        }
                    } else {
                        separatorIndex = 0;
                    }
                    builder.append((char) currentChar);
                }
                if (builder.length() != 0) {
                    next = builder.toString();
                    builder.setLength(0);
                    return true;
                }
                return false;
            }
        };

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                iter, Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.NONNULL), false);
    }
}
