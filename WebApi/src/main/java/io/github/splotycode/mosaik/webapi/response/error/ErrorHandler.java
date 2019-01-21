package io.github.splotycode.mosaik.webapi.response.error;

import io.github.splotycode.mosaik.util.reflection.classregister.ListClassRegister;
import io.github.splotycode.mosaik.webapi.response.Response;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ErrorHandler extends ListClassRegister<ErrorFactory> {

    private Set<ErrorFactory> errorFactories = new HashSet<>();

    public ErrorHandler() {
        setCollection(errorFactories);
    }

    public Response handleError(Throwable throwable) {
        ErrorFactory factory = errorFactories.stream().filter(errorFactory -> errorFactory.valid(throwable)).findFirst().orElse(new DefaultErrorFactory());
        return factory.generatePage(throwable);
    }

    public Response handleError(Throwable throwable, ErrorHandler fallback) {
        List<ErrorFactory> list = combind(fallback);
        ErrorFactory factory = list.stream().filter(errorFactory -> errorFactory.valid(throwable)).findFirst().orElse(new DefaultErrorFactory());
        return factory.generatePage(throwable);
    }

    public void installErrorFactory(ErrorFactory factory) {
        errorFactories.add(factory);
    }

    public void uninstallErrorFactory(ErrorFactory factory) {
        errorFactories.remove(factory);
    }

}
