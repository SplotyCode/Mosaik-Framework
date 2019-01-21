package papa;

import me.david.davidlib.util.logger.Logger;
import me.david.webapi.handler.anotation.check.Handler;
import me.david.webapi.handler.anotation.check.Mapping;
import me.david.webapi.handler.anotation.handle.UrlParam;
import me.david.webapi.response.Response;
import me.david.webapi.response.content.file.FileResponseContent;
import me.david.webapi.response.content.ResponseContent;
import me.david.webapi.request.Request;

@Handler
public class MyHandler {

    private Logger logger = Logger.getInstance(getClass());

   // @Handler
    public ResponseContent abs(Request request) {
        FileResponseContent content = new FileResponseContent("/home/david/Desktop/Programieren/java/davidlib/WebApi/src/test/java/papa/test.html");
        content.manipulate().variable("name", "David");
        return content;
    }

    @Mapping("l/$id$")
    public ResponseContent redirect(@UrlParam("id") String linkId, Response response) {
        logger.info("hey");
        return null;
    }

    /*
     * TODO: fix return type
     */
    @Mapping()
    public boolean two() {
        return false;
    }

}
