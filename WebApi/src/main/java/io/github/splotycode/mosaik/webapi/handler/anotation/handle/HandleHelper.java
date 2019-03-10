package io.github.splotycode.mosaik.webapi.handler.anotation.handle;

import io.github.splotycode.mosaik.valuetransformer.TransformException;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;
import io.github.splotycode.mosaik.valuetransformer.ValueTransformer;
import io.github.splotycode.mosaik.webapi.handler.anotation.parameter.ParameterResolveException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Parameter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HandleHelper {

    public static Object transformParameter(Parameter parameter, String input) {
        if (parameter.isAnnotationPresent(UseTransformer.class)) {
            try {
                ValueTransformer transformer = parameter.getAnnotation(UseTransformer.class).value().newInstance();
                if (transformer.valid(input, parameter.getType())) {
                    return transformer.transform(input);
                }
            } catch (IllegalAccessException | InstantiationException ex) {
                throw new ParameterResolveException("Failed to create instance", ex);
            } catch (TransformException ex) {
                throw new ParameterResolveException("Failed to Transform Value", ex);
            }
        }
        try {
            return TransformerManager.getInstance().transform(input, parameter.getType());
        } catch (TransformException ex) {
            throw new ParameterResolveException("Failed to Transform Value", ex);
        }
    }

}
