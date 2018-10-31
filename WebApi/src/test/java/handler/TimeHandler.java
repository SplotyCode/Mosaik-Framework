package handler;

import me.david.webapi.handler.anotation.check.Mapping;
import me.david.webapi.handler.anotation.handle.RequiredGet;
import me.david.webapi.response.Response;
import me.david.webapi.response.content.FileResponseContent;
import me.david.webapi.response.content.StringResponseContent;
import me.david.webapi.server.Request;

public class TimeHandler {

    @Mapping(path = "/time/get")
    public Response getTime(Request request) {
        return null;
    }


}
