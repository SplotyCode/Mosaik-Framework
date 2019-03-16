package io.github.splotycode.mosaik.util.reflection.annotation;

import io.github.splotycode.mosaik.util.condition.ClassConditions;
import io.github.splotycode.mosaik.util.condition.Conditions;
import io.github.splotycode.mosaik.util.reflection.annotation.exception.*;
import io.github.splotycode.mosaik.util.reflection.annotation.method.AnnotationHandler;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

@Getter
public abstract class MultiAnnotationContext<M extends MultiAnnotationContext, D extends AnnotationData> implements AnnotationContext<M, D, Class> {

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

    public void feedObject(Object object) {
        this.clazz = object.getClass();
        try {
            global = globalDataClass().newInstance();
        } catch (Throwable ex) {
            throw new AnnotationDataExcpetion("failed to construct Global Class Data: " + globalDataClass().getName(), ex);
        }
        try {
            global.setElement(clazz);
            try {
                global.buildData(clazz.getAnnotations());
            } catch (Throwable ex) {
                throw new InvalidAnnotationDataExcpetion("Failed to build global data for " + clazz.getName(), ex);
            }
            for (AnnotationHandler<M, Annotation, D> handler : getAnnotationHandlers()) {
                if (clazz.isAnnotationPresent(handler.annotation())) {
                    Annotation annotation = clazz.getAnnotation(handler.annotation());
                    try {
                        handler.init(self(), annotation, global);
                    } catch (Throwable throwable) {
                        throw new AnnotationHandlerExcpetion("Error calling init on " + handler.getClass().getName(), throwable);
                    }
                }
            }
        } catch (Throwable throwable) {
            global.setLoadError(new AnnotationLoadError("Failed to load global"));
        }
        for (Method method : clazz.getMethods()) {
            if (!methodPredicate().test(method)) continue;
            D data;
            try {
                data = methodDataClass().newInstance();
            } catch (Throwable ex) {
                throw new InvalidAnnotationDataExcpetion("Failed to build data for " + method.getName(), ex);
            }
            sub.add(data);
            data.setElement(method);
            try {
                if (Modifier.isAbstract(method.getModifiers())) {
                    throw new IllegalStateException("Handler might not be abstract" + method.getDeclaringClass().getSimpleName() + "#" + method.getName());
                }
                try {
                    data.buildData(method.getAnnotations());
                } catch (Throwable ex) {
                    throw new InvalidAnnotationDataExcpetion("Failed to build data for " + method.getName(), ex);
                }

                for (AnnotationHandler<M, Annotation, D> handler : getAnnotationHandlers()) {
                    if (method.isAnnotationPresent(handler.annotation())) {
                        Annotation annotation = clazz.getAnnotation(handler.annotation());
                        try {
                            handler.init(self(), annotation, data);
                        } catch (Throwable throwable) {
                            throw new AnnotationHandlerExcpetion("Error calling init on " + handler.getClass().getName(), throwable);
                        }
                    }
                }
            } catch (Throwable throwable) {
                data.setLoadError(throwable);
            }
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

    protected abstract Class<? extends D> globalDataClass();
    protected abstract Predicate<? super AnnotatedElement> methodPredicate();
    protected abstract Class<? extends D> methodDataClass();

}
