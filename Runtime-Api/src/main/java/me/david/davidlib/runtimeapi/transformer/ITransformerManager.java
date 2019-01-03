package me.david.davidlib.runtimeapi.transformer;

import me.david.davidlib.util.reflection.classregister.IListClassRegister;

public interface ITransformerManager extends IListClassRegister<ValueTransformer> {

    <T> T transform(Object input, Class<T> result);

}
