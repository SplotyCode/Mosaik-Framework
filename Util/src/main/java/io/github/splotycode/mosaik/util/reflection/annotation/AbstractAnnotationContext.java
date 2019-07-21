package io.github.splotycode.mosaik.util.reflection.annotation;

import io.github.splotycode.mosaik.util.Pair;
import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.annotation.data.IAnnotationData;
import io.github.splotycode.mosaik.util.reflection.annotation.data.IMethodData;
import io.github.splotycode.mosaik.util.reflection.annotation.exception.*;
import io.github.splotycode.mosaik.util.reflection.annotation.method.AnnotationHandler;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.ParameterResolver;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.UseResolver;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.UseTransformer;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
public abstract class AbstractAnnotationContext<C extends AnnotationContext, D extends IAnnotationData, T extends AnnotatedElement> implements AnnotationContext<C, D, T> {

    protected Class clazz;
    protected Object object;
    protected D data;

    @Override
    public Object parameterValue(D data, Parameter parameter, String input) {
        if (parameter.isAnnotationPresent(UseTransformer.class)) {
            try {
                ValueTransformer transformer = parameter.getAnnotation(UseTransformer.class).value().newInstance();
                if (transformer.valid(input, parameter.getType())) {
                    return transformer.transform(input);
                }
            } catch (IllegalAccessException | InstantiationException ex) {
                throw new ParameterResolveException("Failed to create instance", ex);
            } catch (Throwable throwable) {
                throw new ParameterResolveException("Failed to Transform Value", throwable);
            }
        }
        try {
            return rawTransform(input, parameter.getType(), getAllTransformers(data));
        } catch (Throwable throwable) {
            throw new ParameterResolveException("Failed to transform Parameter " + parameter.getName(), throwable);
        }
    }

    protected Collection<ParameterResolver> getAllResolvers(D data) {
        List<ParameterResolver> allResolvers = new ArrayList<>(data.getCostomParameterResolvers());
        allResolvers.addAll(additionalParameterResolver());
        return allResolvers;
    }

    protected Collection<ValueTransformer> additionalTransformers() {
        return Collections.emptyList();
    }

    protected Collection<ValueTransformer> getAllTransformers(D data) {
        List<ValueTransformer> allResolvers = new ArrayList<>(data.getCostomTransformers());
        allResolvers.addAll(additionalTransformers());
        return allResolvers;
    }

    protected abstract Collection<ParameterResolver> additionalParameterResolver();

    protected void callAnnotationHandler(boolean pre, D data, Method method) {
        getAnnotationHandlers().forEach(handler -> {
            try {
                handler.doCall(method, pre ? AnnotationHandler.CallType.PRE : AnnotationHandler.CallType.POST, self(), data);
            } catch (Throwable e) {
                throw new AnnotationHandlerException("Error in " + handler.getClass().getName() + (pre ? "#preCall" : "#postCall"), e);
            }
        });
    }

    protected void initParameters(D data, Method method) {
        IMethodData methodData;
        try {
            methodData = (IMethodData) data;
        } catch (ClassCastException ex) {
            throw new ParameterInitExcepeion(data.getClass().getName() + " needs to implement IMethodData");
        }
        for (Parameter parameter : method.getParameters()) {
            try {
                if (parameter.isAnnotationPresent(UseResolver.class)) {
                    Class<? extends ParameterResolver> resolverClazz = parameter.getAnnotation(UseResolver.class).value();
                    ParameterResolver resolver;
                    try {
                        resolver = resolverClazz.newInstance();
                    } catch (ReflectiveOperationException ex) {
                        throw new ParameterInitExcepeion("Failed to construct " + resolverClazz.getName(), ex);
                    }
                    methodData.registerParameter(parameter, resolver);
                    continue;
                }

                boolean found = false;
                for (ParameterResolver parameterResolver : getAllResolvers(data)) {
                    if (parameterResolver.transformable(self(), parameter)) {
                        methodData.registerParameter(parameter, parameterResolver);
                        found = true;
                        break;
                    }
                }
                if (!found) throw new ParameterInitExcepeion("Could not find transformer");
            } catch (Throwable throwable) {
                throw new ParameterInitExcepeion("Failed to initialize " + parameter.getName() + " of " + data.getDisplayName(), throwable);
            }
        }
    }

    protected <L> L construct(Class<L> clazz, String name) {
        try {
            return clazz.newInstance();
        } catch (Throwable ex) {
            throw new AnnotationDataException("failed to construct " + name + " Data: " + clazz.getName(), ex);
        }
    }

    protected void createObject(Class clazz) {
        try {
            object = clazz.newInstance();
        } catch (ReflectiveOperationException ex) {
            throw new AnnotationFeedException("Failed to create " + clazz.getName(), ex);
        }
    }

    protected void dataError(D data, Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            data.setLoadError(throwable);
        }
    }

    protected void prepareMethodData(D data, Method method) {
        if (Modifier.isAbstract(method.getModifiers())) {
            throw new IllegalStateException("Handler might not be abstract" + method.getDeclaringClass().getSimpleName() + "#" + method.getName());
        }
        data.setElement(method);
        data.setDisplayName(method.getDeclaringClass().getName() + "#" + method.getName());
        try {
            data.buildData(method.getAnnotations());
        } catch (Throwable ex) {
            throw new InvalidAnnotationDataException("Failed to build data for " + data.getDisplayName(), ex);
        }
    }

    protected void initHandlers(D data, AnnotatedElement object) {
        for (AnnotationHandler<C, ? extends Annotation, D> handler : getAnnotationHandlers()) {
            try {
                handler.doCall(object, AnnotationHandler.CallType.INIT, self(), data);
            } catch (Throwable throwable) {
                throw new AnnotationHandlerException("Error calling init on " + handler.getClass().getName(), throwable);
            }
        }
    }

    protected void checkError(D data) {
        if (data.getLoadError() != null) {
            throw new DataUnavailableException("Count not use Handler Method: " + data.getDisplayName() + " because it fails loading on startup", data.getLoadError());
        }
    }

    protected abstract IMethodData toMethodData(D data);

    protected Object callMethod0(D rawData, DataFactory additionalInfo) {
        IMethodData mData = toMethodData(rawData);

        Object[] parameters = new Object[mData.getAllParameters().size()];
        try {
            int i = 0;
            for (Pair<ParameterResolver, Parameter> pair : mData.getAllParameters()) {
                try {
                    parameters[i] = pair.getOne().transform(self(), pair.getTwo(), additionalInfo);
                } catch (ParameterResolveException ex) {
                    throw new ParameterTransformException("Failed to transform parameter", ex);
                }
                i++;
            }
        } catch (Throwable ex) {
            throw new MethodPrepareExcepetion("Could not prepare parameters for Method: " + mData.getMethod().getName(), ex);
        }
        Method method = mData.getMethod();
        method.setAccessible(true);

        callAnnotationHandler(true, (D) mData, method);

        try {
            return method.invoke(object, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MethodCallExcepetion("Failed to call " + mData.getDisplayName(), e);
        } finally {
            callAnnotationHandler(false, (D) mData, method);
        }
    }

    @Override
    public C self() {
        return (C) this;
    }

    @Override
    public D data() {
        return data;
    }
}
