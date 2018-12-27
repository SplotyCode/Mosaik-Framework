package de.splotycode.davidlib.domparsing.keyvalue;

import de.splotycode.davidlib.domparsing.keyvalue.dom.KeyValueDocument;
import de.splotycode.davidlib.domparsing.keyvalue.readers.CommentReader;
import de.splotycode.davidlib.domparsing.keyvalue.readers.KeyReader;
import lombok.Getter;
import me.david.davidlib.parsing.DefaultStringDomParser;
import me.david.davidlib.parsing.DomParseException;
import me.david.davidlib.parsing.DomReader;
import me.david.davidlib.parsing.input.DomInput;

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
