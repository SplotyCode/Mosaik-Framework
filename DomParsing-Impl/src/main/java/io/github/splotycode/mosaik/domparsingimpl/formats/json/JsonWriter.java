package io.github.splotycode.mosaik.domparsingimpl.formats.json;

import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.domparsing.dom.DocumentSectionNode;
import io.github.splotycode.mosaik.domparsing.dom.IdentifierNode;
import io.github.splotycode.mosaik.domparsing.dom.value.NumberValueNode;
import io.github.splotycode.mosaik.domparsing.dom.value.StringValueNode;
import io.github.splotycode.mosaik.domparsing.dom.value.ValueNode;
import io.github.splotycode.mosaik.domparsing.parsing.output.DomOutput;
import io.github.splotycode.mosaik.domparsing.parsing.output.DomStringOutput;
import io.github.splotycode.mosaik.domparsing.writing.DomWriter;

public class JsonWriter implements DomWriter {

    @Override
    public DomOutput write(Document document) {
        StringBuilder builder = new StringBuilder();
        writeObject(builder, document);
        return new DomStringOutput(builder.toString());
    }

    private void writeIdentifier(StringBuilder builder, IdentifierNode identifier) {
        builder.append('"').append(escape(identifier.name())).append('"').append(": ");
        writeValue(builder, identifier);
        builder.append(", ");
    }

    private void writeValue(StringBuilder builder, IdentifierNode identifier) {
        int childes = identifier.getChildes().size();
        if (childes == 0) {
            builder.append("[]");
            return;
        }
        boolean array = childes > 1;
        if (array) {
            builder.append('[');
        }
        for (ValueNode valueNode : identifier.getChildes()) {
            if (valueNode instanceof DocumentSectionNode) {
                writeObject(builder, (DocumentSectionNode) valueNode);
            } else if (valueNode instanceof StringValueNode) {
                builder.append(escape(((StringValueNode) valueNode).getValue()));
            } else if (valueNode instanceof NumberValueNode) {
                double number = ((NumberValueNode) valueNode).getValue();
                if (Double.isNaN(number) || Double.isInfinite(number)) {
                    builder.append("null");
                } else {
                    builder.append(number);
                }
            } else if (valueNode.supportsToString()) {
                builder.append(valueNode.toString());
            } else {
                throw new IllegalStateException("Failed to write value of type: " + valueNode.getClass().getSimpleName());
            }
            if (array) {
                builder.append(", ");
            }
        }
        if (array) {
            builder.setLength(builder.length() - 2);
            builder.append(']');
        }
    }

    private String escape(String str) {
        int length = str.length();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            switch (c) {
                case '\\':
                case '/':
                case '"':
                    builder.append('\\').append(c);
                    break;
                case '\b':
                    builder.append("\\b");
                    break;
                case '\t':
                    builder.append("\\t");
                    break;
                case '\n':
                    builder.append("\\n");
                    break;
                case '\f':
                    builder.append("\\f");
                    break;
                case '\r':
                    builder.append("\\r");
                    break;
                default:
                    if (c < ' ') {
                        String unicode = "000" + Integer.toHexString(c);
                        builder.append("\\u").append(unicode.substring(unicode.length() - 4));
                    } else {
                        builder.append(c);
                    }
            }
        }
        return builder.toString();
    }

    private void writeObject(StringBuilder builder, DocumentSectionNode object) {
        builder.append('{');
        for (IdentifierNode identifierNode : object.getNodes()) {
            writeIdentifier(builder, identifierNode);
        }
        if (!object.getNodes().isEmpty()) {
            builder.setLength(builder.length() - 2);
        }
        builder.append('}');
    }

}
