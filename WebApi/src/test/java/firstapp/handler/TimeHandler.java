package firstapp.handler;

import me.david.webapi.handler.anotation.check.Handler;
import me.david.webapi.handler.anotation.check.Mapping;
import me.david.webapi.handler.anotation.handle.Get;
import me.david.webapi.handler.anotation.handle.UrlParam;
import me.david.webapi.response.Response;
import me.david.webapi.response.content.ResponseContent;
import me.david.webapi.response.content.file.StaticFileContent;
import me.david.webapi.request.Request;
import me.david.webapi.response.content.string.StaticStringContent;

import java.io.File;

@Handler
public class TimeHandler {

    @Mapping("tasks/?name?/status")
    public ResponseContent getTime(Request request, Response response, @Get("hey") String hey, @UrlParam("name") String name) {
        response.setHeader("Server", "my cool server");
        response.setHeader("X-Task", name);
        //response.setHeader("X-Hey", hey);
        return new StaticFileContent(new File("/home/david/Desktop/Programieren/java/davidlib/JavaDoc/allclasses-noframe.html"));
    }

    @Mapping("test/?id?")
    public ResponseContent aaa(@UrlParam("id") int id) {
        return new StaticStringContent("Id: " + id  );
    }


}
