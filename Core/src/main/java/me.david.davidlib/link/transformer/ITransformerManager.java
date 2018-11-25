package me.david.davidlib.link.transformer;

import me.david.davidlib.utils.reflection.ClassRegister;

public interface ITransformerManager extends ClassRegister<ValueTransformer> {

    <T> T transform(Object input, Class<T> result);

}
