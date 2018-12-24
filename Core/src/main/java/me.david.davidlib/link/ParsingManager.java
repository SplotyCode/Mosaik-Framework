package me.david.davidlib.link;

import me.david.davidlib.parsing.ParsingHandle;
import me.david.davidlib.parsing.input.DomInput;
import me.david.davidlib.storage.Document;

import java.net.URL;
import java.util.Collection;

public interface ParsingManager {

    Collection<ParsingHandle> getHandles();

    Document parseDocument(DomInput input, ParsingHandle handle);
    Document parseDocument(String file);
    Document parseDocument(URL url);

}
