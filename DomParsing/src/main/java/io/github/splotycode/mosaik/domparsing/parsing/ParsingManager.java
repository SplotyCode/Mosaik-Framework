package io.github.splotycode.mosaik.domparsing.parsing;

import io.github.splotycode.mosaik.domparsing.parsing.input.DomInput;
import io.github.splotycode.mosaik.runtime.storage.Document;
import io.github.splotycode.mosaik.util.reflection.classregister.IListClassRegister;

import java.io.File;
import java.net.URL;
import java.util.Collection;

public interface ParsingManager extends IListClassRegister<ParsingHandle> {

    Collection<ParsingHandle> getHandles();

    Document parseDocument(DomInput input, ParsingHandle handle);
    Document parseDocument(File file);
    Document parseDocument(URL url);
    Document parseResourceFile(String file);

}
