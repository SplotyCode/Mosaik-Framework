package io.github.splotycode.mosaik.util.reflection.classregister;

import io.github.splotycode.mosaik.util.reflection.GenericGuesser;
import lombok.Setter;

import java.util.Collection;

public class ListClassRegister<T> implements IListClassRegister<T> {

    @Setter private Collection<T> collection;
    private Class<T> clazz;

    public ListClassRegister() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    public ListClassRegister(Collection<T> collection) {
        this.collection = collection;
        clazz = (Class<T>) GenericGuesser.find(this, ListClassRegister.class, "T");
    }

    public ListClassRegister(Collection<T> collection, Class<T> clazz) {
        this.collection = collection;
        this.clazz = clazz;
    }

    @Override
    public Collection<T> getList() {
        return collection;
    }

    @Override
    public Class<T> getObjectClass() {
        return clazz;
    }

}
