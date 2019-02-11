package io.github.splotycode.mosaik.domparsing.annotation;

@SuppressWarnings("unused")
public interface EntryListener {

    void preSerialisation();
    void postSerialisation();

    void postDeserialization();

}
