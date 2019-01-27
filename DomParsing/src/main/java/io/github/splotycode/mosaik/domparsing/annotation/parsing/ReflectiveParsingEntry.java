package io.github.splotycode.mosaik.domparsing.annotation.parsing;

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
        return toObj.apply(stream);
    }

    @Override
    public byte[] fromObject(Object object) {
        return fromObj.fromObj(object);
    }
}
