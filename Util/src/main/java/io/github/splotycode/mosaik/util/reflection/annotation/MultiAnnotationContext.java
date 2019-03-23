package io.github.splotycode.mosaik.util.reflection.annotation;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.condition.ClassConditions;
import io.github.splotycode.mosaik.util.condition.Conditions;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.util.reflection.annotation.data.IAnnotationData;
import io.github.splotycode.mosaik.util.reflection.annotation.data.IMethodData;
import io.github.splotycode.mosaik.util.reflection.annotation.exception.AnnotationLoadError;
import io.github.splotycode.mosaik.util.reflection.annotation.exception.DataUnavailableException;
import io.github.splotycode.mosaik.util.reflection.annotation.exception.InvalidAnnotationDataExcpetion;
import io.github.splotycode.mosaik.util.reflection.annotation.exception.ParameterInitExcepeion;
import io.github.splotycode.mosaik.util.reflection.annotation.method.AnnotationHandler;
import io.github.splotycode.mosaik.util.reflection.annotation.parameter.ParameterResolver;
import lombok.Getter;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

@Getter
@SuppressWarnings("WeakerAccess")
public abstract class MultiAnnotationContext<C extends MultiAnnotationContext, D extends IAnnotationData> extends AbstractAnnotationContext<C, D, Class> {

    //TODO more specific data keys with genetics
    public static DataKey<IAnnotationData> GLOBAL = new DataKey<>("base.global");
    public static DataKey<IAnnotationData> SUP = new DataKey<>("base.sup");

    protected Collection<D> sub = new ArrayList<>();

    @Override
    public void feed(Class clazz) {
        createObject(clazz);
        feedObject(object);
    }

    protected void constructGlobal() {
        data = construct(elementClass(), "Global");
    }

    protected void prepareGlobal() {
        data.setElement(clazz);
        data.setDisplayName("Global: " + clazz.getName());
        try {
            data.buildData(clazz.getAnnotations());
        } catch (Throwable ex) {
            throw new InvalidAnnotationDataExcpetion("Failed to build global data for " + clazz.getName(), ex);
        }
    }

    public void feedObject(Object object) {
        this.clazz = object.getClass();
        constructGlobal();
        try {
            prepareGlobal();
            initHandlers(data, clazz);
        } catch (Throwable throwable) {
            data.setLoadError(new AnnotationLoadError("Failed to load global", throwable));
        }

        for (Method method : clazz.getMethods()) {
            if (!methodPredicate().test(method)) continue;
            D data = construct(methodDataClass(), "Method");
            dataError(data, () -> {
                prepareMethodData(data, method);
                initHandlers(data, method);
                initParameters(data, method);
            });
        }
    }

    @Override
    protected Collection<ParameterResolver> getAllResolvers(D data) {
        List<ParameterResolver> allResolvers = new ArrayList<>(this.data.getCostomParameterResolvers());
        allResolvers.addAll(data.getCostomParameterResolvers());
        allResolvers.addAll(additionalParameterResolver());
        return allResolvers;
    }

    @Override
    protected Collection<ValueTransformer> getAllTransformers(D data) {
        List<ValueTransformer> allTransformers = new ArrayList<>(this.data.getCostomTransformers());
        allTransformers.addAll(data.getCostomTransformers());
        allTransformers.addAll(additionalTransformers());
        return allTransformers;
    }

    @Override
    public Object callmethod(D mData, DataFactory additionalInfo) {
        checkError(mData);

        additionalInfo.putData(GLOBAL, data);
        additionalInfo.putData(SUP, mData);

        return callMethod0(mData, additionalInfo);
    }

    @Override
    protected IMethodData toMethodData(D mData) {
        try {
            return  (IMethodData) mData;
        } catch (ClassCastException ex) {
            throw new ParameterInitExcepeion(mData.getClass().getName() + " needs to implement IMethodData");
        }
    }

    @Override
    protected void checkError(D currentData) {
        if (data.getLoadError() != null) {
            throw new DataUnavailableException("Count not use Handler Method: " + currentData.getDisplayName() + " because global fails loading on startup", data.getLoadError());
        }
        super.checkError(currentData);
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

    protected abstract Predicate<? super AnnotatedElement> methodPredicate();
    protected abstract Class<? extends D> methodDataClass();

}
