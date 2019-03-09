package io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.readers;

import io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.KeyValueParser;
import io.github.splotycode.mosaik.domparsing.parsing.DomReader;

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
