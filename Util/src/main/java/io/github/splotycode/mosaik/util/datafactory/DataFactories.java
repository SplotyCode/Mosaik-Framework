package io.github.splotycode.mosaik.util.datafactory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataFactories {

    public static final EmptyDataFactory EMPTY_DATA_FACTORY = new EmptyDataFactory();

    public static SingletonDataFactory singletonDataFactory(String key, Object value) {
        return new SingletonDataFactory(key, value);
    }

    public static <T> SingletonDataFactory singletonDataFactory(DataKey<T> key, T value) {
        return new SingletonDataFactory(key.getName(), value);
    }

    private static class EmptyDataFactory extends DataFactory {

        EmptyDataFactory() {
            super(Collections.emptyMap());
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public int getDataSize() {
            return 0;
        }

        @Override
        public boolean containsData(String name) {
            return false;
        }

        @Override
        public <T> T getDataDefault(String name, DataKey<T> key, T def) {
            return def;
        }

        @Override
        public <T> void putData(String name, DataKey<T> key, T obj) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setMap(Map<String, Object> map) {
            throw new UnsupportedOperationException();
        }

    }

    private static class SingletonDataFactory extends DataFactory {

        private String key;
        private Object object;

        public SingletonDataFactory(String key, Object object) {
            super(null);
            this.key = key;
            this.object = object;
        }

        @Override
        public int getDataSize() {
            return 1;
        }

        @Override
        public <T> T getDataDefault(String name, DataKey<T> key, T def) {
            if (name.equals(this.key)) {
                return (T) object;
            }
            return def;
        }

        @Override
        public boolean containsData(String name) {
            return name.equals(key);
        }

        @Override
        public <T> void putData(String name, DataKey<T> key, T obj) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, Object> getRawMap() {
            if (data == null) {
                data = Collections.singletonMap(key, object);
            }
            return data;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void setMap(Map<String, Object> map) {
            throw new UnsupportedOperationException();
        }
    }

}
