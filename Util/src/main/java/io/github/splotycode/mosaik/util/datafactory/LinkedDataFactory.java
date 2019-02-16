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
    public <T> T getData(String name, DataKey<T> key) {
        T obj = super.getData(name, key);
        if (obj == null) {
            obj = linked.getData(name, key);
        }
        return obj;
    }

    @Override
    public Map<String, Object> getMap() {
        return CollectionUtil.combind(getRawMap(), linked.getMap());
    }

}
