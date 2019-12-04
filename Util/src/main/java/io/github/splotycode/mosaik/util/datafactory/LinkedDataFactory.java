package io.github.splotycode.mosaik.util.datafactory;

import io.github.splotycode.mosaik.util.collection.CollectionUtil;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class LinkedDataFactory extends DataFactory {

    private DataFactory linked;

    @Override
    public boolean containsData(String name) {
        return super.containsData(name) || linked.containsData(name);
    }


    @Override
    public <T> T getDataDefault(String name, DataKey<T> key, T def) {
        T obj = super.getDataDefault(name, key, null);
        if (obj == null) {
            obj = linked.getDataDefault(name, key, null);
        }
        if (obj == null) obj = def;
        return obj;
    }

    @Override
    public Map<String, Object> getMap() {
        return CollectionUtil.mergeMaps(getRawMap(), linked.getMap());
    }

}
