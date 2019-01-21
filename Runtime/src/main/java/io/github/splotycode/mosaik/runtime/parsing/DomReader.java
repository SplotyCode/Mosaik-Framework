package io.github.splotycode.mosaik.runtime.parsing;

public interface DomReader<T extends DomParser> {

    void readNext(char c, T parser) throws RuntimeException;

    void parseDone();

}
