package io.github.splotycode.mosaik.annotationbase.context.data;

import io.github.splotycode.mosaik.util.Pair;
import io.github.splotycode.mosaik.annotationbase.context.parameter.ParameterResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;

public interface IMethodData extends IAnnotationData {

    void registerParameter(Parameter parameter, ParameterResolver parameterResolver);

    Collection<Pair<ParameterResolver, Parameter>> getAllParameters();

    default Method getMethod() {
        return (Method) getElement();
    }

}
