package handler;

import me.david.webapi.handler.anotation.check.Handler;
import me.david.webapi.handler.anotation.handle.RequiredGet;
import me.david.webapi.response.Response;
import me.david.webapi.response.content.ResponseContent;
import me.david.webapi.response.content.StringResponseContent;
import me.david.webapi.server.Request;

@Handler
public class TimeHandler {
































































    @Handler
    public ResponseContent getTime(Request request, Response response, @RequiredGet("hey") String hey, java.util.logging.Handler handler) {
        response.setHeader("Server", "my cool server");
        response.setHeader("X-Hey", hey);
        return new StringResponseContent("Wooooooooops");
    }


}
