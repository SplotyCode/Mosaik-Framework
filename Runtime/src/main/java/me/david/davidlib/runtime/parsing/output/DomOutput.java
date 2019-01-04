package me.david.davidlib.runtime.parsing.output;

import me.david.davidlib.runtime.parsing.DomSourceType;

import java.io.File;
import java.io.InputStream;

public interface DomOutput {

    void writeFile(File file);

    String getString();
    byte[] getBytes();

    InputStream getStream();

    DomSourceType getSourceType();


}
