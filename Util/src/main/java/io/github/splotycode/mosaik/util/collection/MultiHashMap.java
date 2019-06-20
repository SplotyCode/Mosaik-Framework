package io.github.splotycode.mosaik.util.collection;

import java.util.*;

public class MultiHashMap<K, V> extends HashMap<K, ArrayList<V>> {

    public MultiHashMap() {
    }

    public MultiHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public MultiHashMap(Map<? extends K, ? extends ArrayList<V>> m) {
        super(m);
    }

    public Iterator<V> allValues() {
        return new Iterator<V>() {
            Iterator<ArrayList<V>> values = values().iterator();
            Iterator<V> currentValues;
            @Override
            public boolean hasNext() {
                return values.hasNext() || (currentValues != null && currentValues.hasNext());
            }

            @Override
            public V next() {
                if (currentValues == null || !currentValues.hasNext()) {
                    ArrayList<V> list = values.next();
                    if (list == null) return null;
                    currentValues = list.iterator();
                }
                return currentValues.next();
            }
        };
    }

    public MultiHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public void addToList(K key, V value) {
        ArrayList<V> list = get(key);
        if (list == null) {
            list = new ArrayList<>();
            list.add(value);
            put(key, list);
        } else {
            list.add(value);
        }
    }

    @Override
    public Object clone() {
        return new MultiHashMap<>(this);
    }
}
