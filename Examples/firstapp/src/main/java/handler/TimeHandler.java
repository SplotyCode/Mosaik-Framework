package firstapp.handler;

import io.github.splotycode.mosaik.webapi.handler.anotation.check.Handler;
import io.github.splotycode.mosaik.webapi.handler.anotation.check.Mapping;
import io.github.splotycode.mosaik.webapi.handler.anotation.handle.Get;
import io.github.splotycode.mosaik.webapi.handler.anotation.handle.UrlParam;
import io.github.splotycode.mosaik.webapi.response.Response;
import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;
import io.github.splotycode.mosaik.webapi.response.content.file.StaticFileContent;
import io.github.splotycode.mosaik.webapi.request.Request;
import io.github.splotycode.mosaik.webapi.response.content.string.StaticStringContent;

import java.io.File;

@Handler
public class TimeHandler {

    @Mapping("tasks/?name?/status")
    public ResponseContent getTime(Request request, Response response, @Get("hey") String hey, @UrlParam("name") String name) {
        response.setHeader("Server", "my cool server");
        response.setHeader("X-Task", name);
        //response.setHeader("X-Hey", hey);
        return new StaticFileContent(new File("../../docs/allclasses-noframe.html"));
    }

    @Mapping("test/?id?")
    public ResponseContent aaa(@UrlParam("id") int id) {
        return new StaticStringContent("Id: " + id  );
    }


}
