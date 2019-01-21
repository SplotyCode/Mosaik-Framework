package io.github.splotycode.mosaik.domparsing.keyvalue;

import io.github.splotycode.mosaik.domparsing.keyvalue.dom.KeyValueDocument;
import io.github.splotycode.mosaik.domparsing.keyvalue.readers.CommentReader;
import io.github.splotycode.mosaik.domparsing.keyvalue.readers.KeyReader;
import lombok.Getter;
import io.github.splotycode.mosaik.runtime.parsing.DefaultStringDomParser;
import io.github.splotycode.mosaik.runtime.parsing.DomParseException;
import io.github.splotycode.mosaik.runtime.parsing.DomReader;
import io.github.splotycode.mosaik.runtime.parsing.input.DomInput;

public class KeyValueParser extends DefaultStringDomParser<KeyValueDocument, KeyValueParser> {

    @Getter private KeyValueDocument document = new KeyValueDocument();

    @Override
    public KeyValueDocument parse(DomInput input) {
        content = input.getString();
        while (index < content.length()){
            char c = content.charAt(index);
            if(isLocked()) getLocked().readNext(c, this);
            else for(DomReader<KeyValueParser> reader : getActivReaders()) {
                try {
                    reader.readNext(c, this);
                }catch (Throwable throwable){
                    throw new DomParseException("Exception in readNext() method", throwable);
                }
                if(skipThis) {
                    skipThis = false;
                    break;
                }
            }
            if(rehandle) rehandle = false;
            else {
                if(c == '\n') line++;
                index++;
            }
        }
        for(DomReader<KeyValueParser> reader : getReaders())
            reader.parseDone();
        return document;
    }

    private DomReader<KeyValueParser>[] readers = new DomReader[] {new KeyReader(), new CommentReader()};

    @Override
    public DomReader<KeyValueParser>[] getReaders() {
        return readers;
    }
}
