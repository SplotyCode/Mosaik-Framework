package io.github.splotycode.mosaik.domparsing.annotation;

import java.io.InputStream;

public interface IEntryParser {

    Object toObject(InputStream stream);
    byte[] fromObject(Object object);

}
