package me.david.davidlib.link;

import me.david.davidlib.parsing.ParsingHandle;
import me.david.davidlib.parsing.input.DomInput;
import me.david.davidlib.storage.Document;
import me.david.davidlib.utils.reflection.classregister.IListClassRegister;

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
