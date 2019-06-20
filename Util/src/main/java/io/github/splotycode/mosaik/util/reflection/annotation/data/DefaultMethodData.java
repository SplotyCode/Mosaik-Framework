package io.github.splotycode.mosaik.util.reflection.annotation.data;

import io.github.splotycode.mosaik.util.Pair;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.ParameterResolver;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class DefaultMethodData extends AnnotationData implements IMethodData {

    @Getter @Setter
    protected Method method;

    protected List<Pair<ParameterResolver, Parameter>> parameters = new ArrayList<>();

    @Override
    public void registerParameter(Parameter parameter, ParameterResolver parameterResolver) {
        parameters.add(new Pair<>(parameterResolver, parameter));
    }

    @Override
    public Collection<Pair<ParameterResolver, Parameter>> getAllPrameters() {
        return parameters;
    }

}
