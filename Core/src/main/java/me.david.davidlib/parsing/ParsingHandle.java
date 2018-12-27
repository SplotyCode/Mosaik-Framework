package me.david.davidlib.parsing;

import me.david.davidlib.parsing.input.DomInput;

public interface ParsingHandle {

    String[] getFileTypes();
    String[] getMimeTypes();

    DomParser getParser(DomInput input);

}
