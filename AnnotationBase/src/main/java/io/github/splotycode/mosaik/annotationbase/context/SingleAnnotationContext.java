package io.github.splotycode.mosaik.annotationbase.context;

import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.annotationbase.context.data.IMethodData;
import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public abstract class SingleAnnotationContext<C extends SingleAnnotationContext, D extends IMethodData> extends AbstractAnnotationContext<C, D, Method> {

    public void feed(Method method, Object object) {
        this.object = object;
        clazz = method.getDeclaringClass();
        data = construct(elementClass(), "Method");
        dataError(data, () -> {
            prepareMethodData(data, method);
            initHandlers(data, method);
            initParameters(data, method);
        });
    }

    @Override
    public void feed(Method method) {
        createObject(clazz);
        feed(method, object);
    }

    @Override
    protected IMethodData toMethodData(D data) {
        return data;
    }

    public Object callmethod(DataFactory additionalInfo) {
        return callmethod(null, additionalInfo);
    }

    @Override
    public Object callmethod(D data, DataFactory additionalInfo) {
        if (data == null) {
            data = this.data;
        }
        checkError(data);
        return callMethod0(data, additionalInfo);
    }
}
