package me.david.davidlib.runtime.parsing;

public interface DomReader<T extends DomParser> {

    void readNext(char c, T parser) throws RuntimeException;

    void parseDone();

}
