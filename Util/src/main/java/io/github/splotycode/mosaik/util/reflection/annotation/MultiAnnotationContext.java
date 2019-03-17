package io.github.splotycode.mosaik.util.reflection.annotation;

import io.github.splotycode.mosaik.util.Pair;
import io.github.splotycode.mosaik.util.condition.ClassConditions;
import io.github.splotycode.mosaik.util.condition.Conditions;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.util.reflection.annotation.data.AnnotationData;
import io.github.splotycode.mosaik.util.reflection.annotation.data.IMethodData;
import io.github.splotycode.mosaik.util.reflection.annotation.exception.*;
import io.github.splotycode.mosaik.util.reflection.annotation.method.AnnotationHandler;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.ParameterResolver;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.UseResolver;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

@Getter
@SuppressWarnings("WeakerAccess")
public abstract class MultiAnnotationContext<M extends MultiAnnotationContext, D extends AnnotationData> implements AnnotationContext<M, D, Class> {

    public static DataKey<AnnotationData> GLOBAL = new DataKey<>("base.global");
    public static DataKey<AnnotationData> SUP = new DataKey<>("base.sup");

    protected Class clazz;
    protected Object object;
    protected D global;
    protected Collection<D> sub = new ArrayList<>();

    @Override
    public void feed(Class clazz) {
         try {
            object = clazz.newInstance();
        } catch (ReflectiveOperationException ex) {
            throw new AnnotationFeedExcpetion("Failed to create " + clazz.getName(), ex);
        }
        feedObject(object);
    }

    protected void constructGlobal() {
        try {
            global = globalDataClass().newInstance();
        } catch (Throwable ex) {
            throw new AnnotationDataExcpetion("failed to construct Global Class Data: " + globalDataClass().getName(), ex);
        }
    }

    protected void prePairGlobal() {
        global.setElement(clazz);
        global.setDisplayName("Global: " + clazz.getName());
        try {
            global.buildData(clazz.getAnnotations());
        } catch (Throwable ex) {
            throw new InvalidAnnotationDataExcpetion("Failed to build global data for " + clazz.getName(), ex);
        }
    }

    protected void initHandlers(D data, AnnotatedElement object) {
        for (AnnotationHandler<M, Annotation, D> handler : getAnnotationHandlers()) {
            if (object.isAnnotationPresent(handler.annotation())) {
                Annotation annotation = object.getAnnotation(handler.annotation());
                try {
                    handler.init(self(), annotation, data);
                } catch (Throwable throwable) {
                    throw new AnnotationHandlerExcpetion("Error calling init on " + handler.getClass().getName(), throwable);
                }
            }
        }
    }

    public void feedObject(Object object) {
        this.clazz = object.getClass();
        constructGlobal();
        try {
            prePairGlobal();
            initHandlers(global, clazz);
        } catch (Throwable throwable) {
            global.setLoadError(new AnnotationLoadError("Failed to load global", throwable));
        }
        for (Method method : clazz.getMethods()) {
            if (!methodPredicate().test(method)) continue;
            D data = constructData(method);
            try {
                prepairData(data, method);
                initHandlers(data, method);
                initParameters(data, method);
            } catch (Throwable throwable) {
                data.setLoadError(throwable);
            }
        }
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
                    if (parameterResolver.transformable(parameter)) {
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

    private Collection<ParameterResolver> getAllResolvers(D data) {
        List<ParameterResolver> allResolvers = new ArrayList<>(data.getCostomParameterResolvers());
        allResolvers.addAll(global.getCostomParameterResolvers());
        allResolvers.addAll(additionalParameterResolver());
        return allResolvers;
    }

    protected void prepairData(D data, Method method) {
        if (Modifier.isAbstract(method.getModifiers())) {
            throw new IllegalStateException("Handler might not be abstract" + method.getDeclaringClass().getSimpleName() + "#" + method.getName());
        }
        data.setDisplayName(method.getDeclaringClass().getName() + "#" + method.getName());
        try {
            data.buildData(method.getAnnotations());
        } catch (Throwable ex) {
            throw new InvalidAnnotationDataExcpetion("Failed to build data for " + method.getName(), ex);
        }
    }

    protected D constructData(Method method) {
        try {
            D data = methodDataClass().newInstance();
            sub.add(data);
            data.setElement(method);
            return data;
        } catch (Throwable ex) {
            throw new InvalidAnnotationDataExcpetion("Failed to build data for " + method.getName(), ex);
        }
    }

    @Override
    public Object callmethod(D data, DataFactory additionalInfo) {
        if (data.getLoadError() != null) {
            throw new DataUnavailableException("Count not use Handler Method: " + data.getDisplayName() + " because it fails loading on startup", data.getLoadError());
        }
        IMethodData methodData;
        try {
            methodData = (IMethodData) data;
        } catch (ClassCastException ex) {
            throw new ParameterInitExcepeion(data.getClass().getName() + " needs to implement IMethodData");
        }
        Object[] parameters = new Object[methodData.getAllPrameters().size()];
        try {
            int i = 0;
            for (Pair<ParameterResolver, Parameter> pair : methodData.getAllPrameters()) {
                try {
                    additionalInfo.putData(GLOBAL, global);
                    additionalInfo.putData(SUP, data);
                    parameters[i] = pair.getOne().transform(pair.getTwo(), additionalInfo);
                } catch (ParameterResolveException ex) {
                    throw new ParameterTransformExcpetion("Failed to transform parameter", ex);
                }
                i++;
            }
        } catch (Throwable ex) {
            throw new MethodPrepareExcepetion("Could not prepare parameters for Method: " + methodData.getMethod().getName(), ex);
        }
        Method method = methodData.getMethod();
        method.setAccessible(true);
        try {
            return method.invoke(object, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MethodCallExcepetion("Failed to call " + data.getDisplayName(), e);
        }
    }

    public static Predicate<Class> buildPredicate(boolean needClass, Collection<AnnotationHandler> handlers, Collection<AnnotationHandler> subHandlers) {
        Predicate<Class> classPredicate = item -> {
            return handlers.stream().anyMatch(
                    handler -> item.isAnnotationPresent(handler.annotation())
            );
        };
        Predicate<Class> methodPredicate = ClassConditions.anyMethod(method -> {
            return subHandlers.stream().anyMatch(
                    handler -> method.isAnnotationPresent(handler.annotation())
            );
        });
        if (needClass) {
            return Conditions.and(classPredicate, methodPredicate);
        }
        return Conditions.or(classPredicate, methodPredicate);
    }

    @Override
    public AnnotationData globalData() {
        return global;
    }

    protected abstract Collection<ParameterResolver> additionalParameterResolver();
    protected abstract Class<? extends D> globalDataClass();
    protected abstract Predicate<? super AnnotatedElement> methodPredicate();
    protected abstract Class<? extends D> methodDataClass();

}
