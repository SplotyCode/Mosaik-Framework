package io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.readers;

import io.github.splotycode.mosaik.domparsing.parsing.DomParseException;
import io.github.splotycode.mosaik.domparsing.parsing.DomReader;
import io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.KeyValueParser;
import io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.dom.KeyNode;
import io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.dom.ValueNode;
import io.github.splotycode.mosaik.util.node.NameableNode;

import java.util.ArrayList;
import java.util.List;

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
                    List<NameableNode> nodes = new ArrayList<>();
                    KeyNode key = new KeyNode(name, nodes);
                    nodes.add(new ValueNode(key, value));

                    parser.getDocument().getNodes().add(key);

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
