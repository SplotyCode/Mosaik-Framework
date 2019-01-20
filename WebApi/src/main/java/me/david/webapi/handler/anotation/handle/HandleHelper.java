package me.david.webapi.handler.anotation.handle;

import me.david.splotycode.valuetransformer.TransformException;
import me.david.splotycode.valuetransformer.TransformerManager;
import me.david.splotycode.valuetransformer.ValueTransformer;
import me.david.webapi.handler.anotation.parameter.ParameterResolveException;

import java.lang.reflect.Parameter;

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
