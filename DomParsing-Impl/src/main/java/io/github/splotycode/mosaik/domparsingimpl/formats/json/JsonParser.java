package io.github.splotycode.mosaik.domparsingimpl.formats.json;

import io.github.splotycode.mosaik.domparsing.parsing.DefaultStringDomParser;
import io.github.splotycode.mosaik.domparsingimpl.dom.DefaultDocument;
import lombok.Getter;

public class JsonParser extends DefaultStringDomParser<DefaultDocument, JsonParser> {

    @Getter
    private DefaultDocument document = new DefaultDocument();

    public JsonParser() {
        this(false);
    }

    public JsonParser(boolean definitelyValid) {
        setReaders(new JsonReader(definitelyValid));
    }

    @Override
    protected DefaultDocument getResult() {
        return document;
    }
}
