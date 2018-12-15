package me.david.webapi.request.body;

import me.david.webapi.request.Request;
import me.david.webapi.server.AbstractWebServer;
import me.david.webapi.server.WebServer;

import java.util.Collection;

public final class RequestBodyHelper {

    public static RequestContent getRequestContent(Request request, WebServer webServer) {
        Collection<RequestContentHandler> handlers = webServer instanceof AbstractWebServer ?
                ((AbstractWebServer) webServer).getContentHandlers() :
                webServer.getApplication().getContentHandlerRegister().getList();

        RequestContentHandler contentHandler = handlers.stream().
                filter(handler -> handler.valid(request)).findFirst().
                orElse(null);

        return contentHandler == null ? new EmptyRequestContent() : contentHandler.create(request);
    }

}
