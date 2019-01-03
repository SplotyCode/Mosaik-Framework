package me.david.davidlib.runtimeapi.parsing;

import me.david.davidlib.runtimeapi.parsing.input.DomInput;
import me.david.davidlib.runtimeapi.storage.Document;

import java.util.List;

public interface DomParser<O extends Document, R extends DomParser> {

    O parse(DomInput input);
    int getIndex();
    char getChar();
    char getChar(int next);
    void rehandle();

    DomReader<R>[] getReaders();
    List<DomReader<R>> getActivReaders();
    void disableReader(DomReader<R> reader);

    void skip(int i);
    void skip();
    boolean skipIfFollow(String next);
    boolean skipIfFollowIgnoreCase(String text);

    void stopCurrent();

    void setLocked(DomReader<R> reader);
    boolean isLocked();
    DomReader<R> getLocked();

}
