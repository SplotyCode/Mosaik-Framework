package io.github.splotycode.mosaik.domparsing.parsing;

public interface DomReader<T extends DomParser> {

    void readNext(char c, T parser) throws RuntimeException;

    void parseDone(T parser);
    default void parseInit(T parser) {}

}
