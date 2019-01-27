package io.github.splotycode.mosaik.domparsing.annotation.parsing;

import io.github.splotycode.mosaik.domparsing.annotation.IEntryParser;

import java.io.*;

public class SerialisedEntryParser implements IEntryParser {

    @Override
    public Object toObject(InputStream stream) {
        try {
            ObjectInputStream is = new ObjectInputStream(stream);
            Object obj = is.readObject();
            is.close();
            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] fromObject(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
