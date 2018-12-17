package me.david.davidlib.utils.reflection.classregister;

import lombok.Setter;
import me.david.davidlib.utils.reflection.ReflectionUtil;

import java.util.ArrayList;
import java.util.Collection;

public class ListClassRegister<T> implements IListClassRegister<T> {

    @Setter private Collection<T> collection;
    private Class<T> clazz;

    public ListClassRegister() {
        this(null);
    }

    public ListClassRegister(Collection<T> collection) {
        this.collection = collection;
        clazz = (Class<T>) ReflectionUtil.getGenerretics(getClass())[0];
    }

    @Override
    public Collection<T> getList() {
        return collection;
    }

    public ArrayList<T> combind(IListClassRegister<T> register) {
        ArrayList<T> list = new ArrayList<>(register.getList());
        list.addAll(collection);
        return list;
    }

    @Override
    public Class<T> getObjectClass() {
        return clazz;
    }

}
