package io.github.splotycode.mosaik.domparsingimpl.formats.json;

import io.github.splotycode.mosaik.domparsing.dom.value.BooleanValueNode;
import io.github.splotycode.mosaik.domparsing.dom.value.NullValueNode;
import io.github.splotycode.mosaik.domparsing.dom.value.StringValueNode;
import io.github.splotycode.mosaik.domparsing.parsing.DomReader;
import io.github.splotycode.mosaik.domparsingimpl.dom.DefaultDocument;
import io.github.splotycode.mosaik.domparsingimpl.dom.DefaultDocumentSectionNode;
import io.github.splotycode.mosaik.domparsingimpl.dom.DefaultIdentifierNode;

import java.math.BigInteger;
import java.util.Stack;

/** TODO:
 *  - Support arrays
 *  - Support numbers (also 2e2)
 *  - Support arrays without document
 *  - Support escape in keys
 */
public class JsonReader implements DomReader<JsonParser> {

    private Stack<DefaultDocumentSectionNode> openNodes = new Stack<>();
    private DefaultIdentifierNode currentIdentifier;

    private boolean definitelyValid;

    public JsonReader(boolean definitelyValid) {
        this.definitelyValid = definitelyValid;
    }

    private Status status;

    private int marker;
    private StringBuilder valueCapture = new StringBuilder();

    private enum Status {

        PRE_DOC,
        PRE_KEY,
        KEY,
        POST_KEY,
        PRE_VALUE,
        VALUE,
        VALUE_ESCAPE,
        POST_VALUE,
        END

    }

    @Override
    public void readNext(char c, JsonParser parser) throws RuntimeException {
        switch (status) {
            case PRE_DOC:
                if (c == '{') {
                    status = Status.PRE_KEY;
                } else if (!Character.isWhitespace(c)) {
                    throw new IllegalStateException("Expected openNodes start ('{')");
                }
                break;
            case PRE_KEY:
                if (c == '"') {
                    status = Status.KEY;
                    marker = parser.getIndex() + 1;
                } else if (c == '}') {
                    endObject();
                } else if (!Character.isWhitespace(c)) {
                    throw new IllegalStateException("Expected identifier start ('\"')");
                }
                break;
            case KEY:
                if (c == '"') {
                    String key = parser.getContent().substring(marker, parser.getIndex());
                    currentIdentifier = new DefaultIdentifierNode(key);
                    openNodes.peek().addNode(currentIdentifier);
                    status = Status.POST_KEY;
                }
                break;
            case POST_KEY:
                if (c == ':') {
                    status = Status.PRE_VALUE;
                } else if (!Character.isWhitespace(c)) {
                    throw new IllegalStateException("Expected ':' after separator");
                }
                break;
            case PRE_VALUE:
                switch (c) {
                    case '"':
                        status = Status.VALUE;
                        marker = parser.getIndex() + 1;
                        break;
                    case '{':
                        DefaultDocumentSectionNode section = new DefaultDocumentSectionNode();
                        currentIdentifier.addChild(section);
                        openNodes.push(section);
                        status = Status.PRE_KEY;
                        break;
                    case 'n':
                        if (mightCheckKeyword("ull", parser)) {
                            throw new IllegalStateException("Expected null");
                        }
                        currentIdentifier.addChild(NullValueNode.INSTANCE);
                        status = Status.POST_VALUE;
                        break;
                    case 't':
                        if (mightCheckKeyword("rue", parser)) {
                            throw new IllegalStateException("Expected true");
                        }
                        currentIdentifier.addChild(BooleanValueNode.TRUE);
                        status = Status.POST_VALUE;
                        break;
                    case 'f':
                        if (mightCheckKeyword("alse", parser)) {
                            throw new IllegalStateException("Expected false");
                        }
                        currentIdentifier.addChild(BooleanValueNode.FALSE);
                        status = Status.POST_VALUE;
                        break;
                    default:
                        if (!Character.isWhitespace(c)) {
                            throw new IllegalStateException("Expected identifier start ('\"')");
                        }
                        break;
                }
                break;
            case VALUE:
                if (c == '"') {
                    valueCapture.append(parser.getContent(), marker, parser.getIndex());
                    String value = valueCapture.toString();
                    valueCapture.setLength(0);
                    currentIdentifier.addChild(new StringValueNode(value));
                    status = Status.POST_VALUE;
                } else if (c == '\\') {
                    valueCapture.append(parser.getContent(), marker, parser.getIndex());
                    status = Status.VALUE_ESCAPE;
                }
                break;
            case VALUE_ESCAPE:
                switch (c) {
                    case '"':
                    case '\\':
                    case '/':
                        valueCapture.append(c);
                        break;
                    case 'b':
                        valueCapture.append('\b');
                        break;
                    case 'f':
                        valueCapture.append('\f');
                        break;
                    case 'n':
                        valueCapture.append('\n');
                        break;
                    case 'r':
                        valueCapture.append('\r');
                        break;
                    case 't':
                        valueCapture.append('\t');
                        break;
                    case 'u':
                        String unicode = parser.getContent().substring(parser.getIndex() + 1, parser.getIndex() + 5);
                        parser.skip(4);
                        valueCapture.append((char) new BigInteger(unicode, 16).intValue());
                        break;
                    default:
                        throw new IllegalStateException("Invalid character after escape: " + c);
                }
                marker = parser.getIndex() + 1;
                status = Status.VALUE;
                break;
            case POST_VALUE:
                if (c == ',') {
                    status = Status.PRE_KEY;
                } else if (c == '}') {
                    endObject();
                }  else if (!Character.isWhitespace(c)) {
                    throw new IllegalStateException("Expected document end ('}') or , got " + c);
                }
                break;
            case END:
                if (!Character.isWhitespace(c)) {
                    throw new IllegalStateException("Main object is closed");
                }
                break;
        }
    }

    private void endObject() {
        if (openNodes.size() == 1) {
            status = Status.END;
        }
        openNodes.pop();
    }

    private boolean mightCheckKeyword(String keyword, JsonParser parser) {
        if (definitelyValid) {
            parser.skip(keyword.length());
            return false;
        }

        parser.skip();
        return !parser.skipIfFollow(keyword);
    }

    @Override
    public void parseDone(JsonParser parser) {
        String message = null;
        if (!openNodes.isEmpty()) {
            message = " " + openNodes.size() + " Objects are not closed";
            openNodes.clear();
        }

        if (status != Status.END) {
            String finalMsg = "Unexpected end of document (status: " + status + ")";
            if (message != null) {
                finalMsg += message;
            }
            throw new IllegalArgumentException(finalMsg);
        }
    }

    @Override
    public void parseInit(JsonParser parser) {
        /* This is the only reader so we can save performance */
        parser.setLocked(this);

        DefaultDocument document = parser.getDocument();
        openNodes.push(document);

        status = Status.PRE_DOC;
    }
}
