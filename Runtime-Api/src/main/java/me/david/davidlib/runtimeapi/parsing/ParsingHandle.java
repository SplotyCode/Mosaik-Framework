package me.david.davidlib.runtimeapi.parsing;

import me.david.davidlib.runtimeapi.parsing.input.DomInput;

public interface ParsingHandle {

    String[] getFileTypes();
    String[] getMimeTypes();

    DomParser getParser(DomInput input);

}
