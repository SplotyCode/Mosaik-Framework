package de.splotycode.davidlib.domparsing.keyvalue.readers;

import de.splotycode.davidlib.domparsing.keyvalue.KeyValueParser;
import me.david.davidlib.util.core.parsing.DomReader;

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
