package io.github.splotycode.mosaik.domparsing.parsing;

import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.domparsing.parsing.input.DomInput;
import io.github.splotycode.mosaik.domparsing.parsing.input.DomUrlInput;
import io.github.splotycode.mosaik.domparsing.parsing.output.DomOutput;
import io.github.splotycode.mosaik.util.reflection.classregister.IListClassRegister;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

public interface ParsingManager extends IListClassRegister<ParsingHandle> {

    Collection<ParsingHandle> getHandles();

    <P extends ParsingHandle> P getHandleByClass(Class<P> clazz);
    ParsingHandle getHandle(File file);
    ParsingHandle getHandle(DomUrlInput url);

    /* Raw Input */
    <P extends Document> P parseDocument(DomInput input, ParsingHandle<P> handle);
    <P extends Document> P parseDocument(DomInput input, Class<? extends ParsingHandle<P>> handle);

    /* Physical File */
    Document parseDocument(File file);
    <P extends Document> P  parseDocument(File file, ParsingHandle<P> handle);
    <P extends Document> P  parseDocument(File file, Class<? extends ParsingHandle<P>> handle);

    /* Web Resource */
    Document parseDocument(URL url);
    <P extends Document> P  parseDocument(URL url, ParsingHandle<P> handle);
    <P extends Document> P  parseDocument(URL url, Class<? extends ParsingHandle<P>> handle);

    /* Resource in ClassLoader */
    Document parseResourceFile(String file);
    <P extends Document> P  parseDocument(String file, ParsingHandle<P> handle);
    <P extends Document> P  parseDocument(String file, Class<? extends ParsingHandle<P>> handle);

    <D extends Document> String writeToText(D document, ParsingHandle<D> handle);
    <D extends Document> String writeToText(D document, Class<? extends ParsingHandle<D>> handle);


    <D extends Document> void writeToFile(D document, File file, ParsingHandle<D> handle) throws IOException;
    <D extends Document> void writeToFile(D document, File file, Class<? extends ParsingHandle<D>> handle) throws IOException;
    <D extends Document> void writeToFile(D document, File file) throws IOException;

    /* Convert */
    <C extends Document, L extends Document> DomOutput convert(DomInput domInput, ParsingHandle<C> current, ParsingHandle<L> later);
    <C extends Document, L extends Document> void convert(File file, ParsingHandle<C> current, ParsingHandle<L> later);
    <L extends Document> void convert(File file, ParsingHandle<L> later);

}
