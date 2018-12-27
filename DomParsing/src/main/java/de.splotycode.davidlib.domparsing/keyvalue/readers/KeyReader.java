package de.splotycode.davidlib.domparsing.keyvalue.readers;

import de.splotycode.davidlib.domparsing.keyvalue.KeyValueParser;
import de.splotycode.davidlib.domparsing.keyvalue.dom.KeyNode;
import de.splotycode.davidlib.domparsing.keyvalue.dom.ValueNode;
import me.david.davidlib.parsing.DomParseException;
import me.david.davidlib.parsing.DomReader;
import me.david.davidlib.storage.Node;
import me.david.davidlib.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import static de.splotycode.davidlib.domparsing.keyvalue.readers.KeyReader.State.*;

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
        boolean whiteSpace = StringUtil.isWhiteSpace(c);

        switch (state) {
            case BEFORE_NAME:
                if (!whiteSpace) {
                    state = NAME;
                    parser.rehandle();
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
                    parser.rehandle();
                    state = VALUE;
                }
                break;
            case VALUE:
                if (whiteSpace) {
                    List<Node> nodes = new ArrayList<>();
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
