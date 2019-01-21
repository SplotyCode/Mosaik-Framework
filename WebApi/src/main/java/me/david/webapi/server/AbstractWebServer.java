package me.david.webapi.server;

import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.util.init.InitialisedOnce;
import me.david.davidlib.util.reflection.classregister.IListClassRegister;
import me.david.davidlib.util.reflection.classregister.ListClassRegister;
import me.david.webapi.WebApplicationType;
import me.david.webapi.handler.HandlerFinder;
import me.david.webapi.handler.HttpHandler;
import me.david.webapi.handler.StaticHandlerFinder;
import me.david.webapi.handler.anotation.AnnotationHandlerFinder;
import me.david.webapi.handler.anotation.parameter.ParameterResolver;
import me.david.webapi.request.Request;
import me.david.webapi.request.body.RequestContentHandler;
import me.david.webapi.response.Response;
import me.david.webapi.response.error.ErrorFactory;
import me.david.webapi.response.error.ErrorHandler;
import me.david.webapi.session.SessionSystem;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractWebServer extends InitialisedOnce implements WebServer {

    @Getter protected InetSocketAddress address;

    @Getter protected int requests = 0;
    @Getter protected long totalTime = 0;

    @Getter protected WebApplicationType application;
    @Setter @Getter protected ErrorHandler errorHandler = new ErrorHandler();

    public AbstractWebServer(WebApplicationType application) {
        this.application = application;
    }

    @Getter private List<HttpHandler> allHandlers = new ArrayList<>();
    @Getter private StaticHandlerFinder staticHandlerFinder;

    private List<ParameterResolver> parameterResolvers = new ArrayList<>();
    @Getter private ListClassRegister<ParameterResolver> parameterResolverRegister = new ListClassRegister<>(parameterResolvers);

    private List<RequestContentHandler> contentHandlers = new ArrayList<>();
    @Getter private ListClassRegister<RequestContentHandler> contentHandlerRegister = new ListClassRegister<>(contentHandlers);

    private ListClassRegister<SessionSystem> sessionSystems = new ListClassRegister<>(new ArrayList<>());

    @Override
    public IListClassRegister<SessionSystem> getSessionLoader() {
        return sessionSystems;
    }

    @Override
    public Collection<SessionSystem> getSessionSystems() {
        return sessionSystems.getList();
    }

    public List<RequestContentHandler> getContentHandlers() {
        return contentHandlerRegister.combind(application.getContentHandlerRegister());
    }

    public List<ParameterResolver> getParameterResolvers() {
        return parameterResolverRegister.combind(application.getParameterResolveRegister());
    }

    public List<ParameterResolver> getLocalParameterResolvers() {
        return parameterResolvers;
    }

    @Override
    protected void init() {
        staticHandlerFinder = new StaticHandlerFinder();
        addFinder(staticHandlerFinder);
        addFinder(new AnnotationHandlerFinder(this));
    }

    public void addFinder(HandlerFinder finder) {
        if (initialised) throw new IllegalStateException("Can not add finder if WebServer is already initialised");
        allHandlers.addAll(finder.search());
    }

    @Override
    public void installErrorFactory(ErrorFactory factory) {
        errorHandler.installErrorFactory(factory);
    }

    @Override
    public void uninstallErrorFactory(ErrorFactory factory) {
        errorHandler.uninstallErrorFactory(factory);
    }

    public Response handleRequest(Request request) {
        List<HttpHandler> handlers = allHandlers.stream().filter(handler -> handler.valid(request)).sorted(Comparator.comparingInt(HttpHandler::priority)).collect(Collectors.toList());
        for (HttpHandler handler : handlers) {
            if (handler.handle(request))
                break;
        }
        requests++;
        return request.getResponse();
    }

    public Response handleError(Throwable throwable) {
        return errorHandler.handleError(throwable, application.getErrorHandler());
    }

    public void addTotalTime(long time) {
        totalTime += time;
    }

    @Override
    public void listen(int port) {
        if (!isInitialised()) initalize();
        address = new InetSocketAddress(port);
        if (isRunning()) throw new ServerAlreadyRunningException();
    }

    public static class ServerAlreadyRunningException extends RuntimeException {

        public ServerAlreadyRunningException() {
        }

        public ServerAlreadyRunningException(String s) {
            super(s);
        }

        public ServerAlreadyRunningException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public ServerAlreadyRunningException(Throwable throwable) {
            super(throwable);
        }

        public ServerAlreadyRunningException(String s, Throwable throwable, boolean b, boolean b1) {
            super(s, throwable, b, b1);
        }
    }

    public static class BadRequestException extends RuntimeException {

        public BadRequestException() {
        }

        public BadRequestException(String s) {
            super(s);
        }

        public BadRequestException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public BadRequestException(Throwable throwable) {
            super(throwable);
        }

        public BadRequestException(String s, Throwable throwable, boolean b, boolean b1) {
            super(s, throwable, b, b1);
        }

    }
}
