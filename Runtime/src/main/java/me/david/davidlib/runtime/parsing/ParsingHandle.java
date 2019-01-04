package me.david.davidlib.runtime.parsing;

import me.david.davidlib.runtime.parsing.input.DomInput;

public interface ParsingHandle {

    String[] getFileTypes();
    String[] getMimeTypes();

    DomParser getParser(DomInput input);

}
