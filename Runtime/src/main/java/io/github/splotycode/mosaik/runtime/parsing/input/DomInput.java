package io.github.splotycode.mosaik.runtime.parsing.input;

import io.github.splotycode.mosaik.runtime.parsing.DomSourceType;

import java.io.InputStream;

public interface DomInput {

    byte[] getBytes();
    String getString();
    InputStream getStream();

    DomSourceType getType();

}
