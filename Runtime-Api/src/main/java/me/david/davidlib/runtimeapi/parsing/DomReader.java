package me.david.davidlib.runtimeapi.parsing;

public interface DomReader<T extends DomParser> {

    void readNext(char c, T parser) throws RuntimeException;

    void parseDone();

}
