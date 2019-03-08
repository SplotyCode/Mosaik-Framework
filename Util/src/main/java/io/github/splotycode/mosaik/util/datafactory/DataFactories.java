package io.github.splotycode.mosaik.util.datafactory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataFactories {

    public static final EmptyDataFactory EMPTY_DATA_FACTORY = new EmptyDataFactory();

    public static SingletonDataFactory singletonDataFactory(String key, Object value) {
        return new SingletonDataFactory(key, value);
    }

    private static class EmptyDataFactory extends DataFactory {

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
            return null;
        }

        @Override
        public <T> void putData(String name, DataKey<T> key, T obj) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, Object> getRawMap() {
            return Collections.emptyMap();
        }

        @Override
        public void setMap(Map<String, Object> map) {
            throw new UnsupportedOperationException();
        }
    }

    @AllArgsConstructor
    private static class SingletonDataFactory extends DataFactory {

        private String key;
        private Object object;

        @Override
        public int getDataSize() {
            return 1;
        }

        @Override
        public <T> T getDataDefault(String name, DataKey<T> key, T def) {
            if (name.equals(this.key)) {
                return (T) object;
            }
            return null;
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
            return Collections.singletonMap(key, object);
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
