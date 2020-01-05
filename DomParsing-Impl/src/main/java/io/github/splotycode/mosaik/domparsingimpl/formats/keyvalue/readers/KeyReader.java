package io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.readers;

import io.github.splotycode.mosaik.domparsing.dom.value.StringValueNode;
import io.github.splotycode.mosaik.domparsing.parsing.DomParseException;
import io.github.splotycode.mosaik.domparsing.parsing.DomReader;
import io.github.splotycode.mosaik.domparsingimpl.dom.DefaultIdentifierNode;
import io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.KeyValueParser;

import static io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.readers.KeyReader.State.*;

public class KeyReader implements DomReader<KeyValueParser> {

    private String name = "", value = "";
    private State state = State.BEFORE_NAME;

    public enum State {

        BEFORE_NAME,
        NAME,
        BEFORE_SEPARATOR,
        BEFORE_VALUE,
        VALUE

    }

    @Override
    public void readNext(char c, KeyValueParser parser) throws RuntimeException {
        boolean whiteSpace = Character.isWhitespace(c);

        switch (state) {
            case BEFORE_NAME:
                if (!whiteSpace) {
                    state = NAME;
                    parser.reHandle();
                }
                break;
            case NAME:
                if (whiteSpace) {
                    state = BEFORE_SEPARATOR;
                } else if (c == ':') {
                    state = BEFORE_VALUE;
                } else name += c;
                break;
            case BEFORE_SEPARATOR:
                if (c == ':') state = BEFORE_VALUE;
                break;
            case BEFORE_VALUE:
                if (!whiteSpace) {
                    parser.reHandle();
                    state = VALUE;
                }
                break;
            case VALUE:
                if (whiteSpace) {
                    parser.getDocument().addNode(new DefaultIdentifierNode(name, new StringValueNode(value)));

                    value = name = "";
                    state = BEFORE_NAME;
                } else value += c;
                break;
        }
    }

    @Override
    public void parseDone() {
        if (state != BEFORE_NAME) throw new DomParseException("Unexpected EOF");
    }

}
