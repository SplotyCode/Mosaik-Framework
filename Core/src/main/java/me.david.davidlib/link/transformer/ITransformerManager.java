package me.david.davidlib.link.transformer;

import me.david.davidlib.utils.reflection.classregister.IListClassRegister;

public interface ITransformerManager extends IListClassRegister<ValueTransformer> {

    <T> T transform(Object input, Class<T> result);

}
