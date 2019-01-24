package io.github.splotycode.mosaik.domparsing.parsing.output;

import io.github.splotycode.mosaik.domparsing.parsing.DomSourceType;

import java.io.File;
import java.io.InputStream;

public interface DomOutput {

    void writeFile(File file);

    String getString();
    byte[] getBytes();

    InputStream getStream();

    DomSourceType getSourceType();


}
