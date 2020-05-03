package io.github.splotycode.mosaik.annotationbase.context;

import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.condition.ClassConditions;
import io.github.splotycode.mosaik.util.condition.Conditions;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.annotationbase.context.data.IAnnotationData;
import io.github.splotycode.mosaik.annotationbase.context.data.IMethodData;
import io.github.splotycode.mosaik.annotationbase.context.exception.AnnotationLoadError;
import io.github.splotycode.mosaik.annotationbase.context.exception.DataUnavailableException;
import io.github.splotycode.mosaik.annotationbase.context.exception.InvalidAnnotationDataException;
import io.github.splotycode.mosaik.annotationbase.context.exception.ParameterInitExcepeion;
import io.github.splotycode.mosaik.annotationbase.context.method.AnnotationHandler;
import io.github.splotycode.mosaik.annotationbase.context.parameter.ParameterResolver;
import lombok.Getter;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Getter
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class MultiAnnotationContext<C extends MultiAnnotationContext, D extends IAnnotationData> extends AbstractAnnotationContext<C, D, Class> {

    public static DataKey<IAnnotationData> GLOBAL = new DataKey<>("base.global");
    public static DataKey<IAnnotationData> SUP = new DataKey<>("base.sup");

    @SuppressWarnings("unchecked")
    public DataKey<D> globalKey() {
        return (DataKey<D>) GLOBAL;
    }

    @SuppressWarnings("unchecked")
    public DataKey<D> supKey() {
        return (DataKey<D>) SUP;
    }

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
            throw new InvalidAnnotationDataException("Failed to build global data for " + clazz.getName(), ex);
        }
    }

    public void feedObject(Object object) {
        this.object = object;
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
            sub.add(data);
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
        return buildPredicate(needClass, handlers.stream(), subHandlers.stream());
    }

    public static Predicate<Class> buildPredicate(boolean needClass, AnnotationHandler[] handlers, AnnotationHandler[] subHandlers) {
        return buildPredicate(needClass, Arrays.stream(handlers), Arrays.stream(subHandlers));
    }

    public static Predicate<Class> buildPredicate(boolean needClass, Collection<AnnotationHandler> handlers) {
        return buildPredicate(needClass, handlers.stream(), handlers.stream());
    }

    public static Predicate<Class> buildPredicate(boolean needClass, AnnotationHandler[] handlers) {
        return buildPredicate(needClass, Arrays.stream(handlers), Arrays.stream(handlers));
    }

    @SuppressWarnings("unchecked")
    public static Predicate<Class> buildPredicate(boolean needClass, Stream<AnnotationHandler> handlers, Stream<AnnotationHandler> subHandlers) {
        Predicate<Class> classPredicate = item -> handlers.anyMatch(
                handler -> item.isAnnotationPresent(handler.annotation())
        );
        Predicate<Class> methodPredicate = ClassConditions.anyMethod(method -> subHandlers.anyMatch(
                handler -> method.isAnnotationPresent(handler.annotation())
        ));
        if (needClass) {
            return Conditions.and(classPredicate, methodPredicate);
        }
        return Conditions.or(classPredicate, methodPredicate);
    }

    protected abstract Predicate<? super AnnotatedElement> methodPredicate();
    protected abstract Class<? extends D> methodDataClass();

}
