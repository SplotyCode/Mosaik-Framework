package io.github.splotycode.mosaik.util.datafactory;

import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode
public class DataFactory implements DataFactoryComponent {

    protected Map<String, Object> data;

    public DataFactory() {
        data = new HashMap<>();
    }

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
        return getDataDefault(key.name, key, def);
    }

    public <T> T getDataDefault(String name, DataKey<T> key, T def) {
        Object result = data.get(name);
        return result == null ? def : (T) result;
    }

    public <T> T getDataDefault(String name, DataKey<T> key) {
        return getDataDefault(name, key, null);
    }

    /**
     * This will use the name instead of the key name
     * @param key only for providing the Generic type <code>T</code>
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
        data.put(name, obj);
    }

    public Map<String, Object> getMap() {
        return getRawMap();
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

    public boolean isEmpty() {
        return getDataSize() == 0;
    }

    @Override
    public DataFactory getDataFactory() {
        return this;
    }

}
