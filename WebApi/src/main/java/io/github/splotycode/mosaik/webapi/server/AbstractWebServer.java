package io.github.splotycode.mosaik.webapi.server;

import io.github.splotycode.mosaik.runtime.Runtime;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.datafactory.DataFactory;
import io.github.splotycode.mosaik.util.datafactory.LinkedDataFactory;
import io.github.splotycode.mosaik.util.init.InitialisedOnce;
import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.annotationbase.context.parameter.ParameterResolver;
import io.github.splotycode.mosaik.util.reflection.classregister.IListClassRegister;
import io.github.splotycode.mosaik.util.reflection.classregister.ListClassRegister;
import io.github.splotycode.mosaik.webapi.WebApplicationType;
import io.github.splotycode.mosaik.webapi.handler.HandlerFinder;
import io.github.splotycode.mosaik.webapi.handler.HttpHandler;
import io.github.splotycode.mosaik.webapi.handler.StaticHandlerFinder;
import io.github.splotycode.mosaik.webapi.handler.anotation.AnnotationHandlerFinder;
import io.github.splotycode.mosaik.webapi.request.HandleRequestException;
import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.request.body.RequestContentHandler;
import io.github.splotycode.mosaik.webapi.response.Response;
import io.github.splotycode.mosaik.webapi.response.error.ErrorFactory;
import io.github.splotycode.mosaik.webapi.response.error.ErrorHandler;
import io.github.splotycode.mosaik.webapi.session.SessionSystem;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractWebServer extends InitialisedOnce implements WebServer {

    private Logger logger = Logger.getInstance(getClass());

    @Getter protected InetSocketAddress address;
    @Getter protected boolean ssl;

    @Getter protected int requests = 0;
    @Getter protected long totalTime = 0;

    @Getter protected WebApplicationType application;
    @Setter @Getter protected ErrorHandler errorHandler = new ErrorHandler();
    @Getter private DataFactory config;

    public AbstractWebServer() {
        this(null);
    }

    public AbstractWebServer(WebApplicationType application) {
        this.application = application;
        if (application == null) {
            config = new DataFactory();
            WebApplicationType.setDefaults(config);
            contentHandlerRegister.registerAll(WebApplicationType.CONTENT_HANDLER_COLLECTOR, Runtime.getRuntime().getGlobalClassPath());
            parameterResolverRegister.registerAll(WebApplicationType.PARAMETER_RESOLVER_COLLECTOR, Runtime.getRuntime().getGlobalClassPath());
        } else {
            config = new LinkedDataFactory(application.getConfig());
        }
    }

    @Getter private List<HttpHandler> allHandlers = new ArrayList<>();
    @Getter private StaticHandlerFinder staticHandlerFinder = new StaticHandlerFinder(this);
    @Getter private AnnotationHandlerFinder annotationHandlerFinder = new AnnotationHandlerFinder(this);

    private List<ParameterResolver> parameterResolvers = new ArrayList<>();
    @Getter private ListClassRegister<ParameterResolver> parameterResolverRegister = new ListClassRegister<>(parameterResolvers, ParameterResolver.class);

    private List<RequestContentHandler> contentHandlers = new ArrayList<>();
    @Getter private ListClassRegister<RequestContentHandler> contentHandlerRegister = new ListClassRegister<>(contentHandlers, RequestContentHandler.class);

    private ListClassRegister<SessionSystem> sessionSystems = new ListClassRegister<>(new ArrayList<>(), SessionSystem.class);

    @Override
    public IListClassRegister<SessionSystem> getSessionLoader() {
        return sessionSystems;
    }

    @Override
    public Collection<SessionSystem> getSessionSystems() {
        return sessionSystems.getList();
    }

    @Override
    public void installSessionSystem(SessionSystem sessionSystem) {
        getSessionLoader().register(sessionSystem);
    }

    public Collection<RequestContentHandler> getContentHandlers() {
        return contentHandlerRegister.combind(application == null ? null : application.getContentHandlerRegister());
    }

    @Override
    public Collection<ParameterResolver> getParameterResolvers() {
        return parameterResolverRegister.combind(application == null ? null : application.getParameterResolveRegister());
    }

    public List<ParameterResolver> getLocalParameterResolvers() {
        return parameterResolvers;
    }

    @Override
    protected void init() {
        addFinder(staticHandlerFinder);
        addFinder(annotationHandlerFinder);
    }

    public void addFinder(HandlerFinder finder) {
        if (initialised) throw new IllegalStateException("Can not add finder if WebServer is already initialised");
        Collection<? extends HttpHandler> handlers = finder.search();
        logger.info(finder.getClass().getSimpleName() + " has registered " + handlers.size() + " handlers: " + StringUtil.join(handlers, HttpHandler::displayName));
        allHandlers.addAll(handlers);
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
        List<HttpHandler> handlers = allHandlers.stream().sorted(Comparator.comparingInt(HttpHandler::priority)).collect(Collectors.toList());
        for (HttpHandler handler : handlers) {
            try {
                if (!handler.valid(request)) {
                    continue;
                }
            } catch (Throwable throwable) {
                throw new HandleRequestException("Error while validating " + handler.getClass().getName(), throwable);
            }
            try {
                if (handler.handle(request)) {
                    break;
                }
            } catch (Throwable throwable) {
                throw new HandleRequestException("Error while Handling " + handler.getClass().getName(), throwable);
            }
        }
        requests++;
        return request.getResponse();
    }

    @Override
    public IListClassRegister<HttpHandler> getHttpHandlerRegister() {
        return staticHandlerFinder;
    }

    public Response handleError(Throwable throwable) {
        return errorHandler.handleError(throwable, application == null ? null : application.getErrorHandler());
    }

    public void addTotalTime(long time) {
        totalTime += time;
    }

    @Override
    public void listen(int port, boolean ssl) {
        if (!isInitialised()) initalize();
        address = new InetSocketAddress(port);
        this.ssl = ssl;
        if (isRunning()) throw new ServerAlreadyRunningException();
    }

    @Override
    public SocketAddress address() {
        return address;
    }

    @Override
    public int port() {
        return address.getPort();
    }
}
