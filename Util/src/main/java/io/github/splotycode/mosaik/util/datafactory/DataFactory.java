package io.github.splotycode.mosaik.util.datafactory;

import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode
public class DataFactory {

    private Map<String, Object> data = new HashMap<>();

    public DataFactory() {}

    public DataFactory(Map<String, Object> data) {
        this.data = data;
    }

    public <T> T getData(DataKey<T> key) {
        return getData(key.name, key);
    }

    public <T> T getDataDefault(DataKey<T> key) {
        return getDataDefault(key, null);
    }

    public <T> T getDataDefault(DataKey<T> key, T def) {
        return getData(key.name, key);
    }

    public <T> T getDataDefault(String name, DataKey<T> key, T def) {
        T result = (T) data.get(name);
        return result == null ? def : result;
    }

    public <T> T getDataDefault(String name, DataKey<T> key) {
        return getDataDefault(name, key, null);
    }



    /*
     * This will use the name instead of the key name
     * The Key is only for providing the Generic
     */
    @SuppressWarnings("unused")
    public <T> T getData(String name, DataKey<T> key) {
        T result = getDataDefault(name, key);
        if (result == null) throw new DataNotFoundException("Could not find data for " + name);
        return result;
    }

    public <T> void putData(DataKey<T> key, T obj) {
        putData(key.name, key, obj);
    }

    @SuppressWarnings("unused")
    public <T> void putData(String name, DataKey<T> key, T obj) {
        data.putIfAbsent(name, obj);
    }

    public Map<String, Object> getMap() {
        return data;
    }

    public Map<String, Object> getRawMap() {
        return data;
    }

    public void setMap(Map<String, Object> map) {
        data = map;
    }

    public boolean containsData(String name) {
        return data.containsKey(name);
    }

    public boolean containsData(DataKey<?> key) {
        return containsData(key.getName());
    }

    public int getDataSize() {
        return getMap().size();
    }
}
