package io.github.splotycode.mosaik.domparsingimpl.annotation.parsing;

import io.github.splotycode.mosaik.domparsing.annotation.EntryParseExcpetion;
import io.github.splotycode.mosaik.domparsing.annotation.parsing.ReflectiveEntryData;
import io.github.splotycode.mosaik.domparsing.annotation.parsing.ReflectiveParsingEntry;
import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.domparsing.dom.Node;
import io.github.splotycode.mosaik.domparsing.parsing.input.DomInput;
import io.github.splotycode.mosaik.domparsing.parsing.input.DomStreamInput;
import io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.KeyValueHandle;
import io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.dom.KeyValueDocument;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReflectiveParsingHelper {

    private static Map<String, ReflectiveEntryData> data = new HashMap<>();

    public static ReflectiveEntryData getData(Class clazz) {
        ReflectiveEntryData entry = data.get(clazz.getName());
        if (entry == null) {
            entry = new ReflectiveEntryData(clazz);
            data.put(clazz.getName(), entry);
        }
        return entry;
    }

    public static void initialize() {
        ReflectiveParsingEntry.setFromObj(ReflectiveParsingHelper::fromObject);
        ReflectiveParsingEntry.setToObj(ReflectiveParsingHelper::toObject);
    }

    public static Object toObject(InputStream stream) {
        DomInput input = new DomStreamInput(stream);
        Document document = LinkBase.getInstance().getLink(Links.PARSING_MANAGER).parseDocument(input, KeyValueHandle.class);

        try {
            Class clazz = Class.forName(document.getFirstTextFromNode("__clazz__"));
            Object obj = clazz.newInstance();

            for (Node node : document.getNodes()) {
                if (node.name().equals("__clazz__")) continue;
                String key = node.name();
                String value = document.getFirstTextFromNode(key);

                Field field = clazz.getDeclaredField(getData(clazz).getFieldName(key));
                field.setAccessible(true);
                field.set(obj, LinkBase.getInstance().getLink(TransformerManager.LINK).transform(value, field.getType()));
            }
            stream.close();
            return obj;
        } catch (Exception ex) {
            throw new EntryParseExcpetion("Failed to provide Object", ex);
        }
    }

    public static byte[] fromObject(Object object) {
        try {
            KeyValueDocument document = new KeyValueDocument();
            document.addNodeWithInnerText("_clazz__", object.getClass().getName());
            for (Map.Entry<String, String> node : getData(object.getClass()).entrySet()) {
                Field field = object.getClass().getDeclaredField(node.getValue());
                field.setAccessible(true);
                document.addNodeWithInnerText(node.getKey(), LinkBase.getInstance().getLink(TransformerManager.LINK).transform(field.get(object), String.class));
            }

            return LinkBase.getInstance().getLink(Links.PARSING_MANAGER).writeToText(document, KeyValueHandle.class).getBytes();
        } catch (ReflectiveOperationException ex) {
            throw new EntryParseExcpetion("Failed to parse Obect to byte[]", ex);
        }
    }

}
