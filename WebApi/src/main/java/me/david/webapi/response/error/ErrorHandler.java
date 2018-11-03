package me.david.webapi.response.error;

import me.david.webapi.response.Response;

import java.util.HashSet;
import java.util.Set;

public class ErrorHandler {

    private Set<ErrorFactory> errorFactories = new HashSet<>();

    public Response handleError(Throwable throwable) {
        ErrorFactory factory = errorFactories.stream().filter(errorFactory -> errorFactory.valid(throwable)).findFirst().orElse(new DefaultErrorFactory());
        return factory.generatePage(throwable);
    }

    public void installErrorFactory(ErrorFactory factory) {
        errorFactories.add(factory);
    }

    public void uninstallErrorFactory(ErrorFactory factory) {
        errorFactories.remove(factory);
    }

}
