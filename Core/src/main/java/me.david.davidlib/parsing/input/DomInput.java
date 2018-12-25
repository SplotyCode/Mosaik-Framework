package me.david.davidlib.parsing.input;

import me.david.davidlib.parsing.DomSourceType;

import java.io.InputStream;

public interface DomInput {

    byte[] getBytes();
    String getString();
    InputStream getStream();

    DomSourceType getType();

}
