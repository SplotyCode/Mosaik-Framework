package io.github.splotycode.mosaik.webapi.handler;

import io.github.splotycode.mosaik.util.AlmostBoolean;
import io.github.splotycode.mosaik.util.reflection.ClassCollector;
import io.github.splotycode.mosaik.util.reflection.classregister.ListClassRegister;
import io.github.splotycode.mosaik.webapi.config.WebConfig;
import io.github.splotycode.mosaik.webapi.server.WebServer;
import lombok.Getter;

import java.util.Collection;
import java.util.HashSet;

@Getter
public class StaticHandlerFinder extends ListClassRegister<HttpHandler> implements HandlerFinder {

    private ClassCollector collector = ClassCollector.newInstance()
            .setAbstracation(AlmostBoolean.NO)
            .setOnlyClasses(true)
            .setNeedAssignable(HttpHandler.class);

    private WebServer webServer;

    public StaticHandlerFinder(WebServer webServer) {
        super(new HashSet<>());
        this.webServer = webServer;
    }

    @Override
    public Collection<HttpHandler> search() {
        collector.setVisibility(webServer.getConfig().getData(WebConfig.SEARCH_HANDLERS_VISIBILITY));
        return combind(collector.collectAllInstances());
    }

}
