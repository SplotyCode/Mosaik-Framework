package io.github.splotycode.mosaik.domparsing.keyvalue.readers;

import io.github.splotycode.mosaik.domparsing.keyvalue.KeyValueParser;
import io.github.splotycode.mosaik.runtime.parsing.DomReader;

public class CommentReader implements DomReader<KeyValueParser> {

    @Override
    public void readNext(char c, KeyValueParser parser) throws RuntimeException {
        if (parser.isLocked()) {
            if (c == '\n') {
                parser.setLocked(null);
            }
        } else if (c == '#') {
            parser.setLocked(this);
        }
    }

    @Override public void parseDone() {}

}
