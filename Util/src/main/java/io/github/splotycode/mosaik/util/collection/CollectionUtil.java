package io.github.splotycode.mosaik.util.collection;

import io.github.splotycode.mosaik.util.Pair;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings({"unused", "WeakerAccess"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CollectionUtil {

    @SuppressWarnings("unchecked")
    public static <K, V, M extends Map<K, V>> M copyWithType(final M source) throws Exception {
        final M newMap = (M) source.getClass().newInstance();
        newMap.putAll(source);
        return newMap;
    }

    /**
     * @deprecated Use {@link HashMap#HashMap(Map)} instead
     */
    public static <K, V> Map<K, V> copy(final Map<K, V> source) {
        return new HashMap<>(source);
    }

    public static int getOptimalMapSize(int size, float loadFactor) {
        float ft = ((float)size / loadFactor) + 1.0F;
        return (ft < (float)(1 << 30)) ? (int)ft : (1 << 30);
    }

    public static int getOptimalMapSize(int size) {
        return getOptimalMapSize(size, 0.75f);
    }

    public static <K, V> Map<K, V> newHashMap(List<K> keys, List<V> values) {
        if (keys.size() != values.size()) {
            throw new IllegalArgumentException("Lists need to have the same lengths");
        }

        Map<K, V> map = new HashMap<>(getOptimalMapSize(keys.size()));
        for (int i = 0; i < keys.size(); ++i) {
            map.put(keys.get(i), values.get(i));
        }
        return map;
    }

    @SafeVarargs
    public static <K, V> Map<K, V> newHashMap(Pair<K, ? extends V>... entries) {
        Map<K, V> map = new HashMap<>(getOptimalMapSize(entries.length));
        for (Pair<K, ? extends V> entry : entries) {
            map.put(entry.getOne(), entry.getTwo());
        }
        return map;
    }

    @SafeVarargs
    public static <T> LinkedList<T> newLinkedList(T... elements) {
        final LinkedList<T> list = new LinkedList<>();
        Collections.addAll(list, elements);
        return list;
    }

    public static <T> LinkedList<T> newLinkedList(Iterable<? extends T> elements) {
        return copy(new LinkedList<>(), elements);
    }

    @SafeVarargs
    public static <T> ArrayList<T> newArrayList(T... elements) {
        ArrayList<T> list = new ArrayList<>(elements.length);
        Collections.addAll(list, elements);
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> newArrayList(Iterable<? extends T> elements) {
        if (elements instanceof Collection) {
            Collection<? extends T> collection = (Collection<? extends T>) elements;
            return new ArrayList<>(collection);
        }
        return copy(new ArrayList<>(), elements);
    }

    private static <T, C extends Collection<T>> C copy(C collection, Iterable<? extends T> elements) {
        for (T element : elements) {
            collection.add(element);
        }
        return collection;
    }

    @SuppressWarnings("unchecked")
    public static <T> HashSet<T> newHashSet(Iterable<? extends T> elements) {
        if (elements instanceof Collection) {
            Collection<? extends T> collection = (Collection<? extends T>)elements;
            return new HashSet<>(collection);
        }
        return newHashSet(elements.iterator());
    }

    @SafeVarargs
    public static <T> HashSet<T> newHashSet(T... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }

    public static <T> HashSet<T> newHashSet(Iterator<? extends T> iterator) {
        HashSet<T> set = new HashSet<>();
        while (iterator.hasNext()) set.add(iterator.next());
        return set;
    }

    /**
     * @deprecated {@link CollectionUtil#addIfNotNull(Collection, Object)}
     */
    @Deprecated
    public static <T> void addIfNotNull(T element, Collection<T> result) {
        if (element != null) {
            result.add(element);
        }
    }

    public static <T> void addIfNotNull(Collection<T> result, T element) {
        if (element != null) {
            result.add(element);
        }
    }

    public static <T, V> List<V> map2List(T[] array, Function<T, V> mapper) {
        return map2List(Arrays.asList(array), mapper);
    }

    public static <T, V> List<V> mapNotNull(Collection<? extends T> collection, Function<T, V> mapping) {
        if (collection.isEmpty()) {
            return Collections.emptyList();
        }

        List<V> result = new ArrayList<>(collection.size());
        for (T t : collection) {
            final V o = mapping.apply(t);
            if (o != null) {
                result.add(o);
            }
        }
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    public static <T, V> List<V> map2List(Collection<? extends T> collection, Function<T, V> mapper) {
        if (collection.isEmpty()) return Collections.emptyList();
        List<V> list = new ArrayList<>(collection.size());
        for (final T t : collection) {
            list.add(mapper.apply(t));
        }
        return list;
    }

    public static <K, V> List<Pair<K, V>> map2List(Map<K, V> map) {
        if (map.isEmpty()) return Collections.emptyList();
        final List<Pair<K, V>> result = new ArrayList<>(map.size());
        for (Map.Entry<K, V> entry : map.entrySet()) {
            result.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    public static <T, V> Set<V> map2Set(T[] collection, Function<T, V> mapper) {
        return map2Set(Arrays.asList(collection), mapper);
    }

    public static <T, V> Set<V> map2Set(Collection<? extends T> collection, Function<T, V> mapper) {
        if (collection.isEmpty()) return Collections.emptySet();
        Set <V> set = new HashSet<>(collection.size());
        for (final T t : collection) {
            set.add(mapper.apply(t));
        }
        return set;
    }

    public static <T> T[] toArray(List<T> collection, T[] array) {
        final int length = array.length;
        if (length < 12 && array.length >= collection.size()) {
            for (int i = 0; i < collection.size(); i++) {
                array[i] = collection.get(i);
            }
            return array;
        }
        return collection.toArray(array);
    }


    public static <T> T[] toArray(Collection<T> c, T[] sample) {
        final int size = c.size();
        if (size == sample.length && size < 12) {
            int i = 0;
            for (T t : c) {
                sample[i++] = t;
            }
            return sample;
        }

        return c.toArray(sample);
    }

    public static <T, L extends List<T>> T getLastItem(L list, T def) {
        return isEmpty(list) ? def : list.get(list.size() - 1);
    }

    public static <T, L extends List<T>> T getLastItem(L list) {
        return getLastItem(list, null);
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T, V extends T> V find(Iterable<V> iterable, Predicate<T> condition) {
        return find(iterable.iterator(), condition);
    }

    public static <T, V extends T> V find(Iterator<V> iterator, Predicate<T> condition) {
        while (iterator.hasNext()) {
            V value = iterator.next();
            if (condition.test(value)) return value;
        }
        return null;
    }

    public static <T> int indexOf(List<T> list, Predicate<? super T> condition) {
        for (int i = 0, listSize = list.size(); i < listSize; i++) {
            T t = list.get(i);
            if (condition.test(t)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @deprecated please use {@link CollectionUtil#mergeMaps(Map, Map)}
     */
    @Deprecated
    @SuppressWarnings("all")
    public static <K, V> Map<K, V> combind(Map<K, V> map1, Map<K, V> map2) {
        Map<K, V> map3 = new HashMap<>(getOptimalMapSize(map1.size() + map2.size()));
        map3.putAll(map1);
        map3.putAll(map2);
        return map3;
    }

    public static <E> List<E> mergeCollections(Collection<E> one, Collection<E> two) {
        if (one.isEmpty() && two.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList<E> result = new ArrayList<>(one.size() + two.size());
        result.addAll(one);
        result.addAll(two);
        return result;
    }

    @SafeVarargs
    public static <E> List<E> mergeCollections(Collection<E>... lists) {
        int size = 0;
        for (Collection<E> es : lists) {
            size += es.size();
        }

        if (size == 0) {
            return Collections.emptyList();
        }

        ArrayList<E> result = new ArrayList<>(size);
        for (Collection<E> list : lists) {
            result.addAll(list);
        }
        return result;
    }

    public static <K, V> Map<K, V> mergeMaps(Map<K, V> one, Map<K, V> two) {
        if (one.isEmpty() && two.isEmpty()) {
            return Collections.emptyMap();
        }
        HashMap<K, V> result = new HashMap<>(getOptimalMapSize(one.size() + two.size()));
        result.putAll(one);
        result.putAll(two);
        return result;
    }

    @SafeVarargs
    public static <K, V> Map<K, V> mergeMaps(Map<K, V>... maps) {
        int size = 0;
        for (Map<K, V> map : maps) {
            size += map.size();
        }

        if (size == 0) {
            return Collections.emptyMap();
        }

        HashMap<K, V> result = new HashMap<>(getOptimalMapSize(size));
        for (Map<K, V> map : maps) {
            result.putAll(map);
        }
        return result;
    }
    
}
