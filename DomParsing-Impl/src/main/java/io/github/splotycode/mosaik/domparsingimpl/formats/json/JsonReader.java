package io.github.splotycode.mosaik.domparsingimpl.formats.json;

import io.github.splotycode.mosaik.domparsing.dom.value.StringValueNode;
import io.github.splotycode.mosaik.domparsing.parsing.DomReader;
import io.github.splotycode.mosaik.domparsingimpl.dom.DefaultDocument;
import io.github.splotycode.mosaik.domparsingimpl.dom.DefaultDocumentSectionNode;
import io.github.splotycode.mosaik.domparsingimpl.dom.DefaultIdentifierNode;

import java.util.Stack;

/** TODO:
 *  - Support arrays
 *  - Support null and numbers (also 2e2)
 *  - Support arrays without document
 *  - Support string: hex and escape
 */
public class JsonReader implements DomReader<JsonParser> {

    private Stack<DefaultDocumentSectionNode> openNodes = new Stack<>();
    private DefaultIdentifierNode currentIdentifier;

    private Status status;

    private int marker;

    private enum Status {

        PRE_DOC,
        PRE_KEY,
        KEY,
        POST_KEY,
        PRE_VALUE,
        VALUE,
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
                if (c == '"') {
                    status = Status.VALUE;
                    marker = parser.getIndex() + 1;
                }  else if (c == '{') {
                    DefaultDocumentSectionNode section = new DefaultDocumentSectionNode();
                    currentIdentifier.addChild(section);
                    openNodes.push(section);
                    status = Status.PRE_KEY;
                } else if (!Character.isWhitespace(c)) {
                    throw new IllegalStateException("Expected identifier start ('\"')");
                }
                break;
            case VALUE:
                if (c == '"') {
                    String value = parser.getContent().substring(marker, parser.getIndex());
                    currentIdentifier.addChild(new StringValueNode(value));
                    status = Status.POST_VALUE;
                }
                break;
            case POST_VALUE:
                if (c == ',') {
                    status = Status.PRE_KEY;
                } else if (c == '}') {
                    int nodeSize = openNodes.size();
                    if (nodeSize == 1) {
                        status = Status.END;
                    }
                    if (openNodes.size() == 0) {
                        throw new IllegalStateException("No Document to close");
                    }
                    openNodes.pop();
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
        DefaultDocument document = parser.getDocument();
        openNodes.push(document);

        status = Status.PRE_DOC;
    }
}
