package me.david.davidlib.runtime.parsing.input;

import me.david.davidlib.runtime.parsing.DomSourceType;

import java.io.InputStream;

public interface DomInput {

    byte[] getBytes();
    String getString();
    InputStream getStream();

    DomSourceType getType();

}
