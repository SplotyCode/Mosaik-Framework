package me.david.davidlib.runtime.parsing;

import me.david.davidlib.runtime.parsing.input.DomInput;
import me.david.davidlib.runtime.storage.Document;
import me.david.davidlib.util.reflection.classregister.IListClassRegister;

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
