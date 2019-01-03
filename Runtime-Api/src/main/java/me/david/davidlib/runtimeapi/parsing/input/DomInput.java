package me.david.davidlib.runtimeapi.parsing.input;

import me.david.davidlib.runtimeapi.parsing.DomSourceType;

import java.io.InputStream;

public interface DomInput {

    byte[] getBytes();
    String getString();
    InputStream getStream();

    DomSourceType getType();

}
