package io.github.splotycode.mosaik.domparsing.annotation.parsing;

import io.github.splotycode.mosaik.domparsing.annotation.EntryListener;
import io.github.splotycode.mosaik.domparsing.annotation.EntryParseException;
import io.github.splotycode.mosaik.domparsing.annotation.IEntryParser;

import java.io.*;

public class SerialisedEntryParser implements IEntryParser {

    @Override
    public Object toObject(InputStream stream) {
        try {
            ObjectInputStream is = new ObjectInputStream(stream);
            Object obj = is.readObject();
            if (obj instanceof EntryListener) {
                ((EntryListener) obj).postDeserialization();
            }
            is.close();
            return obj;
        } catch (Exception ex) {
            throw new EntryParseException("Failed to get Obect out of Stream", ex);
        }
    }

    @Override
    public byte[] fromObject(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            EntryListener listener = null;
            if (object instanceof EntryListener) {
                listener = (EntryListener) object;
                listener.preSerialisation();
            }
            oos.writeObject(object);
            byte[] result = baos.toByteArray();
            if (listener != null) {
                listener.postSerialisation();
            }
            oos.close();
            return result;
        } catch (IOException e) {
            throw new EntryParseException("Failed to save Object", e);
        }
    }
}
