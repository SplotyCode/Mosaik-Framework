package io.github.splotycode.mosaik.domparsing.parsing;

import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.domparsing.parsing.input.DomInput;

import java.util.List;

public interface DomParser<O extends Document, R extends DomParser> {

    O parse(DomInput input);
    int getIndex();
    char getChar();
    char getChar(int next);
    void reHandle();

    DomReader<R>[] getReaders();
    DomReader<R> getInstance(Class<DomReader<R>> clazz);

    List<DomReader<R>> getActiveReaders();
    void disableReader(DomReader<R> reader);
    void enableReader(DomReader<R> reader);

    void skip(int i);
    void skip();
    boolean skipIfFollow(char next);
    boolean skipIfFollow(String next);
    boolean skipIfFollowIgnoreCase(String text);

    void stopCurrent();

    void setLocked(DomReader<R> reader);
    boolean isLocked();
    DomReader<R> getLocked();

}
