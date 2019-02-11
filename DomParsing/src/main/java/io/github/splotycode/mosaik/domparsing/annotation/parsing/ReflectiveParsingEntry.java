package io.github.splotycode.mosaik.domparsing.annotation.parsing;

import io.github.splotycode.mosaik.domparsing.annotation.EntryListener;
import io.github.splotycode.mosaik.domparsing.annotation.IEntryParser;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.util.function.Function;

public class ReflectiveParsingEntry implements IEntryParser {

    @Getter @Setter private static Function<InputStream, Object> toObj;
    @Getter @Setter private static FromObject fromObj;

    public interface FromObject {
        byte[] fromObj(Object obj);
    }

    @Override
    public Object toObject(InputStream stream) {
        Object object = toObj.apply(stream);
        if (object instanceof EntryListener) {
            ((EntryListener) object).postDeserialization();
        }
        return object;
    }

    @Override
    public byte[] fromObject(Object object) {
        EntryListener listener = null;
        if (object instanceof EntryListener) {
            listener = (EntryListener) object;
            listener.preSerialisation();
        }
        byte[] result = fromObj.fromObj(object);
        if (listener != null) {
            listener.postSerialisation();
        }
        return result;
    }
}
