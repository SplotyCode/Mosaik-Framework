package io.github.splotycode.mosaik.util.reflection.annotation;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.reflection.annotation.data.IMethodData;
import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public abstract class SingleAnnotationContext<C extends SingleAnnotationContext, D extends IMethodData> extends AbstractAnnotationContext<C, D, Method> {

    @Override
    public void feed(Method method) {
        /* Create Object */
        clazz = method.getDeclaringClass();
        createObject(clazz);

        data = construct(elementClass(), "Method");
        dataError(data, () -> {
            prepareMethodData(data, method);
            initHandlers(data, method);
            initParameters(data, method);
        });
    }

    @Override
    protected IMethodData toMethodData(D data) {
        return data;
    }

    @Override
    public Object callmethod(D data, DataFactory additionalInfo) {
        checkError(data);
        return callMethod0(data, additionalInfo);
    }
}
