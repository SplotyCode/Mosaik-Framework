package io.github.splotycode.mosaik.util.reflection.annotation.parameter;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.annotation.AnnotationContext;
import io.github.splotycode.mosaik.util.reflection.annotation.exception.ParameterResolveException;

import java.lang.reflect.Parameter;

public interface ParameterResolver<R, C extends AnnotationContext> {

    boolean transformable(C context, Parameter parameter);

    R transform(C context, Parameter parameter, DataFactory data) throws ParameterResolveException;

}
