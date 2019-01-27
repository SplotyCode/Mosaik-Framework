package io.github.splotycode.mosaik.domparsing.annotation.parsing;

import io.github.splotycode.mosaik.domparsing.annotation.IEntry;
import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ReflectiveEntry implements IEntry {

    private static Map<String, ReflectiveEntryData> data = new HashMap<>();
    @Getter @Setter private static Function<Pair<ReflectiveEntryData, ReflectiveEntry>, Document> readFun;
    @Getter @Setter private static Consumer<Pair<ReflectiveEntryData, Document>> writeFun;

    public static ReflectiveEntryData getData(Class clazz) {
        ReflectiveEntryData entry = data.get(clazz.getName());
        if (entry == null) {
            entry = new ReflectiveEntryData(clazz);
            data.put(clazz.getName(), entry);
        }
        return entry;
    }

    @Override
    public Document read() {
        return readFun.apply(new Pair<>(getData(getClass()), this));
    }

    @Override
    public void write(Document document) {
        ReflectiveEntryData entryData = getData(getClass());
        writeFun.accept(new Pair<>(entryData, document));
    }

}
