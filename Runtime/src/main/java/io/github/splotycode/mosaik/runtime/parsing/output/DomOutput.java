package io.github.splotycode.mosaik.runtime.parsing.output;

import io.github.splotycode.mosaik.runtime.parsing.DomSourceType;

import java.io.File;
import java.io.InputStream;

public interface DomOutput {

    void writeFile(File file);

    String getString();
    byte[] getBytes();

    InputStream getStream();

    DomSourceType getSourceType();


}
