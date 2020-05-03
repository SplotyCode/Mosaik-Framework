package io.github.splotycode.mosaik.annotationbase.context.data;

import io.github.splotycode.mosaik.util.Pair;
import io.github.splotycode.mosaik.annotationbase.context.parameter.ParameterResolver;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class DefaultMethodData extends AnnotationData implements IMethodData {

    protected List<Pair<ParameterResolver, Parameter>> parameters = new ArrayList<>();

    @Override
    public void registerParameter(Parameter parameter, ParameterResolver parameterResolver) {
        parameters.add(new Pair<>(parameterResolver, parameter));
    }

    @Override
    public Collection<Pair<ParameterResolver, Parameter>> getAllParameters() {
        return parameters;
    }

}
