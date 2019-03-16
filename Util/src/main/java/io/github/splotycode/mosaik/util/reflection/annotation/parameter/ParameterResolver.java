package io.github.splotycode.mosaik.util.reflection.annotation.parameter;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.annotation.exception.ParameterResolveException;

import java.lang.reflect.Parameter;

public interface ParameterResolver<R> {

    boolean transformable(Parameter parameter);

    R transform(Parameter parameter, DataFactory data) throws ParameterResolveException;

}
