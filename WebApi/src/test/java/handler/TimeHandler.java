package handler;

import me.david.webapi.handler.anotation.check.Handler;
import me.david.webapi.handler.anotation.check.Mapping;
import me.david.webapi.response.Response;
import me.david.webapi.server.Request;

@Handler
public class TimeHandler {

    @Handler
    public Response getTime(Request request) {
        throw new IllegalArgumentException("Oh no and Exception");
    }


}
